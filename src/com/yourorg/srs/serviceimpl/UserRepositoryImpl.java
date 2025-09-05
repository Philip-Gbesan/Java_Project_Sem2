package com.yourorg.srs.serviceimpl;

import com.yourorg.srs.entity.User;
import com.yourorg.srs.repository.UserRepository;
import com.yourorg.srs.util.Db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserRepositoryImpl implements UserRepository {

    @Override
    public User findByUsername(String username) {
        String sql = "SELECT id, username, password, role, full_name, email FROM users WHERE username=?";
        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User u = new User();
                    u.setId(rs.getInt("id"));
                    u.setUsername(rs.getString("username"));
                    u.setPassword(rs.getString("password"));
                    u.setRole(rs.getString("role"));
                    u.setFullName(rs.getString("full_name"));
                    u.setEmail(rs.getString("email"));
                    return u;
                }
            }
        } catch (Exception e) {
            System.err.println("Error findByUsername: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean createUser(User user) {
        String sql = "INSERT INTO users (username, password, role, full_name, email) VALUES (?,?,?,?,?)";
        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole());
            ps.setString(4, user.getFullName());
            ps.setString(5, user.getEmail());
            return ps.executeUpdate() == 1;
        } catch (Exception e) {
            System.err.println("Error createUser: " + e.getMessage());
            return false;
        }
    }
}
