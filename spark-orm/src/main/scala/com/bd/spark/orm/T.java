package com.bd.spark.orm;

import org.apache.hive.jdbc.HiveQueryResultSet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by ning on 2017/7/14.
 */
public class T {
    public static void main(String[] args) throws Exception {
   /*     HiveClient hc = new HiveClient();
        hc.executeQuery("select channelname,count(*) from tvlog.epg_wiki_info where dt='2017-01-01' group by channelname limit 2",rs -> {
            try {
                System.out.println(rs.getString(1));
                System.out.println(rs.getObject(2));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });*/

        Class.forName("org.apache.hive.jdbc.HiveDriver");
        Connection conn  = DriverManager.getConnection("jdbc:hive2://54.223.38.146:10000/default","hive","hive");
        String     sql   = "select count(*) from tvlog.epg_wiki_info";
        Statement  sment = conn.createStatement();
        ResultSet  rs    = sment.executeQuery(sql);
        while(rs.next()){
            HiveQueryResultSet hqrs = (HiveQueryResultSet)rs;
            System.out.println(hqrs.getString(1)+"\t");
        }
        conn.close();
    }
}
