//DKGen.java
import it.unisa.dia.gas.jpbc.Element;

import java.util.Arrays;
import java.util.List;

public class DKGen {
    public DK generateDK(Setup.Parameters.PublicParameters pp, int l) {
        //generate S
        Element w = pp.pairing.getZr().newRandomElement().getImmutable();
        Element W = pp.g2.powZn(w).getImmutable();


        //generate H
        Element eta = pp.pairing.getZr().newRandomElement().getImmutable();
        Element gamma = pp.pairing.getZr().newRandomElement().getImmutable();
        Element lota = pp.pairing.getZr().newRandomElement().getImmutable();
        Element N = eta.mulZn(gamma).getImmutable();
        Element xi = Primeseq(pp); //PrimeSeq unfinished
        List<Element> Hsk = Arrays.asList(eta, gamma, xi, lota);
        List<Element> Hpk = Arrays.asList(N, xi, lota);

        //generate T
        Element[] Tsk = new Element[l];
        Element[] Tpk = new Element[l];
        for (int i = 0; i < l; i++) {
            Tsk[i] = pp.pairing.getZr().newRandomElement();
            Tpk[i] = pp.g2.powZn(Tsk[i]);

        }

        return new DK(w, W, Hsk, Hpk, Tsk, Tpk);
    }

    private Element Primeseq(Setup.Parameters.PublicParameters pp) {
        return pp.pairing.getZr().newRandomElement(); //unfinished
    }

    public static class DK {
        public Element Ssk, Spk;
        public List<Element> Hpk, Hsk;
        public Element[] Tsk, Tpk;

        public DK(Element Ssk, Element Spk, List<Element> Hsk, List<Element> Hpk, Element[] Tsk, Element[] Tpk) {
            this.Ssk = Ssk;
            this.Spk = Spk;
            this.Hsk = Hsk;
            this.Hpk = Hpk;
            this.Tsk = Tsk;
            this.Tpk = Tpk;
        }
    }
}
