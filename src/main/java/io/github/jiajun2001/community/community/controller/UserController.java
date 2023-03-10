package io.github.jiajun2001.community.community.controller;

import io.github.jiajun2001.community.community.annotation.LoginRequired;
import io.github.jiajun2001.community.community.entity.User;
import io.github.jiajun2001.community.community.service.FollowService;
import io.github.jiajun2001.community.community.service.LikeService;
import io.github.jiajun2001.community.community.service.UserService;
import io.github.jiajun2001.community.community.util.CommunityConstant;
import io.github.jiajun2001.community.community.util.CommunityUtil;
import io.github.jiajun2001.community.community.util.HostHolder;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController implements CommunityConstant {

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

    @Autowired
    private LikeService likeService;

    @Autowired
    private FollowService followService;

    @LoginRequired
    @RequestMapping(path = "/setting", method = RequestMethod.GET)
    public String getSettingPage() {
        return "/site/setting";
    }

    @LoginRequired
    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerImage, Model model) {
        if (headerImage == null) {
            model.addAttribute("error", "You have not chosen a picture!");
            return "/site/setting";
        }

        String fileName = headerImage.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf('.'));
        if (StringUtils.isBlank(suffix)) {
            model.addAttribute("error", "The file path is incorrect!");
            return "/site/setting";
        }

        User user = hostHolder.getUser();

        // Delete old profile picture
        String oldHeaderURL = user.getHeaderURL();
        String oldProfileImagePath = uploadPath + oldHeaderURL.substring(oldHeaderURL.lastIndexOf('/'));
        File myObj = new File(oldProfileImagePath);
        if (myObj.delete()) {
            System.out.println("Deleted the file: " + myObj.getName());
        } else {
            System.out.println("Failed to delete the file.");
        }

        fileName = CommunityUtil.generateUUID() + suffix;
        File dest = new File(uploadPath + "/" + fileName);
        try {
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("Fail to upload the file" + e.getMessage());
            throw new RuntimeException("Fail to upload the file!", e);
        }

        String headerURL = domain + contextPath + "/user/header/" + fileName;
        userService.updateHeader(user.getId(), headerURL);

        return "redirect:/index";
    }

    @RequestMapping(path = "/header/{fileName}", method = RequestMethod.GET)
    public void getHeader(@PathVariable("fileName")String fileName, HttpServletResponse response) {
        fileName = uploadPath + "/" + fileName;
        // Get the suffix of the file
        String suffix = fileName.substring(fileName.lastIndexOf("."));

        response.setContentType("image/" + suffix);
        try (
                FileInputStream fis = new FileInputStream(fileName);
                OutputStream os = response.getOutputStream();
        ) {
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fis.read(buffer)) != -1) {
                os.write(buffer, 0, b);
            }
        } catch (IOException e) {
            logger.error("Fail to load profile image: " + e.getMessage());
        }
    }

    @LoginRequired
    @RequestMapping(path = "/changePassword", method = RequestMethod.POST)
    public String changePassword(String originalPassword, String newPassword, String confirmedPassword, Model model) {
        User user = hostHolder.getUser();
        Map<String, Object> map = userService.changePassword(user, originalPassword, newPassword, confirmedPassword);
        System.out.println(map);
        if (map == null || map.isEmpty()) {
            return "redirect:/logout";
        } else {
            model.addAttribute("originalPasswordMsg", map.get("originalPasswordMsg"));
            model.addAttribute("newPasswordMsg", map.get("newPasswordMsg"));
            model.addAttribute("confirmedPasswordMsg", map.get("confirmedPasswordMsg"));
            return "/site/setting";
        }
    }

    @RequestMapping(path = "/profile/{userId}", method = RequestMethod.GET)
    public String getProfilePage(@PathVariable("userId") int userId, Model model) {
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("The user does not exist!");
        }

        // User information
        model.addAttribute("user", user);
        int likeCount = likeService.findUserLikeCount(userId);
        model.addAttribute("likeCount", likeCount);

        // Count the followee number
        long followeeCount = followService.findFolloweeCount(userId, ENTITY_TYPE_USER);
        model.addAttribute("followeeCount", followeeCount);

        // Count the follower number
        long followerCount = followService.findFollowerCount(ENTITY_TYPE_USER, userId);
        model.addAttribute("followerCount", followerCount);

        // Check if the user has followed the entity or not
        boolean hasFollowed = false;
        if (hostHolder.getUser() != null) {
            hasFollowed = followService.hasFollowed(hostHolder.getUser().getId(), ENTITY_TYPE_USER, userId);
        }
        model.addAttribute("hasFollowed", hasFollowed);

        return "/site/profile";
    }
}
