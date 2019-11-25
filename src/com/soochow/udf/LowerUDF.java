package com.soochow.udf;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

public final class LowerUDF extends UDF {
    public Text evaluate(final Text s) {
        if (s == null) { return null; }
        return new Text(s.toString().toLowerCase());
    }

    public static void main(String[] args) {
        System.out.println(new LowerUDF().evaluate(new Text("ABC")));
    }
}
