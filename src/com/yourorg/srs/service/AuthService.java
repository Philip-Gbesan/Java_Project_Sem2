package com.yourorg.srs.service;

import com.yourorg.srs.entity.User;

public interface AuthService {
    User login(String username, String password);
    boolean addUser(User user);
}
