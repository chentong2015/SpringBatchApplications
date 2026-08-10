package spring.batch.csv_to_xml.process;

import spring.batch.csv_to_xml.bean.Transaction;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.xml.StaxEventItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.WritableResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class TransactionItemWriter {

    // WritableResource输出资源的路径定位在项目路径下
    @Value("file:spring-batch-example/src/main/resources/xml/transactions_output.xml")
    private Resource outputXml;

    @Bean
    public ItemWriter<Transaction> itemWriter() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(Transaction.class);

        StaxEventItemWriter<Transaction> itemWriter = new StaxEventItemWriter<>((WritableResource) outputXml, marshaller);
        itemWriter.setRootTagName("transactionRecord");
        return itemWriter;
    }
}
