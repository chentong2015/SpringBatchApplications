package com.batch.main.conversion.processor;

import com.batch.main.model.Record;
import com.batch.main.model.DbRecord;
import org.springframework.batch.item.ItemProcessor;

// TODO. Processor是针对读取的record item来进行解析，而非整个XML
public class RecordItemProcessor implements ItemProcessor<Record, DbRecord> {

    @Override
    public DbRecord process(Record record) throws Exception {
        DbRecord recordDB = new DbRecord();
        recordDB.setUsername(record.getUsername());
        recordDB.setId(record.getId());
        recordDB.setAmount(record.getAmount());
        return recordDB;
    }
}
