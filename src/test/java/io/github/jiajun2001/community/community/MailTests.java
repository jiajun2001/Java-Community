package io.github.jiajun2001.community.community;

import io.github.jiajun2001.community.community.util.MailClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MailTests {

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void testTextMail() {
        mailClient.sendMail("chanjianzhe2002@gmail.com", "Hello", "Hello bro! Why malaysian are so smart like you!");
    }

    @Test
    public void testHTMLMail() {
        Context context = new Context();
        context.setVariable("username", "Eugene Handsome Baby");
        String htmlString = templateEngine.process("/mail/demo", context);
        System.out.println(htmlString);
        mailClient.sendMail("chanjianzhe2002@gmail.com", "Replacement Exam", htmlString);
    }

}
