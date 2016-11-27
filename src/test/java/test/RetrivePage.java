package test;

import org.apache.http.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RetrivePage {

    private static CloseableHttpClient httpclient = HttpClients.createDefault();       //����һ���ͻ���
    private static String filename = "d:"+File.separator+File.separator+"liudehua.html";      //�������html�ļ���·��
    private static String outfile = "d:"+File.separator+File.separator+"liudehua.csv";          //�������csv�ļ���·��
    private static boolean bfile = true;                 // ����������file��boolean����
    private static boolean bdb = true;                  // ����������file��boolean����
    private static ArrayList<String> datalist = new ArrayList<String>();            //����Arraylist�༯��������ÿһ�����ݵ���Ϣ
    private static String headtitle = "��Ӱ����,��ӳʱ��,���ݣ���Ա����������";               //��ӡ�ı���ͷ
    private static int countrs = 0;                         //��������

    /**
     * ����ҳ��
     */
    public static String downloadPage(String url) throws Exception {

        String htmlString = "";         //���巵�ص�String����
        HttpGet request = new HttpGet(url);             //������Դ

        CloseableHttpResponse response = httpclient.execute(request);           //�õ���Ӧ

        try {

            System.out.println(response.getStatusLine());               //��ӡ״̬��

            HttpEntity entity = response.getEntity();                   //���Entity����
            htmlString = EntityUtils.toString(entity);                  //��Entity����ת��Ϊ�ַ���
            htmlString = htmlString.replace("typeof jQuery182014058358692271755_1480220220217 === 'function' && jQuery182014058358692271755_1480220220217(", "");
            htmlString = htmlString.substring(5);
            htmlString= htmlString.substring(0, htmlString.length()-2);

            Object o = JSON.parse(htmlString);
            JSONArray ja = JSONArray.parseArray("["+htmlString+"]");
            ja.get(0);
            List<Map<String, Object>> listMap = JSON.parseObject("["+htmlString+"]", new TypeReference<List<Map<String,Object>>>(){});
            Map<String, Object> mp = listMap.get(0);
            Object ob =  mp.get("data");
            String s = ob.toString();
            List<Map<String, Object>> lMap = JSON.parseObject( mp.get("data").toString(), new TypeReference<List<Map<String,Object>>>(){});
             
              
            System.out.println(response.getStatusLine());      
            //  EntityUtils.consume(entity);                                //���ٶ���
        } finally {
            response.close();                                       
        }
        htmltoFile(htmlString);                         //����htmltoFile()�������ƶ�·�����html�ļ�

        return htmlString;

    }
    /**
     * ���html�ļ�
     */
    public static void htmltoFile(String htmlString) throws Exception {
        // ����ļ������
        FileOutputStream output = new FileOutputStream(filename);
        // ��utf-8�������ʽ������ļ���utf-8�����ı��룬ISO-8859-1��Ӣ�ı��룩
        output.write(htmlString.getBytes("utf-8"));
        if (output != null) {
            output.close();
        }
    }

    /**
     * ��ȡ���ж����Ӱ�б�
     * 
     * @throws Exception
     */
    public static void getDouBanList(String surl) throws Exception {

        String html = RetrivePage.downloadPage(surl);           //ͨ��url����ҳ��

        html = html.replace("star clearfix","star_clearfix");       //��"star_clearfix"���"star clearfix"

        Document doc = Jsoup.parse(html);                   // ������ȡDocument����
        Element divNode = doc.getElementsByClass("grid_view").first();          //ͨ��getElementsByClass������ȡclassΪ"grid_view"��div�ڵ����
        Elements liTag = divNode.select("li[class]");           //ͨ��selectѡ����ѡ����class���Ե�li��ǩ�ڵ㣬����ElementԪ�صļ���
        String title,time,director,actor,amount;            

        for (Element liNode : liTag) {              //����liTag Element�����е�ÿһ��Ԫ��liNode
            Element dd = liNode.select("dd").first();               //ȡ��liNode�ĵ�һ��dd�ڵ����
            title = dd.getElementsByTag("a").text();            //ʹ��getElementsByTag������ͨ����ǩ����ȡ��a��ǩ�ڵ����Ȼ��ȡ���е��ı�Ԫ�أ���Ϊ��Ӱ����
            datalist.clear();                           //���ÿһ������ǰ�����֮ǰ��ӵ����ݣ�������ѭ����ӣ�һ��Ҫ���ǰһ����ӵ����ݣ�
            datalist.add(title);                //��title(��Ӱ����)��ӽ�datalist����
            Element h6 = dd.select("h6").first();               //ѡ��dd�ڵ�����ĵ�һ��h6�ڵ����
            Element a = h6.select("span").first();              //��һ��ѡ��h6�ڵ����ĵ�һ��span�ڵ����
            time = a.text() ;                           //ȡ�õ�һ��span�ڵ������ı����ݣ�����ȡ��ʱ��
            time = time.replace("(","");                //��һ�������ı����ݣ�ȥ��������
            time = time.replace(")","");            //��һ�������ı����ݣ�ȥ��������
            datalist.add(time);                     //��time(��ӳʱ��)��ӽ�datalist����
            Element dl = dd.select("dl").first();           //ͨ��selectѡ����ѡ��dd�ڵ�ĵ�һ��dl�ڵ�
            Element d1 = dl.select("dd").first();           //ͨ��selectѡ����ѡ��dl�ڵ�ĵ�һ��dd�ڵ�
            if(d1!=null){                               //��Ϊ��Щ��Ӱ�������ݿ���Ϊ�գ�Ϊ��(null)ʱ������쳣��������������д�����nullת��Ϊ"";
                director = d1.text();               //��ȡd1���ı�����Ϊ����
            }else{
                director = "";
            }
            datalist.add(director);                 //��director(����)��ӽ�datalist����
            Element d2 = dl.select("dd").last();        //ͨ��selectѡ����ѡ��dl�ڵ�����һ��dd�ڵ�
            if(d2!=null){
                actor = d2.text();
            }else{
                actor = "";
            }
            datalist.add(actor);
            Element foot = liNode.getElementsByClass("star_clearfix").first();      ////ͨ��getElementsByClass������ȡclassΪ"star_clearfix"�Ľڵ����
            Element span = foot.select("span").last();              //ͨ��selectѡ����ѡ��foot�����һ��span����
            amount = span.text();                       //ȡ��span������ı�Ԫ�ؼ�Ϊ��������
            datalist.add(amount);
            outputRs();                 //����outputRs������datalist�����ÿһ�����ݲ��뵽���ݿ�
        }

    }       

    /**
     * ��������ݿ�
     * @throws Exception
     */
    private static void outputRs() throws Exception {

        String strout = "";
        for (int i = 0; i < datalist.size(); i++) {
            strout = strout + datalist.get(i) + ",";                //��ȡdatalist�����е�ÿһ�����ݣ�����һ���ַ���
        }

        if (bfile) {
            FileWriter fw = new FileWriter(outfile, true);          //ʵ�����ļ������
            PrintWriter out = new PrintWriter(fw);                  //ʵ������ӡ��
            if (countrs == 0)
                out.println(headtitle);                         //���ͷ����
            out.println(strout);                        //����ոմ�������strout�ַ���
            out.close();                                //�رմ�ӡ��
            fw.close();                             //�ر������
        }
        countrs = countrs + 1;
        System.out.println(countrs + "  " + strout);            //�������д�ӡ����

        // �������ݿ�
        if (bdb) {
            CrawlDatatoBase.InsertProduct(datalist);
        }
    }

    /**
     * ��ҳ��ȡ
     * 
     * @throws Exception
     */
    public static void skipPage(String surl) throws Exception{
        String html = RetrivePage.downloadPage(surl);           
        Document doc = Jsoup.parse(html);           
        Element footDiv = doc.getElementsByClass("paginator").first();          //��ȡҳ�벿�ֵ�div����
        Element footSpan = footDiv.getElementsByClass("next").first();   //��ȡclassΪ"next"�Ľڵ������footSpan��ʾ
        Element footA = footSpan.select("a[href]").first();         //ѡ��footSpan�е�һ������href���Ե�a�ڵ���󣬲���footA��ʾ
        String href = footA.attr("href");                       //���footA��href�����е�����href
        String http = "http://movie.douban.com/celebrity/1054424/movies"+href;      //��"http://movie.douban.com/celebrity/1054424/movies"��hrefƴ�Ӽ�Ϊ��һҳ���url
        Element thispage = doc.getElementsByClass("thispage").first();      //��ȡ��ǰҳ��ڵ�
        int end = Integer.parseInt(thispage.text());        //��ȡ��ǰҳ���е�����Ԫ�أ�String���ͣ�����ת��Ϊint����
        if(end==1){
            getDouBanList(surl);
            System.out.println("=========================="+1+"===================");
        }
        getDouBanList(http);                    //��ȡ��һҳ��
        System.out.println("=========================="+(end+1)+"===================");   //��ӡһ��ҳ��ָ���
        if(end<19){
            skipPage(http);                     //����һ����19ҳ������endС��19��ʱ��ѭ����ȡ
        }else{
            System.out.println("ҳ��������");            
        }
    }

    /**
     * ���Դ���
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {

        String strURL = "https://jobsearch-api.cloud.seek.com.au/search?&callback=jQuery182014058358692271755_1480220220217&keywords=&companyProfileStructuredDataId=&hirerId=&hirerGroup=&page=1&classification=6281&subclassification=&graduateSearch=false&displaySuburb=&suburb=&where=&whereId=&location=&nation=3001&area=&isAreaUnspecified=false&worktype=&salaryRange=0-999999&salaryType=annual&dateRange=999&sortMode=ListedDate&engineConfig=&usersessionid=opl0nb4zh1uswqg5r5bbk1vw&userid=&eventCaptureSessionId=280051be-cb0d-459d-b748-b940f8cf7449&userqueryid=183443514209146617&include=expanded&siteKey=NZ-Main&seekSelectAllPages=true&hadPremiumListings=true&_=1480220220566";
        
    //    Document doc1 = Jsoup.connect("https://jobsearch-api.cloud.seek.com.au/search?&callback=jQuery182014058358692271755_1480220220217&keywords=&companyProfileStructuredDataId=&hirerId=&hirerGroup=&page=1&classification=6281&subclassification=&graduateSearch=false&displaySuburb=&suburb=&where=&whereId=&location=&nation=3001&area=&isAreaUnspecified=false&worktype=&salaryRange=0-999999&salaryType=annual&dateRange=999&sortMode=ListedDate&engineConfig=&usersessionid=opl0nb4zh1uswqg5r5bbk1vw&userid=&eventCaptureSessionId=280051be-cb0d-459d-b748-b940f8cf7449&userqueryid=183443514209146617&include=expanded&siteKey=NZ-Main&seekSelectAllPages=true&hadPremiumListings=true&_=1480220220566").get(); 
      //  String title = doc1.title(); 
       /* https://jobsearch-api.cloud.seek.com.au/search?&callback=jQuery182014058358692271755_1480220220217
        	&keywords=&companyProfileStructuredDataId=&hirerId=&hirerGroup=&
        	page=1&classification=6281&subclassification=&graduateSearch=false&
        	displaySuburb=&suburb=&where=&whereId=&location=&nation=3001&area=&isAreaUnspecified=false
        	&worktype=&salaryRange=0-999999&salaryType=annual&dateRange=999
        	&sortMode=ListedDate&engineConfig=&usersessionid=opl0nb4zh1uswqg5r5bbk1vw
        	&userid=&eventCaptureSessionId=280051be-cb0d-459d-b748-b940f8cf7449&userqueryid=183443514209146617&
        	include=expanded&siteKey=NZ-Main&seekSelectAllPages=true&hadPremiumListings=true&_=1480220220566
        	*/
        	
       // Elements links = doc.select("a[href]"); //����href���Ե�aԪ��
        String html = RetrivePage.downloadPage(strURL);           
        Document doc = Jsoup.parse(html);           
        Element footDiv = doc.getElementsByClass("paginator").first();          //��ȡҳ�벿�ֵ�div����
        Element footSpan = footDiv.getElementsByClass("next").first();   //��ȡclassΪ"next"�Ľڵ������footSpan��ʾ
        Element footA = footSpan.select("a[href]").first();         //ѡ��footSpan�е�һ������href���Ե�a�ڵ���󣬲���footA��ʾ
        String href = footA.attr("href");                       //���footA��href�����е�����href
        String http = "http://movie.douban.com/celebrity/1054424/movies"+href;      //��"http://movie.douban.com/celebrity/1054424/movies"��hrefƴ�Ӽ�Ϊ��һҳ���url
        Element thispage = doc.getElementsByClass("thispage").first();      //��ȡ��ǰҳ��ڵ�
        int end = Integer.parseInt(thispage.text());        //��ȡ��ǰҳ���е�����Ԫ�أ�String���ͣ�����ת��Ϊint����
        if(end==1){
            getDouBanList(strURL);
            System.out.println("=========================="+1+"===================");
        }
        getDouBanList(http);                    //��ȡ��һҳ��
        System.out.println("=========================="+(end+1)+"===================");   //��ӡһ��ҳ��ָ���
        if(end<19){
            skipPage(http);                     //����һ����19ҳ������endС��19��ʱ��ѭ����ȡ
        }else{
            System.out.println("ҳ��������");            
        }
        
        
         try{
            CrawlDatatoBase.setConn();      
            skipPage(strURL);           //��ҳ��ȡ
            CrawlDatatoBase.closeConn();        //�ر����ݿ�
        }catch(Exception e){
            e.printStackTrace();
        } 

    }

}