package com.yourorg.srs.serviceimpl;

import com.yourorg.srs.entity.User;
import com.yourorg.srs.repository.UserRepository;
import com.yourorg.srs.service.AuthService;
import com.yourorg.srs.util.Security;

public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepo = new UserRepositoryImpl();

    @Override
    public User login(String username, String password) {
        User u = userRepo.findByUsername(username);
        if (u == null) return null;
        if (Security.matches(password, u.getPassword())) {
            return u;
        }
        return null;
    }

    @Override
    public boolean addUser(User user) {
        // Encode the plain password before storing
        user.setPassword(Security.encodePassword(user.getPassword()));
        return userRepo.createUser(user);
    }
}
