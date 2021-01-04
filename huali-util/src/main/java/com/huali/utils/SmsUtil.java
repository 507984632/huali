package com.huali.utils;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import lombok.extern.slf4j.Slf4j;

/**
 * 发送短信的工具类
 *
 * <dependency>
 * <groupId>com.aliyun</groupId>
 * <artifactId>aliyun-java-sdk-core</artifactId>
 * <version>4.5.0</version>
 * </dependency>
 *
 * @author Yang_my
 * @date 2020-06-07 22:41
 **/
@Slf4j
public class SmsUtil {

    /**
     * 测试用例
     *
     * @param args
     */
    public static void main(String[] args) {
        sendMessage("LTAI4GJbSJ4psHG21PVqLdxb", "4GejULkrEnh3JW3hZqla8EB2nFGTkg", "18732612059"
                , "阳仔通知", "SMS_191817610", 123456);
        System.out.println("发送成功");
    }

    private SmsUtil() {
    }

    /**
     * accessKeyId 阿里的签名
     * TODO 这里也不应该写死， 可以将这里的配置到配置文件中
     * 通过 @Value("") 注解 来让它自动注入比较好
     */
    private static final String ACCESS_KEY_ID = "LTAI4GJbSJ4psHG21PVqLdxb";
    /**
     * accessSecret 阿里签名的密钥
     */
    private static final String ACCESS_SECRET = "4GejULkrEnh3JW3hZqla8EB2nFGTkg";

    /**
     * 发送短信的功能 固定的签名
     *
     * @param phoneNumber  要接受短信的手机号
     * @param signName     发送短信的短信签名
     * @param templateCode 发送短信的模板 code 值
     * @param code         随机的验证码
     */
    public static void sendMessage(String phoneNumber, String signName, String templateCode, Integer code) {
        sendMessage(ACCESS_KEY_ID, ACCESS_SECRET, phoneNumber, signName, templateCode, code);
    }

    /**
     * 发送短信的功能 支持任意阿里签名
     *
     * @param accessKeyId  阿里的签名
     * @param accessSecret 阿里签名的密钥
     * @param phoneNumber  要接受短信的手机号
     * @param signName     发送短信的短信签名
     * @param templateCode 发送短信的模板 code 值
     * @param code         随机的验证码
     */
    public static void sendMessage(String accessKeyId, String accessSecret, String phoneNumber, String signName, String templateCode, Integer code) {
        log.info("**********************************发送短信开始**********************************");
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessSecret);
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        //要接受短信的手机号码
        request.putQueryParameter("PhoneNumbers", phoneNumber);
        //发送短信的签名
        request.putQueryParameter("SignName", signName);
        //发送短信的模板 code 值
        request.putQueryParameter("TemplateCode", templateCode);
        // 要发送的验证码
        request.putQueryParameter("TemplateParam", "{\"code\":" + code + "}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            log.info("收件人号码：{}", phoneNumber);
            log.info("发送的短信签名：{}", signName);
            log.info("发送的验证码：{}", code);
            log.info("**********************************发送短信结束**********************************");
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}