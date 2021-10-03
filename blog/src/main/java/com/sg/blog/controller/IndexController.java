package com.sg.blog.controller;

import com.sg.blog.dao.PostRepository;
import com.sg.blog.entity.Post;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @Autowired
    PostRepository postRepository;

    @GetMapping("/")
    public String showIndex(Model model) {
        List<Post> posts = postRepository.findByApprovedTrue();

        posts = posts.stream()
                // This is a bit of a cludge to avoid a more complex DB query
                .filter(p ->
                        p.getStartdate().isBefore(LocalDate.now()) && p.getEnddate().isAfter(LocalDate.now()))
                .sorted(Comparator.comparing(Post::getTimestamp)
                        .reversed())
                .collect(Collectors.toList());

        model.addAttribute("posts", posts);

        return "index";
    }
}
