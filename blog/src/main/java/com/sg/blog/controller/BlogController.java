package com.sg.blog.controller;

import com.sg.blog.dao.PostRepository;
import com.sg.blog.dao.RoleRepository;
import com.sg.blog.dao.TagRepository;
import com.sg.blog.dao.UserRepository;
import com.sg.blog.entity.Post;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BlogController {

    @Autowired
    PostRepository postR;

    @Autowired
    RoleRepository roleR;

    @Autowired
    TagRepository tagR;

    @Autowired
    UserRepository userR;


    @GetMapping("posts")
    public String displayPosts(Model model){
        List<Post>posts = postR.findAll();
        model.addAttribute("posts", posts);
        return "posts";


    }



}
