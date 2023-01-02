package io.github.jiajun2001.community.community;

import io.github.jiajun2001.community.community.util.SensitiveFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class SensitiveTest {
    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Test
    public void testSensitiveFilter() {
        String text = "BITCH! You #F#u$c#k$i#n$g! nerd!! Java is the best language. You IdIoT should learn this haha!";
        System.out.println(sensitiveFilter.filter(text));
    }
}
