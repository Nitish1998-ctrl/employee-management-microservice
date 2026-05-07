package com.example.Employee;

import com.example.Util.JwtUtil;
import com.example.models.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(UserService userService,
                          BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder= passwordEncoder;
    }

    @PostMapping("/login")
    public String login(@RequestBody User request) {

        User user = userService.getUserByUsername(request.getUsername());

        if (user == null) {

            User newUser = new User();
            newUser.setUsername(request.getUsername());
            newUser.setPassword(passwordEncoder.encode(request.getPassword()));

            userService.saveUser(newUser);

            return JwtUtil.generateToken(newUser.getUsername());
        }

        boolean match = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        );

        if (!match) {
            return "Invalid username or password";
        }
        return JwtUtil.generateToken(user.getUsername());
    }
}
