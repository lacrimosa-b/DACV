import it.unisa.dia.gas.jpbc.Element;

import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.List;
public class SLocalVerify {
    public boolean SLocalVerifySequent(List<Element> SpkList, List<Element> mList, SeqSign.SeqSig Sig, Element hint,
                                Setup.Parameters.PublicParameters pp) {
        Element Sig1 = Sig.Sig1, Sig2 = Sig.Sig2;
        if (Sig1 == pp.pairing.getG1().newOneElement()) {
            return false;
        }
        Element C = pp.pairing.getG2().newZeroElement();
        C.set(pp.UpsilonHat);
        for (int i = 0; i < SpkList.size(); i++) {
            C.mul(SpkList.get(i).powZn(mList.get(i)));
        }
        return pp.pairing.pairing(Sig1, hint.mul(C)).equals(pp.pairing.pairing(Sig2, pp.g2));
    }

    public boolean SLocalVerifyDiscrete(List<Element> SpkList, List<Element> mList, SeqSign.SeqSig Sig, List<Element> hints,
                                   Setup.Parameters.PublicParameters pp) {
        Element Sig1 = Sig.Sig1, Sig2 = Sig.Sig2;
        if (Sig1 == pp.pairing.getG1().newOneElement()) {
            return false;
        }
        Element C = pp.pairing.getG2().newZeroElement();
        C.set(pp.UpsilonHat);
        for (int i = 0; i < SpkList.size(); i++) {
            C.mul(SpkList.get(i).powZn(mList.get(i)));
        }
        Element hint = pp.pairing.getG2().newOneElement();
        for (Element element : hints) {
            hint.mul(element);
        }
        return pp.pairing.pairing(Sig1, hint.mul(C)).equals(pp.pairing.pairing(Sig2, pp.g2));
    }
}
