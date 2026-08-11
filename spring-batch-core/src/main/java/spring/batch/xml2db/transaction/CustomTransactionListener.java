package spring.batch.xml2db.transaction;

public interface CustomTransactionListener {

    void beforeTransaction();

    void afterTransaction();
}
