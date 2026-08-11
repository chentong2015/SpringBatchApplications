package spring.batch.csv2xml.process;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import spring.batch.csv2xml.bean.Transaction;
import org.springframework.batch.infrastructure.item.xml.StaxEventItemWriter;
import org.springframework.core.io.WritableResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Component
public class TransactionItemWriter extends StaxEventItemWriter<Transaction>  {

    public TransactionItemWriter(Jaxb2Marshaller transactionMarshaller) {
        super((WritableResource) new ClassPathResource("xml/transactionsOutput.xml"), transactionMarshaller);
        setRootTagName("transactionRecord");
    }
}