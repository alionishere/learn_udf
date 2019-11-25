package com.soochow.udf;

import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

public class IdCodeVerifier {
    private static String[] cityCode = { "11", "12", "13", "14", "15", "21", "22",
            "23", "31", "32", "33", "34", "35", "36", "37", "41", "42", "43",
            "44", "45", "46", "50", "51", "52", "53", "54", "61", "62", "63",
            "64", "65", "71", "81", "82", "91" };

    private static boolean isDigit (String str) {
        Pattern pattern = Pattern.compile("^[\\d]*$");
        return pattern.matcher(str).matches();
    }

    private static SimpleDateFormat getFormat(String str) {
        if (str.length() == 8) {
            return new SimpleDateFormat("yyyyMMdd");
        } else {
            return new SimpleDateFormat("yyyyMM");
        }
    }

    private static boolean isDate(String strDate) {
        if (!isDigit(strDate)) {
            return false;
        }

        SimpleDateFormat format = getFormat(strDate);
        try {
            format.setLenient(false);
            format.parse(strDate);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private static boolean isNull(String str) {
        return str == null || "".equals(str.trim());
    }

    private static boolean isValidateCityCode(String provinceId) {
        for (String id : cityCode) {
            if (id.equals(provinceId)) return true;
        }
        return false;
    }

    private static int[] convertCharToInt(char[] c) throws NumberFormatException {
        int[] a = new int[c.length];
        int k = 0;
        for (char temp : c) {
            a[k++] = Integer.parseInt(String.valueOf(temp));
        }
        return a;
    }

    private static boolean isValidate15Code(String code15) {
        if (isAllEqual(code15)) return false;
        if (! isValidateCityCode(code15.substring(0, 2))) return false;
        return isDate("19" + code15.substring(6, 12));
    }

    public static boolean isAllEqual(String str) {
        for (int i = 0; i < str.length() - 1; i++) {
            if (str.charAt(i) != str.charAt(i + 1)) return false;
        }
        return true;
    }

    private static boolean isCheckCodeOK(String code18) {
        int[] factor = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
        String[] verifyCode = { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };
        int sum = 0;
        String code17 = code18.substring(0, 17);
        String checkCode = code18.substring(17).toUpperCase();
        int[] codes = convertCharToInt(code17.toCharArray());
        for (int i = 0; i < 17; i++) {
            sum += codes[i] * factor[i];
        }
        return checkCode.equals(verifyCode[sum % 11]);
    }

    private static boolean isValidate18Code(String code18) {
        if (isAllEqual(code18.substring(0, 17))) return false;
        if (! isValidateCityCode(code18.substring(0, 2))) return false;
        if (! isDate(code18.substring(6,14))) return false;
        return isCheckCodeOK(code18);
    }

    public static boolean isValidateIdentityCode(String code) {
        if (isNull(code)) return false;
        if (code.length() == 15 && isDigit(code)) {
            return isValidate15Code(code);
        } else if (code.length() == 18 && isDigit(code.substring(0, 17))) {
            return isValidate18Code(code);
        } else return false;
    }

    public static void main(String[] args) {
        System.out.println(IdCodeVerifier.isValidateIdentityCode("000000000918277"));
        System.out.println(IdCodeVerifier.isValidateIdentityCode("410926199611183214"));
        System.out.println(IdCodeVerifier.isValidateIdentityCode("410926199612183214"));
    }
}
