package io.github.jiajun2001.community.community;

import io.github.jiajun2001.community.community.dao.DiscussPostMapper;
import io.github.jiajun2001.community.community.dao.UserMapper;
import io.github.jiajun2001.community.community.entity.DiscussPost;
import io.github.jiajun2001.community.community.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;
import java.util.List;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)

public class MapperTests {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Test
    public void testSelectUser() {
        User user = userMapper.selectById(101);
        System.out.println(user);

        user = userMapper.selectByName("liubei");
        System.out.println(user);

        user = userMapper.selectByEmail("nowcoder101@sina.com");
        System.out.println(user);
    }

    @Test
    public void testInsertUser() {
        User user = new User();
        user.setUsername("tester");
        user.setPassword("123456");
        user.setSalt("abc");
        user.setEmail("jiajun@qq.com");
        user.setHeaderURL("http://www.nowcode.com/101.png");
        user.setCreateTime(new Date());

        int rows = userMapper.insertUser(user);
        System.out.println(rows);
        System.out.println(user.getId());
    }

    @Test
    public void updateUser() {
        int rows = userMapper.updateStatus(150, 1);
        System.out.println(rows);
        rows = userMapper.updateHeader(150, "http://www.nowcode.com/102.png");
        System.out.println(rows);
        rows = userMapper.updatePassword(150, "99999");
        System.out.println(rows);
    }

    @Test
    public void testSelectPosts() {
        List<DiscussPost> list = discussPostMapper.selectDiscussPosts(149, 0, 10);
        for (DiscussPost post : list) {
            System.out.println(post);
        }
        int rows = discussPostMapper.selectDiscussPostRows(149);
        System.out.println(rows);
    }

    @Test
    public void testUserHeader() {
        User user = userMapper.selectById(1);
        System.out.println(user.getHeaderURL());
    }

    @Test
    public void testSelectPostRows() {
        System.out.println(discussPostMapper.selectDiscussPostRows(0));
    }
}
