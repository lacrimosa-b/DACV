import java.math.BigInteger;
import java.util.List;

public class HAgg {
    public static BigInteger HAggregate(DKGen.DK DK, List<BigInteger> signatures) {
        List<BigInteger> Hpk = DK.Hpk;
        BigInteger aggSig = BigInteger.ONE;
        for (BigInteger sig : signatures) {
            aggSig = aggSig.multiply(sig).mod(Hpk.getFirst());
        }
        return aggSig;
    }
}