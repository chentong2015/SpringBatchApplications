package spring.batch.json_reader_writer;

import org.springframework.batch.infrastructure.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.infrastructure.item.json.JsonFileItemWriter;
import org.springframework.batch.infrastructure.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.WritableResource;
import spring.batch.json_reader_writer.bean.Trade;

@Configuration
public class JsonDataWriter {

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
