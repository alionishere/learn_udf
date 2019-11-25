package com.soochow.udf;

import org.apache.hadoop.hive.ql.exec.UDF;

public class IsDateUdf extends UDF {
    public static boolean evaluate(String strDate) {
        if (strDate == null || "".equals(strDate.trim())) {
            return false;
        }
        return GetAgeUdf.isValidDate(strDate);
    }

    public static void main(String[] args) {
        System.out.println(evaluate("000105"));
        System.out.println(evaluate("190623"));
        System.out.println(evaluate("20190612"));
        System.out.println(evaluate("2019-06-12"));
    }
}
