package io.github.jiajun2001.community.community;

import io.github.jiajun2001.community.community.entity.Message;
import io.github.jiajun2001.community.community.service.MessageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MessageTest {

    @Autowired
    private MessageService messageService;

    @Test
    public void messageTest() {
        Message message = messageService.findLatestNotice(111, "follow");
        System.out.println(message);
    }
}
