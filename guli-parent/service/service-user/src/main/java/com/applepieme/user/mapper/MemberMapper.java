package com.applepieme.user.mapper;

import com.applepieme.user.entity.Member;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author applepieme
 * @since 2021-10-06
 */
public interface MemberMapper extends BaseMapper<Member> {
    Integer getRegisterNumByDay(@Param("day") String day);
}
