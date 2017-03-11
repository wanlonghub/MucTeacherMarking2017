package edu.muc.marking.util;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import edu.muc.marking.bean.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.*;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.*;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.URL;

/**
 * version    date      author
 * ──────────────────────────────────
 * 1.0       17-3-4   wanlong.ma
 * Description: Soap请求工具类
 * http://www.cnblogs.com/chenying99/archive/2013/05/23/3094128.html
 * Others:
 * Function List:
 * History:
 */
public class SoapUtil {

    private static Logger logger = LoggerFactory.getLogger(SoapUtil.class);

    /**
     * 信息门户认证soap请求
     * @param account
     * @param password
     * @return 返回的xml
     */
    private static String userAuthSoapRequest(String account,String password){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

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
            bodyElement.addChildElement("yhm").addTextNode(account);
            bodyElement.addChildElement("kl").addTextNode(password);

            //Save the message
            message.saveChanges();

            //Check the input
            message.writeTo(byteArrayOutputStream);

            //Send the message and get a reply

            //Set the destination
            URL destination =
                    new URL("http://ca.muc.edu.cn/zfca/axis/RzzxConfManage?wsdl");
            //Send the message
            SOAPMessage reply = connection.call(message, destination);

            //Check the output
            //Create the transformer
            TransformerFactory transformerFactory =
                    TransformerFactory.newInstance();
            Transformer transformer =
                    transformerFactory.newTransformer();
            //Extract the content of the reply
            Source sourceContent = reply.getSOAPPart().getContent();

            //Set the output for the transformation
            byteArrayOutputStream = new ByteArrayOutputStream();
            StreamResult result = new StreamResult(byteArrayOutputStream);
            transformer.transform(sourceContent, result);

            //Close the connection
            connection.close();


        } catch(Exception e) {
            logger.error("Soap请求失败：{}",e);
        }

        if(byteArrayOutputStream == null)
            return Strings.nullToEmpty(null);
        else{
            String result = new String(byteArrayOutputStream.toByteArray());
            try{
                byteArrayOutputStream.close();
            }catch (IOException e){
                logger.error("流关闭失败",e);
            }
            return result;
        }

    }

    /**
     * 用户登录认证
     * @param account
     * @param password
     * @return
     */
    public static UserInfo userLogin(String account, String password) {
        Preconditions.checkNotNull(account,password);
        String res = userAuthSoapRequest(account,password);
        Preconditions.checkNotNull(res);

        UserInfo userInfo = new UserInfo();
        userInfo.setPassword(password);
        userInfo.setAuthPassed(false);

        try(ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(res.getBytes())) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(byteArrayInputStream);
            Node node = doc.getFirstChild().getFirstChild(); // soapenv:Body
            NodeList nodeList = node.getChildNodes();
            for(int i=0;i<nodeList.getLength();i++){

                Node childNode = nodeList.item(i);
                if(childNode.getNodeName().equals("multiRef")){

                    // 用户信息
                    NodeList multiRefChildNodeList = childNode.getChildNodes();
                    for(int j=0;j<multiRefChildNodeList.getLength();j++){
                        Node multiRefChildNodeListChildNode = multiRefChildNodeList.item(j);
                        if(multiRefChildNodeListChildNode.getNodeName().equals("name")){
                            userInfo.setName(multiRefChildNodeListChildNode.getTextContent());
                        }else if(multiRefChildNodeListChildNode.getNodeName().equals("userId")){
                            userInfo.setAccount(multiRefChildNodeListChildNode.getTextContent());
                        }
                    }

                    // 是否成功
                    NamedNodeMap namedNodeMap = childNode.getAttributes();
                    for(int k=0;k<namedNodeMap.getLength();k++){
                        Node attributeNode = namedNodeMap.item(k);
                        if(attributeNode.getNodeName().equals("id") && attributeNode.getNodeValue().equals("id2")){
                            if(childNode.getTextContent().equals("1")){
                                userInfo.setAuthPassed(true);
                            }
                        }
                    }

                }
            }

        } catch (Exception e) {
            logger.error("解析xml失败",e);
        }

        return userInfo;
    }

    public static void main(String[] args){
        logger.info(userLogin("13052043","wanlong999").toString());
    }


}
