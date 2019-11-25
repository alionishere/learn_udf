package com.soochow.udf;

import org.apache.hadoop.hive.ql.exec.UDF;

import java.sql.*;

public class GetTradeDateUdf extends UDF {

    public static Connection dbConn(String name, String pass) {
        Connection c = null;
        try {
            c = DriverManager.getConnection("jdbc:oracle:thin:@172.22.131.28:1521:orcl", name, pass);
//            c = DriverManager.getConnection("jdbc:oracle:thin:@192.250.107.199:1521:sjgk", name, pass);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return c;
    }

    public static String getDBRes(String sql_stmt){
        ResultSet rs = null;
        Statement sql = null;
        String result = null;
        try (Connection con = dbConn("kettle", "kettle")) {
//        try (Connection con = dbConn("kettle", "kettle")) {
            if (con == null) {
                System.out.print("连接失败");
                System.exit(0);
            }
            sql = con.createStatement();
            rs = sql.executeQuery(sql_stmt);
            while (rs.next()) {
                result = rs.getString(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     *
     * @param date: String not null
     * @param interval: int (0 -> 1)
     * @return String (date)
     */
    public static String evaluate(String date, Integer... interval){
        Integer interval_ = 1;
        Integer txDateFlag = 3;
        String reTradingDay = null;
        if (date == null || date.length() == 0) {
            System.out.println("Origin date can not be null!");
            System.exit(0);
        }

        if (interval != null && interval.length != 0) {
            if(interval.length == 1) {
                if(interval[0] == 0) {
                    if(isTradingDay(date)){
                        return date;
                    }
                   return getDBRes(getSql(date, interval_, txDateFlag,interval[0]<0));
                }
                if(isTradingDay(date)) {
                    reTradingDay = getDBRes(getSql(date, Math.abs(interval[0]) + 1, txDateFlag,interval[0]<0));
                } else {
                    reTradingDay = getDBRes(getSql(date, Math.abs(interval[0]), txDateFlag,interval[0]<0));
                }
            } else if(interval.length == 2) {
                txDateFlag = interval[1];
                if(interval[0] == 0) {
                    if(isTradingDay(date)){
                        return date;
                    }
                    return getDBRes(getSql(date, interval_,txDateFlag,interval[0]<0));
                }
                if(isTradingDay(date)) {
                    reTradingDay = getDBRes(getSql(date, Math.abs(interval[0]) + 1, txDateFlag,interval[0]<0));
                } else {
                    reTradingDay = getDBRes(getSql(date, Math.abs(interval[0]), txDateFlag,interval[0]<0));
                }
            } else {
                System.out.println("Parameter number error!");
                System.exit(0);
            }
        } else {
            reTradingDay = getDBRes(getSql(date, interval_,txDateFlag,false));
        }

        return reTradingDay;
    }

    public static boolean isTradingDay(String date) {
        String judgeSql = " " +
                "SELECT JYRBS\n" +
                "   FROM kettle.JYR\n" +
                "  WHERE RQ = '%s'";
        String txDateFlag = getDBRes(String.format(judgeSql, date));
        return "3".equals(txDateFlag);
    }

    public static  String getSql(String date, Integer interval, Integer txDateFlag, boolean isDesc) {
        String desc = "";
        String sign = ">=";
        if(isDesc) {
            desc = "desc";
            sign = "<=";
        }
        String asc_sql = "" +
                "SELECT rq\n" +
                "  FROM (\n" +
                "SELECT ROW_NUMBER() OVER(ORDER BY rq %s) AS rn,\n" +
                "       rq\n" +
                "  FROM kettle.JYR\n" +
                " WHERE RQ %s '%s'\n" +
                "   AND JYRBS = %d" +
                "     ) t\n" +
                " WHERE rn = %d";
        return String.format(asc_sql, desc, sign, date, txDateFlag, interval);
    }

    public static void main(String[] args) {
//        System.out.println(evaluate("20190111", -1));
//        System.out.println(evaluate("20190110", 1));
//        System.out.println(evaluate("20190111", 2));
//        System.out.println(evaluate("20190109", 0));
//        System.out.println(evaluate("20190109"));
//        System.out.println(evaluate("20190111"));
    }

}
