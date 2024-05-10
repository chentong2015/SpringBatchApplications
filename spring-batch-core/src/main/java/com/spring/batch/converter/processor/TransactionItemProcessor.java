package com.spring.batch.converter.processor;

import com.spring.batch.model.Transaction;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.NonNull;

// Processor<I, O> 解析器
// 从Input数据到Output, 定义读取数据的加工过程
public class TransactionItemProcessor implements ItemProcessor<Transaction, Transaction> {

    @Override
    public Transaction process(@NonNull Transaction transaction) {
        System.out.println("processing transaction item: " + transaction);
        return transaction;
    }
}
