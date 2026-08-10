package spring.batch.json_reader_writer;

import org.springframework.batch.infrastructure.item.json.JacksonJsonObjectReader;
import org.springframework.batch.infrastructure.item.json.JsonItemReader;
import org.springframework.batch.infrastructure.item.json.builder.JsonItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import spring.batch.json_reader_writer.bean.Trade;

@Configuration
public class JsonDataReader {

    // 设置Json Data需要被解析到的类型对象Object
    @Bean
    public JsonItemReader<Trade> jsonItemReader() {
        return new JsonItemReaderBuilder<Trade>()
                .name("tradeJsonItemReader")
                .resource(new ClassPathResource("json/trades.json"))
                .jsonObjectReader(new JacksonJsonObjectReader<>(Trade.class))
                .build();
    }
}
