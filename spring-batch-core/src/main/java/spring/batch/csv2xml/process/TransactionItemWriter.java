package spring.batch.csv2xml.process;

import org.springframework.stereotype.Component;
import spring.batch.csv2xml.bean.Transaction;
import org.springframework.batch.infrastructure.item.xml.StaxEventItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.WritableResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Component
public class TransactionItemWriter extends StaxEventItemWriter<Transaction>  {

    // WritableResource输出资源的路径定位在项目路径下
    @Value("file:spring-batch-example/src/main/resources/xml/transactionsOutput.xml")
    private Resource outputXml;

    public TransactionItemWriter() {
        super(null);
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(Transaction.class);

        setRootTagName("transactionRecord");
        setMarshaller(marshaller);
        setResource((WritableResource) outputXml);
    }
}