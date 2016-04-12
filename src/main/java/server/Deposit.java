package server;

/**
 * Created by Dotin School1 on 4/9/2016.
 */
public class Deposit {
    private String customer ;
    private String id;
    private Integer initBalance;
    private Integer upperBalance;

    public Deposit(String customer, String id, Integer initBalance, Integer upperBalance) {
        this.customer = customer;
        this.id = id;
        this.initBalance = initBalance;
        this.upperBalance = upperBalance;
    }

    public String getCustomer() {
        return customer;
    }

    public String getId() {
        return id;
    }

    public Integer getInitBalance() {
        return initBalance;
    }

    public Integer getUpperBalance() {
        return upperBalance;
    }

    public  void deposit(int amount) throws Exception {
        synchronized (this){
            if((initBalance + amount) > upperBalance){
                throw new Exception();
            }
            initBalance += amount;
        }
    }

    public  void withdraw(int amount) throws Exception {
        synchronized (this){

            if((initBalance - amount) < 0){
                throw new Exception();
            }
            initBalance -= amount;
        }
    }


}
