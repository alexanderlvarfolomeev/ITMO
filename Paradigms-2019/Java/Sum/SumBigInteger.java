import java.lang.*;
import java.math.*;

public class SumBigInteger {
    public static void main(String[] args) {
        BigInteger res = BigInteger.valueOf(0);
        for (String argsElem : args) {
            String[] str = argsElem.split("[^\\d+-]");
            for (String strElem : str) {
                if (!strElem.isEmpty()) {
                    res = res.add(new BigInteger(strElem));
                }
            }
        }
        System.out.print(res);
    }
}