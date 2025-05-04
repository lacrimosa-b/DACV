import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


public class HLocalAggOpen {
    public static BigInteger HLocalAggopen(Setup.Parameters.PublicParameters pp, DKGen.DK DK, List<String> subsetMessages,
                                           List<String> allMessages, BigInteger aggSig) {
        List<BigInteger> Hpk = DK.Hpk;
        BigInteger N = Hpk.getFirst();
        BigInteger xi = Hpk.get(1);
        BigInteger iota = Hpk.getLast();

        List<BigInteger> e_miList = new ArrayList<>();
        for (String msg : subsetMessages) {
            e_miList.add(DKGen.PrimeSamp(pp, msg, xi));
        }

        List<BigInteger> e_mkList = new ArrayList<>();
        for (String msg : allMessages) {
            if (!subsetMessages.contains(msg)) {
                e_mkList.add(DKGen.PrimeSamp(pp, msg, xi));
            }
        }

        BigInteger e_Mt = BigInteger.ONE;
        BigInteger sum_e_mi = BigInteger.ZERO;
        for(BigInteger mi : e_miList) {
            e_Mt = e_Mt.multiply(mi);
            sum_e_mi = sum_e_mi.add(mi);
        }

        BigInteger product_e_mk = BigInteger.ONE;
        for(BigInteger mk : e_mkList) {
            product_e_mk = product_e_mk.multiply(mk);
        }

        BigInteger sum_product_e_mk = BigInteger.ZERO;
        for (BigInteger em : e_mkList) {
            BigInteger product = product_e_mk.divide(em);
            sum_product_e_mk = sum_product_e_mk.add(product);
        }

        BigInteger f_Mt = BigInteger.ZERO;
        for(BigInteger mi : e_miList) {
            f_Mt = f_Mt.add(e_Mt.divide(mi).multiply(product_e_mk));
        }
        BigInteger x = aggSig.modPow(product_e_mk, N).multiply(iota.modPow(sum_product_e_mk.negate(), N)).mod(N);

        BigInteger shamirHint = shamir(x, iota, e_Mt, f_Mt, N);

        BigInteger sum_product_e_mi = BigInteger.ZERO;
        for (BigInteger em : e_miList) {
            BigInteger product = e_Mt.divide(em);
            sum_product_e_mi = sum_product_e_mi.add(product);
        }

        return shamirHint.modPow(sum_product_e_mi, N);
    }

    private static BigInteger[] exgcd(BigInteger a, BigInteger b) {
        if (b.equals(BigInteger.ZERO)) {
            return new BigInteger[] {BigInteger.ONE, BigInteger.ZERO};
        }
        BigInteger[] result = exgcd(b, a.mod(b));
        BigInteger x = result[0], y = result[1];
        return new BigInteger[] {y, x.subtract(a.divide(b).multiply(y))};
    }

    public static BigInteger shamir(BigInteger x, BigInteger y, BigInteger a, BigInteger b, BigInteger N) {
        BigInteger[] exgcd = exgcd(a, b);
        BigInteger alpha = exgcd[0], beta = exgcd[1];
        return y.modPow(alpha, N).multiply(x.modPow(beta, N)).mod(N);
    }
}