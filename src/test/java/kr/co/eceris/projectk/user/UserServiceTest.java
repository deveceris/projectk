package kr.co.eceris.projectk.user;

import kr.co.eceris.projectk.TestContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UserServiceTest extends TestContext {
    private UserService userService;

    @Before
    public void init() {
        userService = context.getBean(UserService.class);
    }

    @Test
    public void 유저생성테스트() {
        UserVo userVo = new UserVo();
        userVo.setUsername("테스트유저");
        userVo.setPassword("test");
        User saved = userService.create(userVo);
        Assert.assertTrue("is same username?", userVo.getUsername().equals(saved.getUsername()));
    }

    @Test
    public void 유저조회() {
        UserVo userVo = new UserVo();
        userVo.setUsername("테스트유저1");
        userVo.setPassword("test1");
        User saved = userService.create(userVo);

        User found = userService.get(saved.getId());
        Assert.assertTrue("is same user?", saved.getId() == found.getId());
    }
}
