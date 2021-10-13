package com.applepieme.edu.controller;

import com.applepieme.util.R;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author applepieme
 * @date 2021/10/1 15:39
 */
@RestController
@RequestMapping("/edu/user")
public class LoginController {
    @PostMapping("/login")
    public R login() {
        return R.ok().data("token", UUID.randomUUID().toString());
    }

    @GetMapping("/info")
    public R info() {
        return R.ok().data("roles", "[admin]").data("name", "admin")
                .data("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
    }
}
