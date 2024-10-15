package csv_to_xml.listener;

import csv_to_xml.model.Transaction;
import org.springframework.batch.core.ItemProcessListener;

// 监听数据处理过程的前后时间
public class MyItemProcessorListener implements ItemProcessListener<Transaction, Transaction> {

    @Override
    public void beforeProcess(Transaction item) {
        System.out.println("Before processing");
    }

    @Override
    public void afterProcess(Transaction item, Transaction result) {
        System.out.println("After processing");
    }

    @Override
    public void onProcessError(Transaction item, Exception e) {
        throw new RuntimeException("Processor failed");
    }
}
