package spring.batch.csv2xml.bean;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateXmlAdapter extends XmlAdapter<String, LocalDate> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public LocalDate unmarshal(String value) {
        return LocalDate.parse(value, FORMATTER);
    }

    // 定义将LocalDate编织成字符串写入到XML标签
    @Override
    public String marshal(LocalDate value) {
        return value != null ? value.format(FORMATTER) : null;
    }
}
