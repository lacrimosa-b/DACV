import it.unisa.dia.gas.jpbc.Element;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
public class SLocalAggOpen {
    public BigInteger Lfunc(BigInteger x, BigInteger o) {
        return (x.subtract(BigInteger.ONE)).divide(o);
    }

    public hint SLocalAggOpen(List<BigInteger> AUX, List<BigInteger> esk, List<BigInteger> epk,
                              Setup.Parameters.PublicParameters pp, int Start, int End, List<Integer> indexList) {
        BigInteger lambda = esk.getFirst();
        BigInteger mu = esk.getLast();
        BigInteger o = epk.getFirst();
        List<Element> hints = new ArrayList<>();

        // 计算离散index的hint
        BigInteger CAux;
        Element tmp = pp.pairing.getZr().newZeroElement();
        if (indexList.getFirst() > 0) {
            CAux = AUX.get(indexList.getFirst() - 1);
            tmp.set(Lfunc(CAux.modPow(lambda, o.pow(2)), o)
                    .multiply(mu).mod(o));
            hints.add(pp.g2.powZn(tmp).getImmutable());
        }

        if (indexList.getLast() < AUX.size() - 1) {
            CAux = AUX.getLast().multiply(AUX.get(indexList.getLast()).modInverse(o.pow(2))).mod(o.pow(2));
            tmp.set(Lfunc(CAux.modPow(lambda, o.pow(2)), o)
                    .multiply(mu).mod(o));
            hints.add(pp.g2.powZn(tmp).getImmutable());
        }

        for (int i = 0; i < indexList.size() - 1; i++) {
            CAux = AUX.get(indexList.get(i + 1) - 1).multiply(AUX.get(indexList.get(i)).modInverse(o.pow(2))).mod(o.pow(2));
            if (!CAux.equals(BigInteger.ONE)) {
                tmp.set(Lfunc(CAux.modPow(lambda, o.pow(2)), o)
                        .multiply(mu).mod(o));
                hints.add(pp.g2.powZn(tmp).getImmutable());
            }
        }


        // 计算Start End的hint
        CAux = AUX.get(Start - 1).multiply(AUX.getLast()).multiply(AUX.get(End).modInverse(o.pow(2))).mod(o.pow(2));
        Element hintSE = pp.pairing.getG1().newZeroElement();
        tmp.set(Lfunc(CAux.modPow(lambda, o.pow(2)), o)
                .multiply(mu).mod(o));
        hintSE.set(pp.g2.powZn(tmp));
        hintSE = hintSE.getImmutable();
        return new hint(hintSE, hints);
    }

    public static class hint {
        Element hintSE;
        List<Element> hints;
        public hint(Element hintSE, List<Element> hints) {
            this.hintSE = hintSE.getImmutable();
            this.hints = hints;
        }
    }
}
