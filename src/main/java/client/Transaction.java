package client;

/**
 * Created by Dotin School1 on 4/9/2016.
 */
public class Transaction {
    private String id;
    private String type;
    private Integer amount;
    private String deposit;

    public Transaction(String id, String type, Integer amount, String deposit) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.deposit = deposit;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public Integer getAmount() {
        return amount;
    }

    public String getDeposit() {
        return deposit;
    }

}
