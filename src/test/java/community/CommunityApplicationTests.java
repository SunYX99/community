package community;

import community.entity.DiscussPost;
import community.entity.User;
import community.mapper.DisscussPostMapper;
import community.mapper.UserMapper;
import org.apache.ibatis.annotations.Mapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class CommunityApplicationTests {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DisscussPostMapper disscussPostMapper;

    @Test
    void contextLoads() {
        System.out.println(("----- selectAll method test ------"));
        List<User> userList = userMapper.selectList(null);
        userList.forEach(System.out::println);
    }
    @Test
    void selectById(){
        User user = userMapper.selectById(1);
        System.out.println(user);
    }

    @Test
    void selectByname(){
        User user = userMapper.selectByName("aaa");
        System.out.println(user);
    }
    @Test
    void InsertUser(){
        User user = new User();
        userMapper.insertUser(user);
        System.out.println(user);
    }
    @Test
    void testSelectPosts(){
        List<DiscussPost> discussPosts = disscussPostMapper.selectDiscussPosts(149, 0, 10);
        for (DiscussPost discussPost : discussPosts) {
            System.out.println(discussPost);
        }

        int count = disscussPostMapper.selectDiscussPostRows(149);
        System.out.println(count);
    }

}
