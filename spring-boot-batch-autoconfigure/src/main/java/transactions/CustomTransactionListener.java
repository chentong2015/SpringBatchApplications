package transactions;

public interface CustomTransactionListener {

    void beforeTransaction();

    void afterTransaction();
}
