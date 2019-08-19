package com.app.service;

import com.app.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
  User save(User user);
  void delete(String id);
  List<User> findAll();
  User findById(String id);
  User findByEmail(String email);
  User findByName(String name);
}
