package spring.batch.json2json.process;

import org.springframework.batch.infrastructure.item.json.JacksonJsonObjectReader;
import org.springframework.batch.infrastructure.item.json.JsonItemReader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import spring.batch.json2json.bean.Trade;

import java.nio.file.FileSystems;
import java.nio.file.Path;

@Component
public class JsonDataReader extends JsonItemReader<Trade> {

    // 设置Json Data需要被解析到的类型对象Object
    public JsonDataReader() {
        super(new JacksonJsonObjectReader<>(Trade.class));

        Path filepath = FileSystems.getDefault().getPath("drive_folder/json/trades.json");
        setResource(new FileSystemResource(filepath));
    }
}