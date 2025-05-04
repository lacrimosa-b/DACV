import java.math.BigInteger;
import java.util.List;

public class HLocalVerify {
    public static boolean HLocalverify(Setup.Parameters.PublicParameters pp, DKGen.DK DK, List<String> subsetMessages, BigInteger hint) {
        return HAggVerify.HAggverify(pp, DK, subsetMessages, hint);
    }
}
