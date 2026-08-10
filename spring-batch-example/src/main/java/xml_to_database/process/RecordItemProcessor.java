package xml_to_database.process;

import beans.xml.Record;
import beans.DbRecord;
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
