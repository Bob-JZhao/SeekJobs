package test;

 
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

public class CrawlDatatoBase {
    static Connection conn;
    static Statement st;
    /**
     * 将数据插入数据库
     */
    public static boolean InsertProduct(ArrayList<String> datalist){
        try{
        Date now = new Date();          //获取当前日期
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");       //通过指定格式实例化日期转化为字符串模板
        String redate = dateFormat.format(now);         //用模板将Date类型日期格式化成字符串
        datalist.add(redate);       //将日期加入datalist集合
        String insql = "INSERT INTO doubanlist(title,time,director,actor,amount,redate) VALUES(?,?,?,?,?,?)";  //定义将执行插入操作的insql语句
        PreparedStatement ps = conn.prepareStatement(insql);        //实例化PreparedStatement对象，预处理insql语句
        int i;
        for(i=0;i<datalist.size();i++){
            String strvalue = datalist.get(i);          //获取datalist集合中的每一条数据
            ps.setString(i+1, strvalue);            //循环取得datalist中的数据并设置进VALUES中的？里面
        }
        int result = ps.executeUpdate();            //执行insql语句，若成功，则返回一个正数，否则返回0
        ps.close();                     //关闭PreparedStatement对象
        if(result>0){           //result大于0说明插入操作成功
            return true;
        }
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public static void setConn(){       //调用getConnection方法连接数据库，增加安全性
        conn = getConnection();
    }
    public static void closeConn(){         //关闭数据库连接
        try{
            conn.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static Connection getConnection(){
        Connection con = null;
        String DBDRIVER = "com.mysql.jdbc.Driver";        //定义驱动类
        String DBUSER = "root";             //定义用户名
        String DBPASS = "zhao1987";            //定义数据库密码
        String DBURL = "jdbc:mysql://localhost:3306/seek";     //定义url
        try{
            Class.forName(DBDRIVER);        //加载数据库驱动类
            con = DriverManager.getConnection(DBURL,DBUSER,DBPASS);     //连接数据库
        }catch(Exception e){
            System.out.println("数据库连接失败"+e.getMessage());       //如果连接失败，获取失败的信息

        }
        return con;
    }
    
    public static void main(String[] args){
    	getConnection();
    	
    }
}