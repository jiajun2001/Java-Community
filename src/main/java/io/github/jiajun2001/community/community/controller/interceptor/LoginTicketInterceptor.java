package io.github.jiajun2001.community.community.controller.interceptor;

import io.github.jiajun2001.community.community.entity.LoginTicket;
import io.github.jiajun2001.community.community.entity.User;
import io.github.jiajun2001.community.community.service.UserService;
import io.github.jiajun2001.community.community.util.CookieUtil;
import io.github.jiajun2001.community.community.util.HostHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.Host;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

@Component
public class LoginTicketInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Get the cookie of the user
        String ticket = CookieUtil.getValue(request, "ticket");
        if (ticket != null) {
            // Find login ticket
            LoginTicket loginTicket = userService.findLoginTicket(ticket);
            // Validate the ticket
            if (loginTicket != null && loginTicket.getStatus() == 0 && loginTicket.getExpired().after(new Date())) {
                // Get the user
                User user = userService.findUserById(loginTicket.getUserId());
                // Hold the user
                hostHolder.setUser(user);
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if (user != null && modelAndView != null) {
            modelAndView.addObject("loginUser", user);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
    }
}
