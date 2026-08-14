package project.transaction;

public interface CustomTransactionListener {

    void beforeTransaction();

    void afterTransaction();
}
