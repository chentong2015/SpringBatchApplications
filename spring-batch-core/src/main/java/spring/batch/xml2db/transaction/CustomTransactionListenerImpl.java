package spring.batch.xml2db.transaction;

public class CustomTransactionListenerImpl implements CustomTransactionListener {

    @Override
    public void beforeTransaction() {
        System.out.println("Test before transaction");
    }

    @Override
    public void afterTransaction() {
        System.out.println("Test after transaction");
    }
}
