package spring.batch.xml_to_database.process;

import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;
import spring.batch.xml_to_database.bean.Record;
import spring.batch.xml_to_database.bean.DbRecord;

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
