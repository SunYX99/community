package community.controller;

import community.annotation.LoginRequired;
import community.entity.LoginTicket;
import community.entity.User;
import community.mapper.LoginTicketMapper;
import community.service.UserService;
import community.util.CommunityUtil;
import community.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${community.path.upload}")
    private String uploadPath;
    @Value("${community.path.domain}")
    private String domain;
    @Value("${server.servlet.context-path}")
    private String contextPath;
    @Autowired
    private UserService userService;
    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(path = "/setting",method = RequestMethod.GET)
    @LoginRequired
    public String getSettingPage(){
        return "/site/setting";
    }

    @RequestMapping(path = "/upload",method = RequestMethod.POST)
    @LoginRequired
    public String uploadHeader(MultipartFile headImage, Model model){

        if(headImage == null){
            model.addAttribute("error","您还没有选择图片!");
            return "/site/setting";
        }
        String originalFilename = headImage.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //判断后缀是否合理
        if(StringUtils.isBlank(suffix)){
            model.addAttribute("error","文件格式不正确!");
            return "/site/setting";
        }
        //生成随机文件名
        String filename =  CommunityUtil.generateUUID() + suffix;
        //确定文件存放路径
        File dest = new File(uploadPath + "/" + filename);
        try {
            headImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("上传文件失败,服务器发生异常!" + e.getMessage());
            throw  new RuntimeException("上传文件失败,服务器发生异常!");
        }
        //更新当前用户头像的访问路径(Web访问路径)
        //http://localhost:8080/community/user/header/xxx.png
        User user = hostHolder.getUser();
        String headUrl = domain + contextPath + "/user/header/" + filename;
        userService.updateHeader(user.getId(),headUrl);
        return "redirect:/index";
    }

    @RequestMapping(path = "/header/{fileName}",method = RequestMethod.GET)
    public void getHeader(@PathVariable String fileName, HttpServletResponse response){
        // 服务器存放路径
        fileName =  uploadPath + "/" + fileName;
        // 文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        // 响应图片
        response.setContentType("image/" + suffix);

        try (   ServletOutputStream outputStream = response.getOutputStream();
                FileInputStream fis = new FileInputStream(fileName);
        ) {
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fis.read(buffer)) != -1){
                outputStream.write(buffer, 0, b);
            }
        } catch (IOException e) {
            logger.error("读取图像失败！"+e.getMessage());
        }

    }

    //修改密码
    @LoginRequired
    @RequestMapping(path = "/updatePassword",method = RequestMethod.POST)
    public String updatePassword(String oldPassword, String newPassword, Model model, @CookieValue("ticket") String ticket){
        Map<String, Object> map = userService.updatePassword(oldPassword, newPassword);
        if(map.containsKey("passwordMsg")){
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            return "/site/setting";
        }
        // 退出用户
        userService.logout(ticket);
        return "redirect:/login";
    }
}
