package spring.batch.xml2db.process;

import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;
import spring.batch.xml2db.bean.Record;
import spring.batch.xml2db.bean.DbRecord;

@Component
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
