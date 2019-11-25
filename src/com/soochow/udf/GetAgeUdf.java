package com.soochow.udf;

import org.apache.hadoop.hive.ql.exec.UDF;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class GetAgeUdf extends UDF {

    public static SimpleDateFormat getFormat(String str) {
        SimpleDateFormat format = null;
        if (str.length() == 8) {
            return new SimpleDateFormat("yyyyMMdd");
        } else {
            return new SimpleDateFormat("yyyyMM");
        }
    }

    public static boolean isValidDate(String strDate) {
        // strDate = strDate.replace("-", "");
        if (!isInteger(strDate)) {
            return false;
        }

        SimpleDateFormat format = getFormat(strDate);
        try {
            format.setLenient(false);
            Date date = format.parse(strDate);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[\\d]*$");
        return pattern.matcher(str).matches();
    }

    public static int getAge(String str) throws Exception {
        SimpleDateFormat format = getFormat(str);
        Date birthday = null;
        try {
            birthday = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        if (birthday == null) {
            throw new Exception("Birthday is null, which can not be parsed!");
        }
        int yearNow = cal.get(Calendar.YEAR);  //当前年份
        int monthNow = cal.get(Calendar.MONTH);  //当前月份
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH); //当前日期
        cal.setTime(birthday);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
        int age = yearNow - yearBirth;   //计算整岁数
        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) age--;
            } else {
                age--;//当前月份在生日之前，年龄减一
            }
        }
        return age;
    }

    public static String rectifyAgeRange(int age) {
        if (age < 0 || age > 200) {
            return "0";
        } else return String.valueOf(age);
    }

    public static boolean isIdentityCard(String idCard) {
        if (idCard == null || "".equals(idCard.trim())) {
            return false;
        } else if (idCard.length() == 15 && isInteger(idCard)) {
            return true;
        } else if (idCard.length() == 18) {
            if (isInteger(idCard.substring(0, idCard.length() - 1)) &&
                    (idCard.endsWith("x") || idCard.endsWith("X"))) {
                return true;
            } else return isInteger(idCard);
        } else return false;
    }

    public static String evaluate(String birthday, String... params) throws Exception {
        int age = Integer.MAX_VALUE;
        if (params != null && params.length > 1) {
            throw new Exception("Parameter error. Should be (String) or (String, String)");
        }
        if (birthday != null && birthday.length() == 8 && isValidDate(birthday))  {
            age = getAge(birthday);
        }

        if (params != null && params.length == 1 && (age < 0 || age > 200)) {
            if (! isIdentityCard(params[0])) {
                return "0";
            }
            // 循环判断params每个值是否满足条件，
            // 满足则返回，否则继续下一个
            if (params[0].length() == 18) {
                age = getAge(params[0].substring(6, 14));
            } else if (params[0].length() == 15) {
                age = getAge("19" + params[0].substring(6, 12));
            }
            return rectifyAgeRange(age);
        } else {
            return rectifyAgeRange(age);
        }
    }

    public static String evaluate1(String idCard, String...params) throws Exception {
        if (params != null && params.length > 1) {
            throw new Exception("Parameter error. Should be (String) or (String, String)");
        }
        int age = Integer.MAX_VALUE;
        IdCardValidator iv = new IdCardValidator();
        if (idCard != null && "".equals(idCard.trim()) && iv.isValidatedAllIdcard(idCard)) {
            if (idCard.length() == 18) {
                age = getAge(idCard.substring(6, 14));
            } else if (idCard.length() == 15) {
                age = getAge("19" + idCard.substring(6, 12));
            }
        }

        if ((age < 0 || age > 200)
                && params != null
                && params.length == 1
                && params[0] != null
                && params[0].length() == 8
                && isValidDate(params[0]))  {
            age = getAge(params[0]);
        }
        return rectifyAgeRange(age);
    }

    public static void main(String[] args) throws Exception {
        System.out.println(evaluate(null));
        System.out.println(evaluate(null, (String) null));
        System.out.println(evaluate("", (String) null));
//        System.out.println(evaluate("142431199001145", "20011010"));
//        System.out.println(evaluate("332102198106200339", "20021010"));
        System.out.println(evaluate(null, "33210219810620033x"));
        System.out.println(evaluate("20021010", "33210219810620033X"));
//        System.out.println(evaluate("x32102198106200339", "20021010"));
    }
}
