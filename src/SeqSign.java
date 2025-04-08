import it.unisa.dia.gas.jpbc.Element;

import java.math.BigInteger;
import java.util.List;

public class SeqSign {
    public BigInteger Lfunc(BigInteger x, BigInteger o) {
        System.out.println("mod: " + x.subtract(BigInteger.ONE).mod(o));
        return (x.subtract(BigInteger.ONE)).divide(o);
    }

    public SeqSig SeqSign(Element Ssk, Element Spk, List<BigInteger> esk, List<BigInteger> epk, List<BigInteger> AUX, SeqSig PrevSig,
                          Element[] mList, Element[] SpkList, Element m, Setup.Parameters.PublicParameters pp, int d) {

        BigInteger o = epk.getFirst(), h = epk.getLast();

        if (d == 0) {
            PrevSig.Sig1 = pp.g1;
            PrevSig.Sig2 = pp.Upsilon;
            PrevSig.caux = BigInteger.ONE;
        }
        else if (m == pp.pairing.getZr().newZeroElement()) {
            System.err.println("m == pp.pairing.getZr().newZeroElement");
            return null;
        }
        else if (d > 0 && !new SeqVerify().SeqVerify(SpkList, mList, PrevSig, pp, d)) {
            System.err.println("Verify failed");
            return null;
        }
        else {
            for (int i = 0; i < d; i++) {
                if (SpkList[i].equals(Spk)) {
                    System.err.println("SpkList[i] == Spk");
                    return null;
                }
            }
        }
        Element t = pp.pairing.getZr().newRandomElement();
        Element newSig1 = PrevSig.Sig1.powZn(t);
        Element newSig2 = PrevSig.Sig1.powZn(Ssk.mul(m)).mul(PrevSig.Sig2).powZn(t);
        Element aux = Ssk.mul(m).getImmutable();
        BigInteger caux;
        caux = (h.modPow(aux.toBigInteger(), o.pow(2))
                .multiply(pp.r.modPow(o,o.pow(2))));
        caux = caux.multiply(PrevSig.caux).mod(o.pow(2));
        AUX.add(caux);

        return new SeqSig(newSig1, newSig2, caux);
    }

    public static class SeqSig {
        public Element Sig1, Sig2;
        BigInteger caux;
        public SeqSig(Element Sig1, Element Sig2, BigInteger caux) {
            this.Sig1 = Sig1.getImmutable();
            this.Sig2 = Sig2.getImmutable();
            this.caux = caux;
        }
    }
}
