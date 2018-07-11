package kr.co.eceris.projectk;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class StaticTest {

    @Test
    public void asdf() {
        String test1 = new BCryptPasswordEncoder().encode("test1");
        System.out.println(test1);
    }

    @Test
    public void aaa() {
        Date date = new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(720));
        System.out.println(date);
    }
}
