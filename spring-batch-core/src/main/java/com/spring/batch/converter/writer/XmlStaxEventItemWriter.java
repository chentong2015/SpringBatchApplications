package com.spring.batch.converter.writer;

import com.spring.batch.model.Transaction;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.WritableResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;

@Component
public class XmlStaxEventItemWriter implements FactoryBean<ItemWriter<Transaction>> {

    // 当前项目根目录下的文件相对路径
    @Value("file:spring-batch-core/xml/output.xml")
    private Resource outputXml;

    @Override
    public ItemWriter<Transaction> getObject() throws Exception {
        StaxEventItemWriter<Transaction> itemWriter = new StaxEventItemWriter<>();
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(Transaction.class);

        itemWriter.setMarshaller(marshaller);
        itemWriter.setRootTagName("transactionRecord");
        itemWriter.setResource((WritableResource) outputXml);
        return itemWriter;
    }

    @Override
    public Class<?> getObjectType() {
        return XmlStaxEventItemWriter.class;
    }
}
