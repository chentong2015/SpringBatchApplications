package database_to_file;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootStarterBatchApplication {

    public static void main(String[] args) {
        System.out.println("Start application..");
        SpringApplication.run(SpringBootStarterBatchApplication.class, args);
    }
}