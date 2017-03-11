package edu.muc.marking.test;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import edu.muc.marking.bean.UserInfo;
import edu.muc.marking.dao.UserInfoDao;
import edu.muc.marking.util.SoapUtil;
import junit.framework.TestCase;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * version    date      author
 * ──────────────────────────────────
 * 1.0       17-3-5   wanlong.ma
 * Description:
 * Others:
 * Function List:
 * History:
 */
public class ActionTest extends TestCase {

    public void testLoginAction() throws IOException {
// 门户登录
        UserInfo userInfo = SoapUtil.userLogin("13052043","wanlong99");
        // 验证成功
        if(userInfo.isAuthPassed()){
            // 获取完整的用户信息
            UserInfoDao.queryCompleteUserInfo(userInfo);
        }else{
            userInfo.setMessage("用户名或密码错误");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        JsonGenerator jsonGenerator = objectMapper.getJsonFactory().createJsonGenerator(System.out);
        jsonGenerator.writeObject(userInfo);
        jsonGenerator.close();
    }
}
