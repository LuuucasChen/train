package com.lucas.train.member.controller;

import com.lucas.common.resp.CommonResp;
import com.lucas.train.member.req.MemberLoginReq;
import com.lucas.train.member.req.MemberRegisterReq;
import com.lucas.train.member.req.MemberSendCodeReq;
import com.lucas.train.member.resp.MemberLoginResp;
import com.lucas.train.member.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public CommonResp<Long> register(@Valid MemberRegisterReq registerReq) {
        long id = memberService.register(registerReq);
        CommonResp<Long> commonResp = new CommonResp<>();
        commonResp.setContent(id);
        return commonResp;
    }

    @PostMapping("/send-code")
    public CommonResp<Long> sendCode(@Valid @RequestBody MemberSendCodeReq req) {
        memberService.sendCode(req);
        return new CommonResp<>();
    }

    @PostMapping("/login")
    public CommonResp<MemberLoginResp> login(@Valid @RequestBody MemberLoginReq req) {
        MemberLoginResp memberLoginResp =  memberService.login(req);
        return new CommonResp<>(memberLoginResp);
    }
}
