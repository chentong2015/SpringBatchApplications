package xml_to_database;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// @EnableBatchProcessing 测试Batch自动装配的Beans
public class XmlToDbApplication {

    public static void main(String[] args) {
        SpringApplication.run(XmlToDbApplication.class, args);
    }
}