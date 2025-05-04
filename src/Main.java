import it.unisa.dia.gas.jpbc.Element;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        Setup Setup = new Setup();
        Setup.Parameters Parameters = Setup.generateParameters();
        Setup.Parameters.PublicParameters pp = Parameters.pp;
        int num = 10;
        DKGen DKGen = new DKGen();
        DKGen.DK[] DKList = new DKGen.DK[num];

        Element[] mList = new Element[num];
        Element[] SpkList = new Element[num];
        List<BigInteger> AUX = new ArrayList<>();
        SeqSign SeqSign = new SeqSign();
        SeqSign.SeqSig Sig = new SeqSign.SeqSig(pp.pairing.getZr().newZeroElement(), pp.pairing.getZr().newZeroElement(), BigInteger.ZERO);
        for (int i = 0; i < num; i++) {
            DKList[i] = DKGen.generateDK(pp, num);
            Element m = pp.pairing.getZr().newRandomElement().getImmutable();
            Sig = SeqSign.SeqSign(DKList[i].Ssk, DKList[i].Spk, Parameters.esk, Parameters.epk, AUX, Sig, mList, SpkList, m, pp, i);
            mList[i] = m.getImmutable();
            SpkList[i] = DKList[i].Spk.getImmutable();
        }
        System.out.println(new SeqVerify().SeqVerify(SpkList, mList, Sig, pp, num));

        SLocalAggOpen SLocalAggOpen = new SLocalAggOpen();
        List<Integer> indexList = Arrays.asList(1, 2, 3, 4, 6, 7);
        SLocalAggOpen.hint hint = SLocalAggOpen.SLocalAggOpen(AUX, Parameters.esk, Parameters.epk, pp, 3, num - 2, indexList);
        List<Element> SpkVerify = new ArrayList<>();
        List<Element> mVerify = new ArrayList<>();
        for (int i = 3; i < num - 1; i++) {
            SpkVerify.add(SpkList[i].getImmutable());
            mVerify.add(mList[i].getImmutable());
        }
        System.out.println(new SLocalVerify().SLocalVerifySequent(SpkVerify, mVerify, Sig, hint.hintSE, pp));
        SpkVerify.clear();
        mVerify.clear();
        for (Integer i : indexList) {
            SpkVerify.add(SpkList[i].getImmutable());
            mVerify.add(mList[i].getImmutable());
        }
        System.out.println(new SLocalVerify().SLocalVerifyDiscrete(SpkVerify, mVerify, Sig, hint.hints, pp));

        List<String> Messages = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            Messages.add(generateRandomString());
        }
        DKGen.DK DK = DKGen.generateDK(pp, 1);
        List<BigInteger> signatures = new ArrayList<>();
        for (String msg : Messages) {
            BigInteger sigma = HSign.Hsign(pp, DK, msg);
            signatures.add(sigma);
        }

        for (int i = 0; i < num; i++) {
            boolean valid = HVerify.Hverify(pp, DK, signatures.get(i), Messages.get(i));
            System.out.println("Signature " + (i+1) + ": " + valid);
        }

        BigInteger aggSig = HAgg.HAggregate(DK, signatures);
        boolean aggValid = HAggVerify.HAggverify(pp, DK, Messages, aggSig);
        System.out.println("Aggregate valid: " + aggValid);

        List<String> subMessages = Arrays.asList(Messages.get(1), Messages.get(3), Messages.get(5));
        BigInteger Hhint = HLocalAggOpen.HLocalAggopen(pp, DK, subMessages, Messages, aggSig);
        boolean LocalaggValid = HLocalVerify.HLocalverify(pp, DK, subMessages, Hhint);
        System.out.println("LocalAggregate valid: " + LocalaggValid);
    }


    private static String generateRandomString() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8);
    }
}
