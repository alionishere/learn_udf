import com.soochow.udf.LowerUDF;
import com.soochow.udf.PlusOneUDF;
import com.soochow.udf.TransferUdf;
import org.apache.hadoop.io.Text;
import org.junit.Test;

import java.util.ArrayList;

//import static com.sun.org.apache.xalan.internal.lib.ExsltMath.power;

public class TestDemo {
    @Test
    public void test () throws Exception {
        TransferUdf transferUdf = new TransferUdf();
        System.out.println(transferUdf.power(3, 5));
//        System.out.println(transferUdf.evaluate(18, ","));
    }

//    @Test
//    public void test_power() {
//        System.out.println(power(3, 5));
//    }

    @Test
    public void test_lowerudf() {
        LowerUDF lowerUDF = new LowerUDF();
        System.out.println(lowerUDF.evaluate(new Text("ABC")));

    }

    @Test
    public void test_power() {
        int i = 2;
        int j = 3;
        int r = 1;
        if(j == 0) {
            System.out.println(1);
        } else {
            for (int k = 0; k < j; k++) {
                r = r * i;
            }
            System.out.println(r);
        }
    }

    @Test
    public void test_plus() {
        PlusOneUDF plusOneUDF = new PlusOneUDF();
        System.out.println(plusOneUDF.evaluate(3));
    }

    @Test
    public void test_str() {
//        if ("    ".trim().equals("")) {
//            System.out.println("ok");
//        }
//
//        ArrayList<Object> strs = new ArrayList<>();
//        System.out.println(strs.size());
//        strs.add(null);
//        System.out.println(strs.size());
//        strs.add("");
//        System.out.println(strs.size());
//
//        System.out.println("adfgX".endsWith("x|X"));
//        System.out.println("中".length());
//        String str = "我是l中国人我爱我的dabed";
//        System.out.println(str.substring(6,13));
//        System.out.println(str.length());
//        System.out.println(isAllEqual("1111111"));
        System.out.println("1234567890".substring(0,3));
    }

    public boolean isAllEqual(String str) {
        for (int i = 0; i < str.length() - 1; i++) {
            if (str.charAt(i) != str.charAt(i + 1)) return false;
        }
        return true;
    }
}
