// Setup.java
import it.unisa.dia.gas.jpbc.*;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class Setup {
    private final Pairing pairing;
    private final Element g1;
    private final Element g2;

    public Setup() {
        pairing = PairingFactory.getPairing("params/curves/a.properties");
        g1 = pairing.getG1().newRandomElement().getImmutable();
        g2 = pairing.getG2().newRandomElement().getImmutable();
    }

    public BigInteger lcm(BigInteger a, BigInteger b) {
        BigInteger gcd = a.gcd(b);
        return a.multiply(b).divide(gcd);
    }

    public BigInteger PrimeNumberGenerator(int bitLength) {
        SecureRandom secureRandom = new SecureRandom();
        return BigInteger.probablePrime(bitLength, secureRandom);
    }

    public Parameters generateParameters() {
        BigInteger r = pairing.getZr().newRandomElement().toBigInteger();
        BigInteger p = PrimeNumberGenerator(256);
        BigInteger q = PrimeNumberGenerator(256);
        BigInteger o = p.multiply(q);
        BigInteger h = o.add(BigInteger.ONE);
        BigInteger lambda = lcm(p.subtract(BigInteger.ONE), q.subtract(BigInteger.ONE));

        while (!o.gcd(lambda).equals(BigInteger.ONE)) {
            r = pairing.getZr().newRandomElement().toBigInteger();
            p = PrimeNumberGenerator(256);
            q = PrimeNumberGenerator(256);
            o = p.multiply(q);
            h = o.add(BigInteger.ONE);
            lambda = lcm(p.subtract(BigInteger.ONE), q.subtract(BigInteger.ONE));
        }

        Element rElement = pairing.getZr().newElement().set(r).getImmutable();
        Element upsilon = g1.powZn(rElement).getImmutable();
        Element upsilonHat = g2.powZn(rElement).getImmutable();

        BigInteger mu = lambda.modInverse(o);

        List<BigInteger> esk = new ArrayList<>();
        List<BigInteger> epk = new ArrayList<>();
        epk.add(o);
        epk.add(h);
        esk.add(lambda);
        esk.add(mu);

        String hashFunction = "SHA-256";
        String[] attributeSet = {"tag1", "tag2"};

        return new Parameters(pairing, g1, g2, upsilon, upsilonHat, r, hashFunction, attributeSet, esk, epk);
    }

    public static class Parameters {
        public PublicParameters pp = new PublicParameters();
        public List<BigInteger> esk, epk;

        public static class PublicParameters {
            public Pairing pairing;
            public Element g1, g2, Upsilon, UpsilonHat;
            public BigInteger r;
            public String hashFunction;
            public String[] attributeSet;
        }

        public Parameters(Pairing pairing, Element g1, Element g2, Element Upsilon, Element UpsilonHat, BigInteger r,
                          String hashFunction, String[] attributeSet, List<BigInteger> esk, List<BigInteger> epk) {
            this.pp.pairing = pairing;
            this.pp.g1 = g1.getImmutable();
            this.pp.g2 = g2.getImmutable();
            this.pp.Upsilon = Upsilon.getImmutable();
            this.pp.UpsilonHat = UpsilonHat.getImmutable();
            this.pp.r = r;
            this.pp.hashFunction = hashFunction;
            this.pp.attributeSet = attributeSet;
            this.esk = esk;
            this.epk = epk;
        }
    }
}