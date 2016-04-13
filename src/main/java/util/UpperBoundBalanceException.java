package util;

/**
 * Created by Dotin School1 on 4/13/2016.
 */
public class UpperBoundBalanceException extends Throwable {
    public UpperBoundBalanceException(String upperBalance){
        super("Your balance can not be more than " + upperBalance);
    }
}
