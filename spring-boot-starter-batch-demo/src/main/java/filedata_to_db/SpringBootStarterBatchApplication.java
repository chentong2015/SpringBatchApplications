package filedata_to_db;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// @EnableBatchProcessing 注入相关的Batch Bean
public class SpringBootStarterBatchApplication {

    public static void main(String[] args) {
        System.out.println("Start application..");
        SpringApplication.run(SpringBootStarterBatchApplication.class, args);
    }
}