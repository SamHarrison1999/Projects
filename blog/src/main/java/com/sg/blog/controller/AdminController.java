package com.sg.blog.controller;

import com.sg.blog.dao.PostRepository;
import com.sg.blog.dao.RoleRepository;
import com.sg.blog.dao.UserRepository;
import com.sg.blog.entity.Post;
import com.sg.blog.entity.Role;
import com.sg.blog.entity.User;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdminController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @GetMapping("/admin")
    String showAdminPage(Model model) {
        List<User> users = userRepository.findAll();
        List<Post> posts = postRepository.findByApprovedFalse();

        posts = posts.stream()
                .sorted(Comparator.comparing(Post::getTimestamp)
                        .reversed())
                .collect(Collectors.toList());

        // Don't display our 'user deleted' placeholder.
        users.remove(0);

        List<Post> scheduledPosts = postRepository.findByApprovedTrue();

        scheduledPosts = scheduledPosts.stream()
                // This is a bit of a cludge to avoid a more complex DB query
                .filter(p
                        -> p.getStartdate().isAfter(LocalDate.now()))
                .sorted(Comparator.comparing(Post::getTimestamp)
                        .reversed())
                .collect(Collectors.toList());

        model.addAttribute("users", users);
        model.addAttribute("posts", posts);
        model.addAttribute("scheduledPosts", scheduledPosts);

        return "admin";
    }

    @GetMapping("/editUser")
    String showEditPage(HttpServletRequest request, Model model, Integer error) {
        int id = Integer.parseInt(request.getParameter("id"));

        User user = userRepository.findById(id).orElse(new User());
        List<Role> roles = roleRepository.findAll();

        model.addAttribute("user", user);
        model.addAttribute("roles", roles);

        if (error != null) {
            if (error == 1) {
                model.addAttribute("error", "Passwords did not match, password was not updated.");
            }
        }

        return "editUser";
    }

    @GetMapping("/createUser")
    String showCreatePage(Model model, Integer error) {
        List<Role> roles = roleRepository.findAll();

        model.addAttribute("roles", roles);

        if (error != null) {
            if (error == 1) {
                model.addAttribute("error", "Passwords did not match, password was not updated.");
            }
        }

        return "createUser";
    }

    @PostMapping("/createUser")
    String createUser(User user, HttpServletRequest request, String passwordSubmission, String confirmPassword) {

        if (!passwordSubmission.equals(confirmPassword)) {
            return "redirect:/createUser?id=" + user.getId() + "&error=1";
        }

        user.setPassword(encoder.encode(passwordSubmission));

        String[] roleStrings = request.getParameterValues("role");

        if (roleStrings.length > 0) {
            Set<Role> roles = Arrays.stream(roleStrings)
                    .map(v -> roleRepository.findById(Integer.parseInt(v)).orElse(new Role()))
                    .collect(Collectors.toSet());

            user.setRoles(roles);
        }

        userRepository.save(user);

        return "redirect:/admin";
    }

    @PostMapping("/editUser")
    String editUser(User user, HttpServletRequest request, String passwordSubmission, String confirmPassword) {
        if (!passwordSubmission.equals("")) {
            if (!passwordSubmission.equals(confirmPassword)) {
                return "redirect:/editUser?id=" + user.getId() + "&error=1";
            }

            user.setPassword(encoder.encode(passwordSubmission));
        } else {
            User oldUser = userRepository.findById(user.getId()).orElse(user);
            user.setPassword(oldUser.getPassword());
        }

        String[] roleStrings = request.getParameterValues("role");

        if (roleStrings != null) {
            Set<Role> roles = Arrays.stream(roleStrings)
                    .map(v -> roleRepository.findById(Integer.parseInt(v)).orElse(new Role()))
                    .collect(Collectors.toSet());

            user.setRoles(roles);
        }
        userRepository.save(user);

        return "redirect:/admin";
    }

    @GetMapping("/deleteUserAndPosts")
    @Transactional
    String deleteUserAndPosts(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));

        User user = userRepository.findById(id).orElse(new User());

        List<Post> posts = postRepository.findByUser(user);

        for (Post post : posts) {
            postRepository.delete(post);
        }

        userRepository.delete(user);

        return "redirect:/admin";
    }

    @GetMapping("/deleteUserAndUpdatePosts")
    @Transactional
    String deleteUserAndUpdatePosts(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));

        User user = userRepository.findById(id).orElse(new User());

        List<Post> posts = postRepository.findByUser(user);

        User blankUser = new User();

        // Update db script to make first user 'user unknown'
        blankUser.setId(1);

        for (Post post : posts) {
            post.setUser(blankUser);
            postRepository.save(post);
        }

        userRepository.delete(user);

        return "redirect:/admin";
    }

}
