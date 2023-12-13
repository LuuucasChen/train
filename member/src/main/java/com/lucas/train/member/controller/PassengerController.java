package com.lucas.train.member.controller;

import com.lucas.common.context.MemberLoginContext;
import com.lucas.common.resp.CommonResp;
import com.lucas.common.resp.PageResp;
import com.lucas.train.member.req.PassengerQueryReq;
import com.lucas.train.member.req.PassengerSaveReq;
import com.lucas.train.member.resp.PassengerQueryResp;
import com.lucas.train.member.service.PassengerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/passenger")
public class PassengerController {
    @Autowired
    private PassengerService passengerService;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public CommonResp<Object> save(@Valid @RequestBody PassengerSaveReq req) {
        passengerService.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
        public CommonResp<PageResp<PassengerQueryResp>> queryList(@Valid PassengerQueryReq req) {
        req.setMemberId(MemberLoginContext.getId());
        PageResp<PassengerQueryResp> passengerQueryList = passengerService.queryList(req);
        return new CommonResp<>(passengerQueryList);
    }
}
