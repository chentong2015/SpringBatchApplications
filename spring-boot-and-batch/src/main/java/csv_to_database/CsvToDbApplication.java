package csv_to_database;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CsvToDbApplication {

    public static void main(String[] args) {
        System.out.println("Start application..");
        SpringApplication.run(CsvToDbApplication.class, args);
    }
}