package spring.batch.csv2xml.process;

import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import spring.batch.csv2xml.bean.Transaction;
import org.springframework.batch.infrastructure.item.xml.StaxEventItemWriter;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import java.nio.file.FileSystems;
import java.nio.file.Path;

@Component
public class TransactionItemWriter extends StaxEventItemWriter<Transaction>  {

    public TransactionItemWriter(Jaxb2Marshaller transactionMarshaller) {
        super(transactionMarshaller);

        Path filepath = FileSystems.getDefault().getPath("drive_folder/xml/transactionsOutput.xml");
        setResource(new FileSystemResource(filepath));
        setRootTagName("transactionRecord");
    }
}