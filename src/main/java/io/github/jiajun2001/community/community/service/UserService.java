package io.github.jiajun2001.community.community.service;

import io.github.jiajun2001.community.community.dao.UserMapper;
import io.github.jiajun2001.community.community.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User findUserById(int id) {
        return userMapper.selectById(id);
    }
}
