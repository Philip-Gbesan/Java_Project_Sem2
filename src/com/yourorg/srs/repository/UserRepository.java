package com.yourorg.srs.repository;

import com.yourorg.srs.entity.User;

public interface UserRepository {
    User findByUsername(String username);
    boolean createUser(User user);
}
