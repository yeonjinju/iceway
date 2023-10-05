package com.delivery.iceway.user;

import org.apache.ibatis.annotations.Mapper;

import com.delivery.iceway.domain.User;

@Mapper
public interface UserMapper {
    User findByUserName(String userName);
    void insertAll(User user);
    void resetAdmin();
}
