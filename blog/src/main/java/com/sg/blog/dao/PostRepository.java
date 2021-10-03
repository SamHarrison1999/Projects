package com.sg.blog.dao;

import com.sg.blog.entity.Post;
import com.sg.blog.entity.Tag;
import com.sg.blog.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer>{
    List<Post> findByApprovedFalse();
    List<Post> findByApprovedTrue();
    List<Post> findByTagsContaining(Tag tag);
    List<Post> findByApprovedTrueAndTagsContainingOrderByTimestamp(Tag tag);
    List<Post> findByUser(User user);
}