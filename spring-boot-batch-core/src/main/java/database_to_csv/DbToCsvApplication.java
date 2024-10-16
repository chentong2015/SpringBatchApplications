package database_to_csv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DbToCsvApplication {

    public static void main(String[] args) {
        System.out.println("Start application..");
        SpringApplication.run(DbToCsvApplication.class, args);
    }
}