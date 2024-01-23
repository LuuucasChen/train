package com.lucas.train.business.controller.admin;

import com.lucas.common.context.MemberLoginContext;
import com.lucas.common.resp.CommonResp;
import com.lucas.common.resp.PageResp;
import com.lucas.train.business.req.Train_seatQueryReq;
import com.lucas.train.business.req.Train_seatSaveReq;
import com.lucas.train.business.resp.Train_seatQueryResp;
import com.lucas.train.business.service.Train_seatService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/train-seat")
public class Train_seatAdminController {
    @Autowired
    private Train_seatService train_seatService;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public CommonResp<Object> save(@Valid @RequestBody Train_seatSaveReq req) {
        train_seatService.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<Train_seatQueryResp>> queryList(@Valid Train_seatQueryReq req) {
        PageResp<Train_seatQueryResp> train_seatQueryList = train_seatService.queryList(req);
        return new CommonResp<>(train_seatQueryList);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id) {
        train_seatService.delete(id);
        return new CommonResp<>();
    }
}
