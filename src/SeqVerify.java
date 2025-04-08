import it.unisa.dia.gas.jpbc.Element;

import java.util.Arrays;

public class SeqVerify {
    public boolean SeqVerify(Element[] SpkList, Element[] mList, SeqSign.SeqSig Sig, Setup.Parameters.PublicParameters pp, int d) {
        if (Sig.Sig1.equals(pp.pairing.getG1().newOneElement())) {
            return false;
        }
        Element C = pp.pairing.getG2().newZeroElement();
        C.set(pp.UpsilonHat);
        for (int i = 0; i < d; i++) {
            C = C.mul(SpkList[i].powZn(mList[i]));
        }
        return pp.pairing.pairing(Sig.Sig1, C).equals(pp.pairing.pairing(Sig.Sig2, pp.g2));
    }
}
