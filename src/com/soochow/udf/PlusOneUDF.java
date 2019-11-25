package com.soochow.udf;

import org.apache.hadoop.hive.ql.exec.UDF;

public class PlusOneUDF extends UDF {
    public int evaluate(int i) {
        return i + 1;
    }
}
