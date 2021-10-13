package com.applepieme.user.service;

import com.applepieme.user.entity.Member;
import com.applepieme.user.entity.vo.LoginVo;
import com.applepieme.user.entity.vo.RegisterVo;
import com.applepieme.vo.MemberVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author applepieme
 * @since 2021-10-06
 */
public interface MemberService extends IService<Member> {
    /**
     * 获取某一天的注册人数
     *
     * @param day 日期
     * @return 注册人数
     */
    Integer getRegisterNumByDay(String day);

    /**
     * 根据用户 id 获取用户信息
     *
     * @param id 用户id
     * @return 用于前台显示的用户信息对象
     */
    MemberVo getMemberVoById(String id);

    /**
     * 用户登录
     *
     * @param loginVo 登录对象
     * @return 自包含 token
     */
    String userLogin(LoginVo loginVo);

    /**
     * 用户注册
     *
     * @param registerVo 注册对象
     */
    void userRegister(RegisterVo registerVo);

    /**
     * 根据 openid 获取用户
     *
     * @param openid openid
     * @return 用户
     */
    Member getMemberByOpenid(String openid);
}
