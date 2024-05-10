package com.batch.main.conveter.processor;

import com.batch.main.model.Record;
import com.batch.main.model.RecordDB;
import com.batch.main.model.Records;
import com.batch.main.model.RecordsDB;
import org.springframework.batch.item.ItemProcessor;

import java.util.ArrayList;
import java.util.List;

public class RecordItemProcessor implements ItemProcessor<Records, RecordsDB> {

    @Override
    public RecordsDB process(Records records) throws Exception {
        RecordsDB recordsDB = new RecordsDB();
        List<RecordDB> recordDBList = new ArrayList<>();
        for (Record record: records.getRecordList()) {
            RecordDB recordDB = new RecordDB();
            recordDB.setUsername(record.getUsername());
            recordDB.setId(record.getUserId());
            recordDB.setAmount(record.getAmount());
            recordDBList.add(recordDB);
        }
        recordsDB.setRecordDBList(recordDBList);
        return recordsDB;
    }
}
