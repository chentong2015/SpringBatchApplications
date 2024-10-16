package transactions;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class TransactionTemplateConfiguration {

    // TODO. 事务模板和数据持久化层所使用的DataSource一致
    @Bean
    public CustomTransactionTemplate customTransactionTemplate(DataSource dataSource) {
        return new CustomTransactionTemplate(dataSource);
    }
}