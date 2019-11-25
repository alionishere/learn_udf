package com.soochow.udf;
import org.apache.hadoop.hive.ql.exec.UDF;

public class TransferUdf extends UDF {
    private static final int BASE_NUM = 2;
    public static String evaluate(String input, String... params) throws Exception {
        if (input == null || "".equals(input.trim())) {
            return "0";
        }
        int i = Integer.valueOf(input);
        String separator;
        if (i < 0) {
            throw new Exception("Parameter num error! First para must be bigger than zero!");
        } else if (i == 0) {
            return "0";
        }
        if (params == null || params.length == 0) {
            separator = ",";
        } else if (params.length == 1) {
            separator = params[0];
        } else {
            throw new Exception("Parameter num error! Can be [(int)|(int, String)]");
        }
        String binStr = Long.toBinaryString(i);
        StringBuilder sb = new StringBuilder();
        int len = binStr.length();
        for (int j = 0; j < len; j++) {
            int c = binStr.charAt(j) - '0';
            if (c != 0) {
                sb.append(c * power(BASE_NUM, len - j -1)).append(separator);
            }
        }
        return sb.delete(sb.length() - separator.length(), sb.length()).toString();
    }

    public static int power(int i, int j) {
        int y;
        if (j == 0) {
            y = 1;
        } else {
            y = power(i, j / 2);
            y = y * y;
            if (j % 2 != 0) {
                y = i * y;
            }
        }
        return y;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(evaluate(null));
    }
}
