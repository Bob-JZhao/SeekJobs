package com.bob.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.bob.model.jobBean;

public class DataBaseTools {
    static Connection conn;
    static Statement st;
	 public static boolean Insert(ArrayList<jobBean> datalist){
	        try{
	       
	        String insql = "insert into jobs (jobid,title,info,advertiser,location,area,teaser) values (?,?,?,?,?,?,?)";

	        PreparedStatement ps = conn.prepareStatement(insql);        // PreparedStatement 
	      
	        for( jobBean job:datalist){
	                
	            ps.setInt( 1, job.getId());            
	            ps.setString(2, job.getTitle());
	            ps.setString(3, "info");
	            ps.setString(4, job.getAdvertiser().getId());
	            ps.setString(5, job.getLocation());
	            ps.setString(6, job.getArea());
	            ps.setString(7, job.getTeaser());
	            ps.executeUpdate();   
	        }
	                
	        ps.close();                      
	        
	        }catch(Exception e){
	            e.printStackTrace();
	        }
	        return true;
	    }
	 public static void setConn(){       
	        conn = getConnection();
	    }
	 public static Connection getConnection(){
	        Connection con = null;
	        String DBDRIVER = "com.mysql.cj.jdbc.Driver";        //driver
	        String DBUSER = "root";             // username
	        String DBPASS = "XXXXX";            // password
	        String DBURL = "jdbc:mysql://localhost:3306/seek_job?serverTimezone=UTC ";     //定义url
	        try{
	            Class.forName(DBDRIVER);        // 
	            con = DriverManager.getConnection(DBURL,DBUSER,DBPASS);     //连接数据库
	        }catch(Exception e){
	            System.out.println("connect fail "+e.getMessage());       // 

	        }
	        return con;
	    }
	 
	 public static void closeConn(){         //close
	        try{
	            conn.close();
	        }catch(Exception e){
	            e.printStackTrace();
	        }
	    }
}
