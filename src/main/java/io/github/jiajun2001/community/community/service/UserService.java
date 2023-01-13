package io.github.jiajun2001.community.community.service;

import io.github.jiajun2001.community.community.dao.LoginTicketMapper;
import io.github.jiajun2001.community.community.dao.UserMapper;
import io.github.jiajun2001.community.community.entity.LoginTicket;
import io.github.jiajun2001.community.community.entity.User;
import io.github.jiajun2001.community.community.util.CommunityConstant;
import io.github.jiajun2001.community.community.util.CommunityUtil;
import io.github.jiajun2001.community.community.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class UserService implements CommunityConstant {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    public User findUserById(int id) {
        return userMapper.selectById(id);
    }

    public Map<String, Object> register(User user) {
        Map<String, Object> map = new HashMap<>();

        // Handle empty value
        if (user == null) {
            throw new IllegalArgumentException("Parameters cannot be empty!");
        }

        if (StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg", "Account cannot be empty!");
            return map;
        }

        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg", "Password cannot be empty!");
            return map;
        }

        if (StringUtils.isBlank(user.getEmail())) {
            map.put("emailMsg", "Email cannot be empty!");
            return map;
        }

        // Check Account
        User u = userMapper.selectByName(user.getUsername());
        if (u != null) {
            map.put("usernameMsg", "Account is existed!");
            return map;
        }

        // Check Email
        u = userMapper.selectByEmail(user.getEmail());
        if (u != null) {
            map.put("emailMsg", "Email is existed!");
            return map;
        }

        // Handle Password
        user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
        user.setPassword(CommunityUtil.md5(user.getPassword() + user.getSalt()));

        // Handle other attributes
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.generateUUID());
        user.setHeaderURL(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setCreateTime(new Date());

        // Register
        userMapper.insertUser(user);

        // Send verification email
        Context context = new Context();
        context.setVariable("username", user.getUsername());

        // Set link: http://localhost:8080/community/activation/101/code
        String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url", url);
        String content = templateEngine.process("/mail/activation", context);
        mailClient.sendMail(user.getEmail(), "Adelaide CS Community - Activate your account", content);

        return map;
    }

    public int activate(int userId, String code) {
        User user = userMapper.selectById(userId);
        if (user.getStatus() == 1) {
            return ACTIVATION_REPEAT;
        } else if (user.getActivationCode().equals(code)) {
            userMapper.updateStatus(userId, 1);
            return ACTIVATION_SUCCESS;
        } else {
            return ACTIVATION_FAIL;
        }
    }

    public Map<String, Object> login(String username, String password, int expiredSeconds) {
        Map<String, Object> map = new HashMap<>();

        // Handle empty value
        if (StringUtils.isBlank(username)) {
            map.put("usernameMsg", "Account cannot be empty!");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("passwordMsg", "Password cannot be empty!");
            return map;
        }

        // Check the account
        User user = userMapper.selectByName(username);
        if (user == null) {
            map.put("usernameMsg", "The account does not exist!");
            return map;
        }

        // Check if the account is activated or not
        if (user.getStatus() == 0) {
            map.put("usernameMsg", "The account is not activated yet!");
            return map;
        }

        // Check the password
        password = CommunityUtil.md5(password + user.getSalt());
        if (!user.getPassword().equals(password)) {
            map.put("passwordMsg", "Password is incorrect!");
            return map;
        }

        // Generate a session for the user
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommunityUtil.generateUUID());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000));
        loginTicketMapper.insertLoginTicket(loginTicket);
        map.put("ticket", loginTicket.getTicket());
        return map;
    }

    public void logout(String ticket) {
        loginTicketMapper.updateStatus(ticket, 1);
    }

    public LoginTicket findLoginTicket(String ticket) {
        return loginTicketMapper.selectByTicket(ticket);
    }

    public int updateHeader(int userId, String headerURL) {
        return userMapper.updateHeader(userId, headerURL);
    }

    public Map<String, Object> changePassword(User user, String originalPassword, String newPassword, String confirmedPassword) {
        Map<String, Object> map = new HashMap<>();

        // Handle empty objects
        if (StringUtils.isBlank(originalPassword)) {
            map.put("originalPasswordMsg", "Please enter your original password!");
            return map;
        }
        if (StringUtils.isBlank(newPassword)) {
            map.put("newPasswordMsg", "Please enter your new password!");
            return map;
        }
        if (StringUtils.isBlank(confirmedPassword)) {
            map.put("confirmedPasswordMsg", "Please enter your confirmed password!");
            return map;
        }

        // Check if original password is correct or not
        String hashedOriginalPassword = CommunityUtil.md5(originalPassword + user.getSalt());
        if (!user.getPassword().equals(hashedOriginalPassword)) {
            map.put("originalPasswordMsg", "Original Password is incorrect!");
            return map;
        }

        // Check if new password is strong enough
        if (newPassword.length() < 8) {
            map.put("newPasswordMsg", "The length of your password cannot be less than 8!");
            return map;
        }

        // Check if new password is identical to confirmed password
        if (!newPassword.equals(confirmedPassword)) {
            map.put("confirmedPasswordMsg", "Please make sure the confirmed password is identical to the new password!");
            return map;
        }

        // Check if original password is the same as new password
        if (newPassword.equals(originalPassword)) {
            map.put("newPasswordMsg", "Please set a new password!");
            return map;
        }

        // Change the password
        String hashedNewPassword = CommunityUtil.md5(newPassword + user.getSalt());
        userMapper.updatePassword(user.getId(), hashedNewPassword);
        return map;
    }

    public User findUserByName(String username) {
        return userMapper.selectByName(username);
    }

}
