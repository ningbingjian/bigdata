package com.bd.spark.orm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * hive 工具类
 * @author yxy
 *
 */

public class HiveClient {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    private String driverName = "org.apache.hive.jdbc.HiveDriver";
    private String url = "jdbc:hive2://54.223.223.245:10000/default";
    private String user = "hive";
    private String password = "hive";
    private String sql = "";
    private String ip = "54.223.38.146";
    private String port = "10000";

    public ResultSet countData(Statement stmt, String tableName)  {
        sql = "select count(1) from " + tableName;
        System.out.println("Running:" + sql);
        ResultSet res=null;
        try {
            res = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public ResultSet selectData(Statement stmt, String tableName)  {
        sql = "select * from " + tableName;
        System.out.println("Running:" + sql);
        ResultSet res=null;
        try {
            res = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public boolean loadData(Statement stmt, String tableName,String filepath) {
        sql = "load data local inpath '" + filepath + "' into table " + tableName;
        boolean result=false;
        try {
            stmt.execute(sql);
            result = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ResultSet describeTables(Statement stmt, String tableName)   {
        sql = "describe " + tableName;
        ResultSet res=null;
        try {
            res = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public ResultSet showTables(Statement stmt, String tableName)  {
        if(tableName==null||tableName.equals(null)){
            sql = "show tables";
        }else{
            sql = "show tables '" + tableName + "'";
        }
        ResultSet res=null;
        try {
            res = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public boolean createTable(Statement stmt, String sql)  {
//        sql = "create table " + tableName + " (key int, value string)  row format delimited fields terminated by '\t'";
        boolean result=false;
        try {
            stmt.execute(sql);
            result = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean dropTable(Statement stmt,String tableName) {
        // 创建的表名
        //String tableName = "testHive";
        sql = "drop table  " + tableName;
        boolean result=false;
        try {
            stmt.execute(sql);
            result = true;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }
    public boolean insertSelect(Statement stmt, String fromTable, String toTable, String dt){
        sql = "insert overwrite table "+toTable+" partition (dt='"+dt+"') select user_id,item_id,action,start_time,end_time,details from " + fromTable;
        boolean result = false;
        try {
            stmt.execute(sql);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public Connection getConn(){
        Connection conn = null;
        try {
            Class.forName(driverName);
            conn = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
    public Connection getConn(String ds){
        Connection conn = null;
        try {
            Class.forName(driverName);
            String url = "jdbc:hive2://" + ip + ":" + port + "/" + ds;
            conn = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public void close(Connection conn,Statement stmt){
          try {
            if (stmt != null) {
              stmt.close();
              stmt = null;
            }
            if (conn != null) {  
                  conn.close();  
                  conn = null;  
              }  

        } catch (SQLException e) {
            e.printStackTrace();
        }  
    }
    public boolean executeUpdate(String sql){
        Statement stmt = null;
        Connection conn = null;
        try{
            conn = getConn();
            stmt = conn.createStatement();
            boolean r = stmt.execute(sql);
            return r;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            close(conn,stmt);
        }
        return false;
    }
    public boolean executeUpdate(String database,String sql){
        Statement stmt = null;
        Connection conn = null;
        try{
            conn = getConn(database);
            stmt = conn.createStatement();
            boolean r = stmt.execute(sql);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            close(conn,stmt);
        }
        return false;

    }
    public <R> List<R> executeQuery(String sql, Function<ResultSet,R> rowMapper){
        return this.executeQuery("default",sql,rowMapper);
    }
    public <R> List<R> executeQuery(String database, String sql, Function<ResultSet,R> rowMapper){
        Statement stmt = null;
        Connection conn = null;
        ResultSet rs = null ;
        List<R> rsList = new ArrayList<R>();
        try{
            conn = getConn(database);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while(rs.next()){
                R r = rowMapper.apply(rs);
                rsList.add(r);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            close(conn,stmt);
        }
        return rsList;
    }
}
