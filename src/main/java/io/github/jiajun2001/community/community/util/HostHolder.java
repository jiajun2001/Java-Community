package io.github.jiajun2001.community.community.util;

import io.github.jiajun2001.community.community.entity.User;
import org.springframework.stereotype.Component;

// Hold the information of the user
@Component
public class HostHolder {

    private ThreadLocal<User> users = new ThreadLocal<>();

    public void setUser(User user) {
        users.set(user);
    }

    public User getUser() {
        return users.get();
    }

    public void clear() {
        users.remove();
    }

}
