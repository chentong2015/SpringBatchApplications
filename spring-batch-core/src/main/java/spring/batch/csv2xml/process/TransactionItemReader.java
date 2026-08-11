package spring.batch.csv2xml.process;

import org.springframework.batch.infrastructure.item.file.LineMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import spring.batch.csv2xml.bean.Transaction;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;

@Component
public class TransactionItemReader extends FlatFileItemReader<Transaction> {

    public TransactionItemReader(LineMapper<Transaction> transactionLineMapper) {
        super(new ClassPathResource("csv/transactions.csv"), transactionLineMapper);
    }
}