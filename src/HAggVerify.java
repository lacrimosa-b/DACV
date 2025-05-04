import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class HAggVerify {
    public static boolean HAggverify(Setup.Parameters.PublicParameters pp, DKGen.DK DK, List<String> messages, BigInteger aggSig) {
        List<BigInteger> Hpk = DK.Hpk;
        BigInteger productE = BigInteger.ONE;
        List<BigInteger> eList = new ArrayList<>();
        for (String msg : messages) {
            BigInteger e_m = DKGen.PrimeSamp(pp, msg, Hpk.get(1));
            productE = productE.multiply(e_m);
            eList.add(e_m);
        }

        BigInteger sumTerm = BigInteger.ZERO;
        for (int i = 0; i < eList.size(); i++) {
            BigInteger productOthers = BigInteger.ONE;
            for (int j = 0; j < eList.size(); j++) {
                if (j != i) {
                    productOthers = productOthers.multiply(eList.get(j));
                }
            }
            sumTerm = sumTerm.add(productOthers);
        }

        BigInteger left = aggSig.modPow(productE, Hpk.getFirst());
        BigInteger right = Hpk.getLast().modPow(sumTerm, Hpk.getFirst());

        return left.equals(right);
    }
}