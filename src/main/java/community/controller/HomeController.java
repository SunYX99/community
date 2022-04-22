package community.controller;

import community.entity.DiscussPost;
import community.entity.Page;
import community.entity.User;
import community.service.DiscussPostService;
import community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {
    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/index",method = RequestMethod.GET)
    public String getIndexPage(Model model, Page page){
        //方法调用之前，springmvc会自动实例化model和Page,并将page注入model
        //所以在thyemleaf中可以直接访问page对象的数据
        page.setRows(discussPostService.findDisscussPostRows(0));
        page.setPath("/index");
        List<DiscussPost> list = discussPostService.findDisscussPosts(0, page.getOffset(), page.getLimit());
        List<Map<String,Object>> disscussPosts = new ArrayList<>();
        if(list.size()!=0){
            for (DiscussPost disscussPost : list) {
                Map<String,Object> map = new HashMap<>();
                map.put("post",disscussPost);
                User user = userService.findUserById(disscussPost.getUserId());
                map.put("user",user);
                disscussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts",disscussPosts);
        return "/index";
    }
}
