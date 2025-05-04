//DKGen.java
import it.unisa.dia.gas.jpbc.Element;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.security.MessageDigest;
import java.security.SecureRandom;


public class DKGen {
    public DK generateDK(Setup.Parameters.PublicParameters pp, int l) {
        //generate S
        Element w = pp.pairing.getZr().newRandomElement().getImmutable();
        Element W = pp.g2.powZn(w).getImmutable();


        //generate H
        SecureRandom random = new SecureRandom();
        int bitlength = 512;
        BigInteger xi = BigInteger.probablePrime(bitlength, random);
        BigInteger iota = BigInteger.probablePrime(bitlength, random);
        BigInteger eta = BigInteger.probablePrime(bitlength, random);
        BigInteger gamma = BigInteger.probablePrime(bitlength, random);
//        BigInteger xi = BigInteger.valueOf(3);
//        BigInteger iota = BigInteger.valueOf(5);
//        BigInteger eta = BigInteger.valueOf(17);
//        BigInteger gamma = BigInteger.valueOf(23);
        BigInteger N = eta.multiply(gamma);
        List<BigInteger> Hsk = Arrays.asList(eta, gamma, xi, iota);
        List<BigInteger> Hpk = Arrays.asList(N, xi, iota);

        //generate T
        Element[] Tsk = new Element[l];
        Element[] Tpk = new Element[l];
        for (int i = 0; i < l; i++) {
            Tsk[i] = pp.pairing.getZr().newRandomElement();
            Tpk[i] = pp.g2.powZn(Tsk[i]);

        }

        return new DK(w, W, Hsk, Hpk, Tsk, Tpk);
    }

    public static BigInteger PrimeSamp(Setup.Parameters.PublicParameters pp, String message, BigInteger xi) {
        try {
            MessageDigest md = MessageDigest.getInstance(pp.hashFunction);
            byte[] hash = md.digest((xi.toString() + message).getBytes());
            BigInteger num = new BigInteger(1, hash).abs();
            return num.nextProbablePrime();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hash algorithm not found", e);
        }
    }

    public static class DK {
        public Element Ssk, Spk;
        public List<BigInteger> Hpk, Hsk;
        public Element[] Tsk, Tpk;

        public DK(Element Ssk, Element Spk, List<BigInteger> Hsk, List<BigInteger> Hpk, Element[] Tsk, Element[] Tpk) {
            this.Ssk = Ssk;
            this.Spk = Spk;
            this.Hsk = Hsk;
            this.Hpk = Hpk;
            this.Tsk = Tsk;
            this.Tpk = Tpk;
        }
    }
}
