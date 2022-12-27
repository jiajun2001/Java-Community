package io.github.jiajun2001.community.community.dao;

import io.github.jiajun2001.community.community.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    User selectById(int id);

    User selectByName(String username);

    User selectByEmail(String email);

    int insertUser(User user);

    int updateStatus(int id, int status);

    int updateHeader(int id, String headerURL);

    int updatePassword(int id, String password);

}