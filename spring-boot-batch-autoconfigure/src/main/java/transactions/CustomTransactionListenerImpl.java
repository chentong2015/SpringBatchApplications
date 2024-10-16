package transactions;

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
