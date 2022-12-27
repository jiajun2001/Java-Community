package io.github.jiajun2001.community.community;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)

public class LoggerTest {
    private static final Logger logger = LoggerFactory.getLogger(LoggerTest.class);
    @Test
    public void testLogger() {
        System.out.println(logger.getName());
        logger.debug("debug Log");
        logger.info("info Log");
        logger.warn("warn Log");
        logger.error("error Log");
    }
}
