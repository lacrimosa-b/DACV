import java.math.BigInteger;
import java.util.List;

public class HSign {
    public static BigInteger Hsign(Setup.Parameters.PublicParameters pp, DKGen.DK DK, String message) {
        List<BigInteger> Hsk = DK.Hsk;
        BigInteger e_m = DKGen.PrimeSamp(pp, message, Hsk.get(2));
        BigInteger phi = Hsk.getFirst().subtract(BigInteger.ONE);
        phi = phi.multiply(Hsk.get(1).subtract(BigInteger.ONE));
        BigInteger exponent = e_m.modInverse(phi);
        return Hsk.getLast().modPow(exponent, Hsk.getFirst().multiply(Hsk.get(1)));
    }


}