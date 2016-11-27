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

    private static CloseableHttpClient httpclient = HttpClients.createDefault();       //创建一个客户端
    private static String filename = "d:"+File.separator+File.separator+"liudehua.html";      //定义输出html文件的路径
    private static String outfile = "d:"+File.separator+File.separator+"liudehua.csv";          //定义输出csv文件的路径
    private static boolean bfile = true;                 // 定义控制输出file的boolean变量
    private static boolean bdb = true;                  // 定义控制输出file的boolean变量
    private static ArrayList<String> datalist = new ArrayList<String>();            //定义Arraylist类集用来保存每一条数据的信息
    private static String headtitle = "电影名称,上映时间,导演，演员，评价人数";               //打印的标题头
    private static int countrs = 0;                         //计数变量

    /**
     * 下载页面
     */
    public static String downloadPage(String url) throws Exception {

        String htmlString = "";         //定义返回的String变量
        HttpGet request = new HttpGet(url);             //请求资源

        CloseableHttpResponse response = httpclient.execute(request);           //得到回应

        try {

            System.out.println(response.getStatusLine());               //打印状态码

            HttpEntity entity = response.getEntity();                   //获得Entity对象
            htmlString = EntityUtils.toString(entity);                  //将Entity对象转化为字符串
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
            //  EntityUtils.consume(entity);                                //销毁对象
        } finally {
            response.close();                                       
        }
        htmltoFile(htmlString);                         //调用htmltoFile()方法在制定路径输出html文件

        return htmlString;

    }
    /**
     * 输出html文件
     */
    public static void htmltoFile(String htmlString) throws Exception {
        // 获得文件输出流
        FileOutputStream output = new FileOutputStream(filename);
        // 以utf-8编码的形式输出到文件（utf-8是中文编码，ISO-8859-1是英文编码）
        output.write(htmlString.getBytes("utf-8"));
        if (output != null) {
            output.close();
        }
    }

    /**
     * 获取所有豆瓣电影列表
     * 
     * @throws Exception
     */
    public static void getDouBanList(String surl) throws Exception {

        String html = RetrivePage.downloadPage(surl);           //通过url下载页面

        html = html.replace("star clearfix","star_clearfix");       //用"star_clearfix"替代"star clearfix"

        Document doc = Jsoup.parse(html);                   // 解析获取Document对象
        Element divNode = doc.getElementsByClass("grid_view").first();          //通过getElementsByClass方法获取class为"grid_view"的div节点对象
        Elements liTag = divNode.select("li[class]");           //通过select选择器选择有class属性的li标签节点，返回Element元素的集合
        String title,time,director,actor,amount;            

        for (Element liNode : liTag) {              //对于liTag Element集合中的每一个元素liNode
            Element dd = liNode.select("dd").first();               //取得liNode的第一个dd节点对象
            title = dd.getElementsByTag("a").text();            //使用getElementsByTag方法，通过标签名称取得a标签节点对象，然后取其中的文本元素，即为电影名称
            datalist.clear();                           //添加每一条数据前先清空之前添加的内容（由于是循环添加，一定要清空前一次添加的内容）
            datalist.add(title);                //将title(电影名称)添加进datalist集合
            Element h6 = dd.select("h6").first();               //选择dd节点里面的第一个h6节点对象
            Element a = h6.select("span").first();              //进一步选择h6节点对象的第一个span节点对象
            time = a.text() ;                           //取得第一个span节点对象的文本内容，初步取出时间
            time = time.replace("(","");                //进一步处理文本内容，去掉左括号
            time = time.replace(")","");            //进一步处理文本内容，去掉右括号
            datalist.add(time);                     //将time(上映时间)添加进datalist集合
            Element dl = dd.select("dl").first();           //通过select选择器选择dd节点的第一个dl节点
            Element d1 = dl.select("dd").first();           //通过select选择器选择dl节点的第一个dd节点
            if(d1!=null){                               //因为有些电影导演数据可能为空，为空(null)时会出现异常，所以在这里进行处理，将null转化为"";
                director = d1.text();               //获取d1的文本对象即为导演
            }else{
                director = "";
            }
            datalist.add(director);                 //将director(导演)添加进datalist集合
            Element d2 = dl.select("dd").last();        //通过select选择器选择dl节点的最后一个dd节点
            if(d2!=null){
                actor = d2.text();
            }else{
                actor = "";
            }
            datalist.add(actor);
            Element foot = liNode.getElementsByClass("star_clearfix").first();      ////通过getElementsByClass方法获取class为"star_clearfix"的节点对象
            Element span = foot.select("span").last();              //通过select选择器选择foot的最后一个span对象
            amount = span.text();                       //取得span里面的文本元素即为评论数量
            datalist.add(amount);
            outputRs();                 //调用outputRs方法将datalist里面的每一条数据插入到数据库
        }

    }       

    /**
     * 输出到数据库
     * @throws Exception
     */
    private static void outputRs() throws Exception {

        String strout = "";
        for (int i = 0; i < datalist.size(); i++) {
            strout = strout + datalist.get(i) + ",";                //获取datalist集合中的每一条数据，串成一个字符串
        }

        if (bfile) {
            FileWriter fw = new FileWriter(outfile, true);          //实例化文件输出流
            PrintWriter out = new PrintWriter(fw);                  //实例化打印流
            if (countrs == 0)
                out.println(headtitle);                         //输出头标题
            out.println(strout);                        //输出刚刚串起来的strout字符串
            out.close();                                //关闭打印流
            fw.close();                             //关闭输出流
        }
        countrs = countrs + 1;
        System.out.println(countrs + "  " + strout);            //在命令行打印数据

        // 插入数据库
        if (bdb) {
            CrawlDatatoBase.InsertProduct(datalist);
        }
    }

    /**
     * 翻页爬取
     * 
     * @throws Exception
     */
    public static void skipPage(String surl) throws Exception{
        String html = RetrivePage.downloadPage(surl);           
        Document doc = Jsoup.parse(html);           
        Element footDiv = doc.getElementsByClass("paginator").first();          //获取页码部分的div对象
        Element footSpan = footDiv.getElementsByClass("next").first();   //获取class为"next"的节点对象用footSpan表示
        Element footA = footSpan.select("a[href]").first();         //选择footSpan中第一个带有href属性的a节点对象，并用footA表示
        String href = footA.attr("href");                       //获得footA中href属性中的内容href
        String http = "http://movie.douban.com/celebrity/1054424/movies"+href;      //将"http://movie.douban.com/celebrity/1054424/movies"和href拼接即为下一页面的url
        Element thispage = doc.getElementsByClass("thispage").first();      //获取当前页码节点
        int end = Integer.parseInt(thispage.text());        //获取当前页码中的数字元素（String类型），并转化为int类型
        if(end==1){
            getDouBanList(surl);
            System.out.println("=========================="+1+"===================");
        }
        getDouBanList(http);                    //爬取下一页面
        System.out.println("=========================="+(end+1)+"===================");   //打印一行页面分隔符
        if(end<19){
            skipPage(http);                     //由于一共是19页，所以end小于19的时候循环爬取
        }else{
            System.out.println("页面已爬完");            
        }
    }

    /**
     * 测试代码
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
        	
       // Elements links = doc.select("a[href]"); //带有href属性的a元素
        String html = RetrivePage.downloadPage(strURL);           
        Document doc = Jsoup.parse(html);           
        Element footDiv = doc.getElementsByClass("paginator").first();          //获取页码部分的div对象
        Element footSpan = footDiv.getElementsByClass("next").first();   //获取class为"next"的节点对象用footSpan表示
        Element footA = footSpan.select("a[href]").first();         //选择footSpan中第一个带有href属性的a节点对象，并用footA表示
        String href = footA.attr("href");                       //获得footA中href属性中的内容href
        String http = "http://movie.douban.com/celebrity/1054424/movies"+href;      //将"http://movie.douban.com/celebrity/1054424/movies"和href拼接即为下一页面的url
        Element thispage = doc.getElementsByClass("thispage").first();      //获取当前页码节点
        int end = Integer.parseInt(thispage.text());        //获取当前页码中的数字元素（String类型），并转化为int类型
        if(end==1){
            getDouBanList(strURL);
            System.out.println("=========================="+1+"===================");
        }
        getDouBanList(http);                    //爬取下一页面
        System.out.println("=========================="+(end+1)+"===================");   //打印一行页面分隔符
        if(end<19){
            skipPage(http);                     //由于一共是19页，所以end小于19的时候循环爬取
        }else{
            System.out.println("页面已爬完");            
        }
        
        
         try{
            CrawlDatatoBase.setConn();      
            skipPage(strURL);           //翻页爬取
            CrawlDatatoBase.closeConn();        //关闭数据库
        }catch(Exception e){
            e.printStackTrace();
        } 

    }

}