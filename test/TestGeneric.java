public class TestGeneric {
    public static <T> void test_gen(T t) {
        if (t instanceof String) {
            int x = Integer.parseInt((String) t);
            System.out.println(x);
            System.out.println(Integer.toBinaryString(x));
            System.out.println(t);
        } else {
            System.out.println(".....");
        }
    }

    public static void main(String[] args) {
//        test_gen("abc");
        test_gen("1234");
        test_gen(123);
    }
}
