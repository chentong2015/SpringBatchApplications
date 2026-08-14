package project.common.transaction;

public interface CustomTransactionListener {

    void beforeTransaction();

    void afterTransaction();
}
