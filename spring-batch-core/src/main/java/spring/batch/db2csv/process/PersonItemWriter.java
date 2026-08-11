package spring.batch.db2csv.process;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import spring.batch.db2csv.bean.Person;
import org.springframework.batch.infrastructure.item.file.FlatFileItemWriter;
import org.springframework.batch.infrastructure.item.file.transform.LineAggregator;
import org.springframework.core.io.WritableResource;

@Component
public class PersonItemWriter extends FlatFileItemWriter<Person> {

    public PersonItemWriter(LineAggregator<Person> csvLineAggregator) {
        super((WritableResource) new ClassPathResource("csv/personsOutput.csv"), csvLineAggregator);
        // All job repetitions should "append" to same output file
        setAppendAllowed(true);
    }
}