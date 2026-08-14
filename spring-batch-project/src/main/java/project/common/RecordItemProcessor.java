package project.common;

import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;
import project.bean.DbRecord;
import project.bean.Record;

@Component
public class RecordItemProcessor implements ItemProcessor<Record, DbRecord> {

    @Override
    public DbRecord process(Record record) throws Exception {
        DbRecord recordDB = new DbRecord();
        recordDB.setId(record.getId());
        recordDB.setUsername(record.getUsername());
        recordDB.setAmount(record.getAmount());
        return recordDB;
    }
}
