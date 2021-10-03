package com.sg.blog.controller;

import com.sg.blog.dao.PostRepository;
import com.sg.blog.dao.TagRepository;
import com.sg.blog.dao.UserRepository;
import com.sg.blog.entity.Post;
import com.sg.blog.entity.Tag;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class PostController {

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TagRepository tagRepository;

    @GetMapping("/post")
    String displayPostPage(Model model) {
        return "post";
    }

    @GetMapping("/edit")
    String editPost(HttpServletRequest request, Model model) {
        int id = Integer.parseInt(request.getParameter("id"));

        Post post = postRepository.findById(id).orElse(new Post());
        StringBuilder tags = new StringBuilder();

        for(Tag tag : post.getTags()) {
            tags.append(tag.getName()).append(", ");
        }

        model.addAttribute("post", post);
        model.addAttribute("tags", tags.toString());

        return "editPost";
    }

    @GetMapping("/delete")
    @Transactional
    String deletePost(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));

        Post post = postRepository.findById(id).orElse(new Post());

        if(post.getId() != 0) {
            postRepository.delete(post);
        }

        return "redirect:/admin";
    }

    @GetMapping("/approve")
    @Transactional
    String approvePost(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));

        Post post = postRepository.findById(id).orElse(new Post());

        if(post.getId() != 0) {
            post.setApproved(true);
            postRepository.save(post);
        }

        return "redirect:/admin";
    }


    @PostMapping(value = {"/post", "/edit"})
    String createPost(Post post, HttpServletRequest request) {
        // Note that this is using the spring security user, not ours
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = user.getUsername();

        if(request.getParameter("id") != null) {
            post.setId(Integer.parseInt(request.getParameter("id")));
        }

        post.setUser(userRepository.findByUsername(name).get(0));

        if(post.getStartdate() == null) {
            post.setStartdate(LocalDate.of(1000, 1, 1));
        }

        if(post.getEnddate() == null) {
            post.setEnddate(LocalDate.of(9999, 12, 31));
        }

        // Process string of comma-separated tags into list of Tags
        String tagString = request.getParameter("tagString");
        tagString = tagString.replace(" ", "");
        String [] tagArr = tagString.split(",");
        Set<Tag> tags = new HashSet<>();
        for(String tagName : tagArr) {
            Tag tag = new Tag();
            tag.setName(tagName);
            tagRepository.save(tag);
            tags.add(tag);
        }

        post.setTags(tags);

        postRepository.save(post);

        return "redirect:/";
    }

}
