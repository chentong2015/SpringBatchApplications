package spring.batch.db2csv.process;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import spring.batch.db2csv.bean.Person;
import org.springframework.batch.infrastructure.item.file.FlatFileItemWriter;
import org.springframework.batch.infrastructure.item.file.transform.LineAggregator;
import java.nio.file.FileSystems;
import java.nio.file.Path;

@Component
public class PersonItemWriter extends FlatFileItemWriter<Person> {

    public PersonItemWriter(LineAggregator<Person> csvLineAggregator) {
        super(csvLineAggregator);

        Path filepath = FileSystems.getDefault().getPath("drive_folder/csv/personsOutput.csv");
        setResource(new FileSystemResource(filepath));

        // All job repetitions should "append" to same output file
        setAppendAllowed(true);
    }
}