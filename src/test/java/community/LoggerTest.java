package community;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LoggerTest {
    private static final Logger logger = LoggerFactory.getLogger(LoggerTest.class);

    @Test
    void testLogger(){
        System.out.println(logger.getName());
        logger.debug("debug....");
        logger.info("info......");
        logger.warn("warn.......");
        logger.error("error......");
    }
}
