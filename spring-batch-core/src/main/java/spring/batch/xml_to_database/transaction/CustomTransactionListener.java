package spring.batch.xml_to_database.transaction;

public interface CustomTransactionListener {

    void beforeTransaction();

    void afterTransaction();
}
