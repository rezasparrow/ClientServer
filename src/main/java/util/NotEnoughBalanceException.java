package util;

/**
 * Created by Dotin School1 on 4/13/2016.
 */
public class NotEnoughBalanceException extends Throwable {
    public NotEnoughBalanceException(){
        super("Your balance is not enough");
    }
}
