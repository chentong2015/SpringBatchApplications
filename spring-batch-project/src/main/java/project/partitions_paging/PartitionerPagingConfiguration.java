package project.partitions_paging;

import java.util.HashMap;
import java.util.Map;

import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.parameters.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.database.JdbcPagingItemReader;
import org.springframework.batch.infrastructure.item.database.PagingQueryProvider;
import org.springframework.batch.infrastructure.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.infrastructure.item.file.FlatFileItemWriter;
import org.springframework.batch.infrastructure.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.infrastructure.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.WritableResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import project.common.bean.DbRecord;

import javax.sql.DataSource;

@Configuration
public class PartitionerPagingConfiguration {

    @Autowired
    private DataSource dataSource;

    @Bean(name = "partitionPagingJob")
    public Job partitionPagingJob(JobRepository jobRepository, @Qualifier("masterPagingStep") Step masterPagingStep) {
        return new JobBuilder("Partitions Paging Job", jobRepository)
              .incrementer(new RunIdIncrementer())
              .start(masterPagingStep)
              .build();
    }

    @Bean(name = "masterPagingStep")
    public Step masterPagingStep(JobRepository jobRepository,
                                 RangedIndexPartitioner rangePartitioner,
                                 ThreadPoolTaskExecutor taskExecutor,
                                 @Qualifier("slavePagingStep") Step slavePagingStep) {
        return new StepBuilder("Master Paging Step", jobRepository)
              .partitioner("Slave Page Step", rangePartitioner)
              .gridSize(4)
              .taskExecutor(taskExecutor)
              .step(slavePagingStep)
              .build();
    }

    // TODO. 多线程独立执行各自操作, 查询解析加存储流程
    @Bean(name = "slavePagingStep")
    public Step slavePagingStep(JobRepository jobRepository) throws Exception {
        return new StepBuilder("workerStep", jobRepository)
              .<DbRecord, DbRecord>chunk(100) // 每批处理100个数据, 不够再查询
              .reader(slaveReader(0, 0)) // 设置0为占位符合, 并非真实参数
              .processor(slaveProcessor())
              .writer(slaveWriter(0, 0))
              .build();
    }

    // TODO. 使用JDBC以分页方式读取数据库记录, 每页大小由setPageSize(int)方法指定
    // 当调用read()方法时，若需要更多数据系统会自动请求后续页面，并返回对应于当前位置的对象
    // 分页性能取决于数据库自身用于限制返回行数的功能特性
    @Bean
    @StepScope
    public JdbcPagingItemReader<DbRecord> slaveReader(@Value("#{stepExecutionContext['fromId']}") int fromId,
                                                      @Value("#{stepExecutionContext['toId']}") int toId) throws Exception {
        JdbcPagingItemReader<DbRecord> reader = new JdbcPagingItemReader<>(dataSource, queryProvider());
        Map<String, Object> parameterValues = new HashMap<>();
        parameterValues.put("fromId", fromId); // 设置参数的真实值
        parameterValues.put("toId", toId);
        reader.setParameterValues(parameterValues);

        reader.setSaveState(false); // 并发环境下不要保存ExecutionContext上下文
        reader.setPageSize(200);    // LIMIT每次分页查询的数据, 直到全部返回
        reader.setRowMapper(new BeanPropertyRowMapper<>() {{
            setMappedClass(DbRecord.class); // ROW数据映射到Object对象
        }});
        return reader;
    }

    // 根据StepExecutionContext构建条件查询语句, 返回特定范围的数据
    @Bean
    public PagingQueryProvider queryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean provider = new SqlPagingQueryProviderFactoryBean();
        provider.setDataSource(dataSource);
        provider.setSelectClause("select id, username, amount");
        provider.setFromClause("from record_db");
        provider.setWhereClause("where id >= :fromId and id <= :toId");
        provider.setSortKey("id");
        return provider.getObject();
    }

    @Bean
    @StepScope
    public ItemProcessor<DbRecord, DbRecord> slaveProcessor() {
        return new ItemProcessor<>() {
            @Override
            public @Nullable DbRecord process(DbRecord dbRecord) throws Exception {
                // System.out.println("Processing : " + dbRecord.getId() + " : " + dbRecord.getUsername());
                return dbRecord;
            }
        };
    }

    @Bean
    @StepScope
    public FlatFileItemWriter<DbRecord> slaveWriter(@Value("#{stepExecutionContext['fromId']}") int fromId,
                                                    @Value("#{stepExecutionContext['toId']}") int toId) {
        WritableResource writableResource = new FileSystemResource("drive_folder/csv/db-record" + fromId + "-" + toId + ".csv");

        DelimitedLineAggregator<DbRecord> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");
        lineAggregator.setFieldExtractor(new BeanWrapperFieldExtractor<>() {{
            setNames(new String[]{"id", "username", "amount"});
        }});

        FlatFileItemWriter<DbRecord> reader = new FlatFileItemWriter<>(writableResource, lineAggregator);
        reader.setAppendAllowed(true); // Target file should be appended if it already exists
        return reader;
    }
}