package spring.batch.json2json.process;

import org.jspecify.annotations.Nullable;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;
import spring.batch.json2json.bean.Trade;

@Component
public class JsonDataProcessor implements ItemProcessor<Trade, Trade> {

    @Override
    public @Nullable Trade process(Trade item) throws Exception {
        System.out.println("Process Item: " + item);
        item.setCustomer(item.getCustomer() + " Processed");
        return item;
    }
}