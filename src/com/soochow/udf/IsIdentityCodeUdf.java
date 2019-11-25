package com.soochow.udf;

import org.apache.hadoop.hive.ql.exec.UDF;

public class IsIdentityCodeUdf extends UDF {
    public static boolean evaluate(String idCode) {
        if (idCode == null || "".equals(idCode.trim())) return false;
        return IdCodeVerifier.isValidateIdentityCode(idCode);
    }


    public static void main(String[] args) {
        System.out.println(evaluate("410926199611183214"));
        System.out.println(evaluate("522634520829128"));
        System.out.println(evaluate("111111111111111111"));
    }
}
