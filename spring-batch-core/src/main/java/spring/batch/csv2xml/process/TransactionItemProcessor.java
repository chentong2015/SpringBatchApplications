package spring.batch.csv2xml.process;

import org.springframework.stereotype.Component;
import spring.batch.csv2xml.bean.Transaction;
import org.springframework.batch.infrastructure.item.ItemProcessor;

@Component
public class TransactionItemProcessor implements ItemProcessor<Transaction, Transaction> {

    @Override
    public Transaction process(Transaction transaction) {
        System.out.println("Process transaction: " + transaction);
        return transaction;
    }
}
