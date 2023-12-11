package com.lucas.train.member.service;

import cn.hutool.core.collection.CollUtil;
import com.lucas.common.exception.BusinessException;
import com.lucas.common.exception.BusinessExceptionEnum;
import com.lucas.common.util.SnowUtil;
import com.lucas.train.member.domain.Member;
import com.lucas.train.member.domain.MemberExample;
import com.lucas.train.member.mapper.MemberMapper;
import com.lucas.train.member.req.MemberRegisterReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {
    @Autowired
    private MemberMapper memberMapper;

    public int count() {
        return (int) memberMapper.countByExample(null);
    }

    public long register(MemberRegisterReq req) {
        String mobile = req.getMobile();
        MemberExample memberExample = new MemberExample();
        memberExample.createCriteria().andMobileEqualTo(mobile);
        List<Member> list = memberMapper.selectByExample(memberExample);

        if (CollUtil.isNotEmpty(list)) {
//            return list.get(0).getId();
            throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_EXIST);
        }

        Member member = new Member();
        member.setId(SnowUtil.getSnowflakeNextId());
        member.setMobile(mobile);

        memberMapper.insert(member);
        return member.getId();
    }
}
