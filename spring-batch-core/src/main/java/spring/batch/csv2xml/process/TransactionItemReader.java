package spring.batch.csv2xml.process;

import org.springframework.batch.infrastructure.item.file.LineMapper;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import spring.batch.csv2xml.bean.Transaction;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;

import java.nio.file.FileSystems;
import java.nio.file.Path;

@Component
public class TransactionItemReader extends FlatFileItemReader<Transaction> {

    public TransactionItemReader(LineMapper<Transaction> transactionLineMapper) {
        super(transactionLineMapper);

        Path filepath = FileSystems.getDefault().getPath("drive_folder/csv/transactions.csv");
        setResource(new FileSystemResource(filepath));
    }
}