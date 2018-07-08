package kr.co.eceris.projectk;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Test {

    @org.junit.Test
    public void asdf() {
        String test1 = new BCryptPasswordEncoder().encode("test1");
        System.out.println(test1);
    }
}
