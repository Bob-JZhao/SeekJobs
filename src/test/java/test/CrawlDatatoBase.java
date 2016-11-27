package test;

 
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

public class CrawlDatatoBase {
    static Connection conn;
    static Statement st;
    /**
     * �����ݲ������ݿ�
     */
    public static boolean InsertProduct(ArrayList<String> datalist){
        try{
        Date now = new Date();          //��ȡ��ǰ����
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");       //ͨ��ָ����ʽʵ��������ת��Ϊ�ַ���ģ��
        String redate = dateFormat.format(now);         //��ģ�彫Date�������ڸ�ʽ�����ַ���
        datalist.add(redate);       //�����ڼ���datalist����
        String insql = "INSERT INTO doubanlist(title,time,director,actor,amount,redate) VALUES(?,?,?,?,?,?)";  //���彫ִ�в��������insql���
        PreparedStatement ps = conn.prepareStatement(insql);        //ʵ����PreparedStatement����Ԥ����insql���
        int i;
        for(i=0;i<datalist.size();i++){
            String strvalue = datalist.get(i);          //��ȡdatalist�����е�ÿһ������
            ps.setString(i+1, strvalue);            //ѭ��ȡ��datalist�е����ݲ����ý�VALUES�еģ�����
        }
        int result = ps.executeUpdate();            //ִ��insql��䣬���ɹ����򷵻�һ�����������򷵻�0
        ps.close();                     //�ر�PreparedStatement����
        if(result>0){           //result����0˵����������ɹ�
            return true;
        }
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public static void setConn(){       //����getConnection�����������ݿ⣬���Ӱ�ȫ��
        conn = getConnection();
    }
    public static void closeConn(){         //�ر����ݿ�����
        try{
            conn.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static Connection getConnection(){
        Connection con = null;
        String DBDRIVER = "com.mysql.jdbc.Driver";        //����������
        String DBUSER = "root";             //�����û���
        String DBPASS = "zhao1987";            //�������ݿ�����
        String DBURL = "jdbc:mysql://localhost:3306/seek";     //����url
        try{
            Class.forName(DBDRIVER);        //�������ݿ�������
            con = DriverManager.getConnection(DBURL,DBUSER,DBPASS);     //�������ݿ�
        }catch(Exception e){
            System.out.println("���ݿ�����ʧ��"+e.getMessage());       //�������ʧ�ܣ���ȡʧ�ܵ���Ϣ

        }
        return con;
    }
    
    public static void main(String[] args){
    	getConnection();
    	
    }
}