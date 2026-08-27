package spring.batch.common.transaction;

public class CustomTransactionListener {

    public void beforeTransaction() {
        System.out.println("Test before transaction");
    }

    public void afterTransaction() {
        System.out.println("Test after transaction");
    }
}
