package kr.co.eceris.projectk;

import org.junit.BeforeClass;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

public class TestContext {
    public static ConfigurableApplicationContext context;

    @BeforeClass
    public static void initialize() {
        context = SpringApplication.run(Application.class);
    }

}
