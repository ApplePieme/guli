package com.applepieme.user.controller;


import com.applepieme.user.entity.vo.LoginVo;
import com.applepieme.user.entity.vo.RegisterVo;
import com.applepieme.user.service.MemberService;
import com.applepieme.util.JwtUtils;
import com.applepieme.util.R;
import com.applepieme.vo.MemberVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author applepieme
 * @since 2021-10-06
 */
@RestController
@RequestMapping("/user/member")
public class MemberController {
    @Resource
    private MemberService memberService;

    @ApiOperation(value = "获取某一天的用户注册数")
    @GetMapping("/register_num/{day}")
    public Integer getRegisterNumByDay(@PathVariable("day") String day) {
        return memberService.getRegisterNumByDay(day);
    }

    @ApiOperation(value = "根据用户id获取用户信息")
    @GetMapping("/{id}")
    public MemberVo getMemberVoById(@PathVariable("id") String id) {
        return memberService.getMemberVoById(id);
    }

    @ApiOperation(value = "会员登录")
    @PostMapping("/login")
    public R userLogin(@RequestBody LoginVo loginVo) {
        String token = memberService.userLogin(loginVo);
        return R.ok().data("token", token).message("登录成功");
    }

    @ApiOperation(value = "会员注册")
    @PostMapping("/register")
    public R userRegister(@RequestBody RegisterVo registerVo) {
        memberService.userRegister(registerVo);
        return R.ok().message("注册成功");
    }

    @ApiOperation(value = "根据 token 获取用户信息")
    @GetMapping("/info")
    public R getUserInfoByToken(HttpServletRequest request) {
        String id = JwtUtils.getMemberIdByJwtToken(request);
        MemberVo memberVo = memberService.getMemberVoById(id);
        return R.ok().data("item", memberVo);
    }

}

