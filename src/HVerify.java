import java.math.BigInteger;

import java.util.List;

public class HVerify {
    public static boolean Hverify(Setup.Parameters.PublicParameters pp, DKGen.DK DK, BigInteger sigma, String message) {
        List<BigInteger> Hpk = DK.Hpk;
        BigInteger e_m = DKGen.PrimeSamp(pp, message, Hpk.get(1));
        BigInteger left = sigma.modPow(e_m, Hpk.getFirst());
        return left.equals(Hpk.getLast().mod(Hpk.getFirst()));
    }
}