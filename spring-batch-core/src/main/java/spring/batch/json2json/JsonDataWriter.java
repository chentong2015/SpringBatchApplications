package spring.batch.json2json;

import org.springframework.batch.infrastructure.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.infrastructure.item.json.JsonFileItemWriter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.WritableResource;
import org.springframework.stereotype.Component;
import spring.batch.json2json.bean.Trade;

@Component
public class JsonDataWriter extends JsonFileItemWriter<Trade> {

    public JsonDataWriter() {
        // 使用Marshaller编制器将Object对象编制成Json Data数据
        super((WritableResource) new ClassPathResource("json/trades_new.json"), new JacksonJsonObjectMarshaller<>());
    }
}