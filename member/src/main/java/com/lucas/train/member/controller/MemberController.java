package com.lucas.train.member.controller;

import com.lucas.common.resp.CommonResp;
import com.lucas.train.member.req.MemberRegisterReq;
import com.lucas.train.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @GetMapping("/count")
    public CommonResp<Integer> hello() {
        int count =  memberService.count();
        CommonResp<Integer> commonResp = new CommonResp<>();
        commonResp.setContent(count);
        return commonResp;
    }

    @PostMapping("/register")
    public CommonResp<Long> register(MemberRegisterReq registerReq) {
        long id = memberService.register(registerReq);
        CommonResp<Long> commonResp = new CommonResp<>();
        commonResp.setContent(id);
        return commonResp;
    }
}
