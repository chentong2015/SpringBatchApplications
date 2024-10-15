package csv_to_xml.conversion;

import csv_to_xml.model.Transaction;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.NonNull;

// Processor<I, O> 解析器
// 从Input数据到Output, 定义读取数据的加工过程
public class TransactionItemProcessor implements ItemProcessor<Transaction, Transaction> {

    // 通过Mapper来加工和映射原始数据到目标对象
    private MyRecordMapper mapper = new MyRecordMapper();

    @Override
    public Transaction process(@NonNull Transaction transaction) {
        Object target = mapper.map(transaction);
        return transaction;
    }
}
