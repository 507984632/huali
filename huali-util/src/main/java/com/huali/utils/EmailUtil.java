package com.huali.utils;

import lombok.extern.slf4j.Slf4j;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * 发送邮箱的工具
 *
 * <dependency>
 * <groupId>com.sun.mail</groupId>
 * <artifactId>javax.mail</artifactId>
 * <version>1.6.2</version>
 * </dependency>
 *
 * @author Yang_my
 * @date 2020-06-07 22:40
 **/
@Slf4j
public class EmailUtil {
    /**
     * 测试用例
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            EmailUtil.sendEmail(
                    "a507984632",//网易邮箱的名
                    "PJIKJQNZJJWDOLTA", //网易邮箱的密码
                    "a507984632@163.com", //发送邮件的地址
                    "2338141915@qq.com", //接受邮件的地址
                    "亲爱刘先生,您好", //邮件主题
                    //邮件内容
                    "1、人民不能没有面包而生活，人民也不能没有祖国而生活。——雨果\n" +
                            "　　4、为中华之崛起而读书。——周恩来\n" +
                            "\n" +
                            "　　5、位卑未敢忘忧国。——陆游\n" +
                            "\n" +
                            "　　6、爱国如饥渴。——班固\n" +
                            "\n" +
                            "　　7、爱国主义的力量多么伟大呀！在它面前，人的爱生之念，畏苦之情，算得是什么呢！在它面前，人本身也算得是甚么呢！——车尔尼雪夫斯基\n" +
                            "\n" +
                            "　　8、爱祖国高于一切。——肖邦\n" +
                            "\n" +
                            "　　9、最大的荣誉是保卫祖国的荣誉。——亚里士多德\n" +
                            "\n" +
                            "　　10、只有热爱祖国，痛心祖国所受的严重苦难，憎恨敌人，这才给了我们参加斗争和取得胜利的力量。——阿·托尔斯泰");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        System.out.println(" ");
    }

    private EmailUtil(){
    }

    /**
     * 发送邮件的功能 只支持 网易的邮箱 163.com
     *
     * @param userName    发送者的用户名
     * @param passWord    发送者的密码
     *                    例如163邮箱的密码，应该是在163邮箱内自动生成的一个字符串
     * @param fromEmail   发送者的电子邮箱
     * @param addresEmail 接受者的电子邮箱
     *                    收件人可以是任意的邮箱地址
     * @param subject     发送邮件的主题
     *                    发送的标题
     * @param content     发送邮件的内容
     */
    private static void sendEmail(String userName, String passWord, String fromEmail, String addresEmail, String subject, String content) throws MessagingException {
        log.info("**********************************发送邮件开始**********************************");

        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "SMTP");
        props.setProperty("mail.host", "smtp.163.com");
        props.setProperty("mail.smtp.auth", "true");

        //2. 创建验证器验证发送者
        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                //发件人的用户名和密码
                return new PasswordAuthentication(userName, passWord);
            }
        };
        Session session = Session.getInstance(props, auth);

        //3. 创建邮件对象
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromEmail)); //设置发件人地址
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(addresEmail));/*设置邮件的类型*//*设置收件人地址*/ //965938388
        message.setSubject(subject); //设置邮件的主题
        message.setContent(content, "text/html;charset=utf-8");//设置邮件的内容

        //4. 发送邮件
        Transport.send(message);

        log.info("发送人地址：{},收件人地址：{}", fromEmail, addresEmail);
        log.info("发送内容：{}", content);
        log.info("**********************************发送邮件结束**********************************");
    }
}