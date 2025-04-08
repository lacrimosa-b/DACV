//VKGen.java
import it.unisa.dia.gas.jpbc.Element;

import java.util.ArrayList;
import java.util.List;

public class VKGen {
    public VK generateVK(Setup.Parameters.PublicParameters pp, int l) {
        Element h = pp.pairing.getG2().newRandomElement();
        Element a = pp.pairing.getZr().newRandomElement();
        Element[] b = new Element[l];
        Element A = h.powZn(a);
        Element[] B = new Element[l];
        for (int i = 0; i < l; i++) {
            b[i] = pp.pairing.getZr().newRandomElement();
            B[i] = h.powZn(b[i]);
        }
        List<Element> Vsk = new ArrayList<>();
        Vsk.add(a);
        Vsk.addAll(List.of(b));
        List<Element> Vpk = new ArrayList<>();
        Vpk.add(A);
        Vpk.addAll(List.of(B));
        return new VK(Vsk, Vpk);
    }
    public static class VK{
        List<Element> Vsk, Vpk;
        public VK(List<Element> Vsk, List<Element> Vpk) {
            this.Vsk = Vsk;
            this.Vpk = Vpk;
        }
    }
}
