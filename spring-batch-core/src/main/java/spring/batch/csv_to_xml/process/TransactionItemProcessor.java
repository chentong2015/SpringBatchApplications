package spring.batch.csv_to_xml.process;

import org.springframework.stereotype.Component;
import spring.batch.csv_to_xml.bean.Transaction;
import org.springframework.batch.infrastructure.item.ItemProcessor;

// Processor<I, O>解析器:
// - 从Input数据到Output, 定义读取数据的加工过程
// - 针对读取的record item来进行解析，而非整个XML
@Component
public class TransactionItemProcessor implements ItemProcessor<Transaction, Transaction> {

    @Override
    public Transaction process(Transaction transaction) {
        System.out.println("Process transaction: " + transaction);
        return transaction;
    }
}
