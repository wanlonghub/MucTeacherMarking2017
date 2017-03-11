package edu.muc.marking.test;



import com.google.common.primitives.Ints;
import com.sun.org.apache.xpath.internal.SourceTree;
import edu.muc.marking.bean.SubmitResult;
import edu.muc.marking.dao.ScoreDao;
import edu.muc.marking.dao.TokenDao;
import edu.muc.marking.dao.UserInfoDao;
import edu.muc.marking.db.DBUtil;
import edu.muc.marking.util.FileUtil;
import edu.muc.marking.util.OtherUtil;
import junit.framework.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.soap.*;
import java.io.*;

import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

/**
 * version    date      author
 * ──────────────────────────────────
 * 1.0       17-3-4   wanlong.ma
 * Description:
 * Others:
 * Function List:
 * History:
 */
public class TestApp extends TestCase {

    private static Logger logger = LoggerFactory.getLogger(TestApp.class);
    private Connection conn = null;

    public void testA(){
        logger.info("test content");
    }

    /**
     * 增 insert
     */
    public void testInsert(){

        try{
            conn = DBUtil.openConnection();
            String sql = "INSERT INTO t_test(name) values(?)";
            conn.setAutoCommit(true);
            int count = DBUtil.execute(conn, sql, "aaaa");
            logger.info("{} -> {}",sql,count);
        }catch(SQLException e){
            logger.error("SQLException",e);
        }finally {
            DBUtil.closeConnection();
        }

    }

    /**
     * 删
     */
    public void testDelete(){

        try{
            conn = DBUtil.openConnection();
            String sql = "DELETE FROM t_test WHERE id = ?";
            conn.setAutoCommit(true);
            int count = DBUtil.execute(conn, sql, 8);
            logger.info("{} -> {}",sql,count);
        }catch(SQLException e){
            logger.error("SQLException",e);
        } finally {
            DBUtil.closeConnection();
        }

    }

    /**
     * 改
     */
    public void testUpdate(){

        try{
            conn = DBUtil.openConnection();
            String sql = "UPDATE t_test SET name = ? WHERE id = ?";
            conn.setAutoCommit(true);
            int count = DBUtil.execute(conn, sql, "new name",1);
            logger.info("{} -> {}",sql,count);
        }catch(SQLException e){
            logger.error("SQLException",e);
        } finally {
            DBUtil.closeConnection();
        }

    }

    /**
     * 查
     */
    public void testSelect() throws InstantiationException {

        try{
            conn = DBUtil.openConnection();

            // 查询单个对象
            String sql = "SELECT name FROM t_test WHERE id = ?";
            conn.setAutoCommit(true);
            String str = DBUtil.queryObject(conn, sql, String.class, 1);
            logger.info("{} -> {}",sql,str);

            // 查询对个对象
            sql = "SELECT name FROM t_test WHERE id IN(?,?,?,?)";
            List<String> lists = DBUtil.queryObjectList(conn, sql, String.class, 1,6,7,9);
            for(String s : lists){
                logger.info("\t\t {} -> {}",sql,s);
            }

        }catch(SQLException e){
            logger.error("SQLException",e);
        } catch (IllegalAccessException e){
            logger.error("IllegalAccessException",e);
        }finally {
            DBUtil.closeConnection();
        }

    }

    public void testSoap() throws IOException,SOAPException{
        String urlString = "http://ca.muc.edu.cn/zfca/axis/RzzxConfManage?wsdl";
        String xmlFile = FileUtil.getPath("soapRequest.xml");// 要发送的soap格式文件
        String soapActionString = "http://ca.muc.edu.cn/zfca/axis/RzzxConfManage?wsdl";//Soap 1.1中使用
        URL url = new URL(urlString);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        File fileToSend = new File(xmlFile);
        byte[] buf = new byte[(int) fileToSend.length()];// 用于存放文件数据的数组
        new FileInputStream(xmlFile).read(buf);
//		httpConn.setRequestProperty("Content-Length",
//				String.valueOf(buf.length));//这句话可以不用写，即使是随便写
        //根据我的测试，过去的请求头中的Content-Length长度也是正确的，也就是说它会自动进行计算
        httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
        httpConn.setRequestProperty("soapActionString",soapActionString);//Soap
        httpConn.setRequestMethod("POST");
        httpConn.setDoOutput(true);
        httpConn.setDoInput(true);
        OutputStream out = httpConn.getOutputStream();
        out.write(buf);
        out.close();
        InputStreamReader is = new InputStreamReader(httpConn.getInputStream(),
                "utf-8");
        BufferedReader in = new BufferedReader(is);
        String inputLine;
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("result.xml")));// 将结果存放的位置
        while ((inputLine = in.readLine()) != null) {
            System.out.println(inputLine);
            bw.write(inputLine);
            bw.newLine();
        }
        bw.close();
        in.close();
        httpConn.disconnect();

    }


    public static String invokeSrv(String endpoint,String action, String soapXml) throws IOException{
        StringBuilder sb = new StringBuilder();

        String method = "POST";

        URL url = new URL(endpoint);
        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();

        // POST请求
        urlConn.setRequestMethod(method);
        // 设置要发送消息
        urlConn.setDoOutput(true);
        // 设置要读取响应消息
        urlConn.setDoInput(true);
        // POST不能使用cache
        urlConn.setUseCaches(false);

        urlConn.setInstanceFollowRedirects(true);

        urlConn.setRequestProperty("POST", "http://ca.muc.edu.cn/zfca/axis/RzzxConfManage?wsdl HTTP/1.1");
        urlConn.setRequestProperty("Connection", "Keep-Alive");
        urlConn.setRequestProperty("User-Agent", "PHP-SOAP/5.2.5");
        urlConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
        urlConn.setRequestProperty("SOAPAction", "http://ca.muc.edu.cn/zfca/axis/RzzxConfManage?wsdl:checklogin#checklogin");

        urlConn.connect();

        // 向输出流写出数据，这些数据将存到内存缓冲区中
        PrintWriter pw = new PrintWriter(urlConn.getOutputStream());
        pw.write(soapXml);

        // 刷新对象输出流，将任何字节都写入潜在的流中
        pw.flush();

        // 关闭流对象。此时，不能再向对象输出流写入任何数据，先前写入的数据存在于内存缓冲区中,
        // 在调用下边的getInputStream()函数时才把准备好的http请求正式发送到服务器
        pw.close();

        // 接收返回消息
        // 解析返回值编码格式
        String charset = "UTF-8";
        String ct = urlConn.getContentType();
        Pattern p = Pattern.compile("charset=.*;?");
        Matcher m = p.matcher(ct);
        if(m.find()){
            charset = m.group();
            // 去除charset=和;,如果有的话
            if(charset.endsWith(";")){
                charset = charset.substring(charset.indexOf("=") + 1, charset.indexOf(";"));
            }else{
                charset = charset.substring(charset.indexOf("=") + 1);
            }
            // charset = "\"UTF-8\"";
            // 去除引号 ,如果有的话
            if(charset.contains("\"")){
                charset = charset.substring(1, charset.length() - 1);
            }
            charset = charset.trim();
        }

        // 将内存缓冲区中封装好的完整的HTTP请求电文发送到服务端。
        // <===注意，实际发送请求的代码段就在这里
        InputStream inStream = urlConn.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(inStream,charset));

        String line;
        while((line = br.readLine()) != null){
            sb.append(line);
        }
        br.close();
        urlConn.disconnect();

        return sb.toString();
    }

    public static void main(String[] args) {

        try {

            //First create the connection
            SOAPConnectionFactory soapConnFactory =
                    SOAPConnectionFactory.newInstance();
            SOAPConnection connection =
                    soapConnFactory.createConnection();

            //Next, create the actual message
            MessageFactory messageFactory = MessageFactory.newInstance();
            SOAPMessage message = messageFactory.createMessage();

            //Create objects for the message parts
            SOAPPart soapPart =     message.getSOAPPart();
            SOAPEnvelope envelope = soapPart.getEnvelope();
            SOAPBody body =         envelope.getBody();

            //Create the main element and namespace
            SOAPElement bodyElement =
                    body.addChildElement(envelope.createName("checklogin" ,
                            "checklogin",
                            "http://ca.muc.edu.cn/zfca/axis/RzzxConfManage?wsdl"));
            //Add content
            bodyElement.addChildElement("yhm").addTextNode("13052043");
            bodyElement.addChildElement("kl").addTextNode("wanlong99");
            //Save the message
            message.saveChanges();
            //Check the input
            System.out.println("\nREQUEST:\n");
            message.writeTo(System.out);
            System.out.println();

            //Send the message and get a reply

            //Set the destination
            URL destination =
                    new URL("http://ca.muc.edu.cn/zfca/axis/RzzxConfManage?wsdl");
            //Send the message
            SOAPMessage reply = connection.call(message, destination);

            //Check the output
            System.out.println("\nRESPONSE:\n");
            //Create the transformer
            TransformerFactory transformerFactory =
                    TransformerFactory.newInstance();
            Transformer transformer =
                    transformerFactory.newTransformer();
            //Extract the content of the reply
            Source sourceContent = reply.getSOAPPart().getContent();
            //Set the output for the transformation
            StreamResult result = new StreamResult(System.out);
            transformer.transform(sourceContent, result);
            System.out.println();

            //Close the connection
            connection.close();

        } catch(Exception e) {
            System.out.println(e.getMessage());
        }


    }

    public void testToken(){
        int i = TokenDao.isTokenValid("6c7ad0fe8f294fe3961e1b7db43c9f2d");
    }

    public void testQueryClass(){
        System.out.println(UserInfoDao.queryClassBeanByClassId(1));
        System.out.println(UserInfoDao.queryAcademyById(1));
        System.out.println(UserInfoDao.queryTeacherById(3));
        System.out.println(UserInfoDao.queryUserInfoByToken("611f163c2fdc409082800acdae93ddfa").toString());
    }

    public void testText(){

        for(int i=0;i<20;i++){
            System.out.println("<div class=\"fb_componentWrapper\">\n" +
                    "                        <div class=\"fb_component radio\">\n" +
                    "                            <div class=\"fb_componentText\">\n" +
                    "                                <!--题目-->\n" +
                    "                                <p class=\"fbc_title\" style=\"line-height:1.7;font-size:16px;font-weight:normal;color:#000000;\"><span>" + (i+1) + ". </span><span><span>question</span></span></p>\n" +
                    "                            </div>\n" +
                    "                            <form>\n" +
                    "                                <label><input class=\"section_item\" type=\"radio\" arrray_index=\"" + i + "\" value=\"5\" name=\"" + i + "\"/>5分</label><br/>\n" +
                    "                                <label><input class=\"section_item\" type=\"radio\" arrray_index=\"" + i + "\" value=\"4\" name=\"" + i + "\" />4分</label><br/>\n" +
                    "                                <label><input class=\"section_item\" type=\"radio\" arrray_index=\"" + i + "\" value=\"3\" name=\"" + i + "\" />3分</label><br/>\n" +
                    "                                <label><input class=\"section_item\" type=\"radio\" arrray_index=\"" + i + "\" value=\"2\" name=\"" + i + "\" />2分</label><br/>\n" +
                    "                                <label><input class=\"section_item\" type=\"radio\" arrray_index=\"" + i + "\" value=\"1\" name=\"" + i + "\" />1分</label><br/>\n" +
                    "                            </form>\n" +
                    "                        </div>\n" +
                    "                        <div class=\"clearB\"></div>\n" +
                    "                    </div>");
            System.out.println();
        }

    }

    public void testParseAnswer(){
        String answer = "351";
        int[] array = OtherUtil.parseStringAnswerToIntArray(answer);
        System.out.println(answer);
        System.out.println(Ints.join("-",array));
        System.out.println(OtherUtil.getSumOfArray(array));
    }

    public void testsubmitAnswerWithToken(){

        SubmitResult submitResult = new SubmitResult();

        String token = "1214f3a5efe54c9ca6a01ee680d670a2";
        String answer = "5432112345";

        ScoreDao.submitAnswerWithToken(submitResult,token,answer);

        System.out.println(submitResult.toString());




    }



}
