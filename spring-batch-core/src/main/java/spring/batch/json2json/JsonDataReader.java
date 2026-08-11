package spring.batch.json2json;

import org.springframework.batch.infrastructure.item.json.JacksonJsonObjectReader;
import org.springframework.batch.infrastructure.item.json.JsonItemReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import spring.batch.json2json.bean.Trade;

@Component
public class JsonDataReader extends JsonItemReader<Trade> {

    // 设置Json Data需要被解析到的类型对象Object
    public JsonDataReader() {
        super(new ClassPathResource("json/trades.json"), new JacksonJsonObjectReader<>(Trade.class));
    }
}