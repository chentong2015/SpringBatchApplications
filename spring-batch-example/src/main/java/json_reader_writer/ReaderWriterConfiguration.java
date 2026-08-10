package json_reader_writer;

import beans.Trade;
import org.springframework.batch.infrastructure.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.infrastructure.item.json.JacksonJsonObjectReader;
import org.springframework.batch.infrastructure.item.json.JsonFileItemWriter;
import org.springframework.batch.infrastructure.item.json.JsonItemReader;
import org.springframework.batch.infrastructure.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.batch.infrastructure.item.json.builder.JsonItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.WritableResource;

@Configuration
public class ReaderWriterConfiguration {

    // 设置Json Data需要被解析到的类型对象Object
    @Bean
    public JsonItemReader<Trade> jsonItemReader() {
        return new JsonItemReaderBuilder<Trade>()
                .name("tradeJsonItemReader")
                .resource(new ClassPathResource("json/trades.json"))
                .jsonObjectReader(new JacksonJsonObjectReader<>(Trade.class))
                .build();
    }

    // 使用Marshaller编制器将Object对象编制成Json Data数据
    @Bean
    public JsonFileItemWriter<Trade> jsonFileItemWriter() {
        return new JsonFileItemWriterBuilder<Trade>()
                .jsonObjectMarshaller(new JacksonJsonObjectMarshaller<>())
                .resource((WritableResource) new ClassPathResource("json/trades_new.json"))
                .name("tradeJsonFileItemWriter")
                .build();
    }
}
