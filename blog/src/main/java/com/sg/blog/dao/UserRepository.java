package com.sg.blog.dao;

import com.sg.blog.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
    List<User> findByUsername(String username);
}
