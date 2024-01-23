package com.lucas.train.business.controller.admin;

import com.lucas.common.resp.CommonResp;
import com.lucas.common.resp.PageResp;
import com.lucas.train.business.req.TrainSeatQueryReq;
import com.lucas.train.business.req.TrainSeatSaveReq;
import com.lucas.train.business.resp.TrainSeatQueryResp;
import com.lucas.train.business.service.TrainSeatService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/train-seat")
public class Train_seatAdminController {
    @Autowired
    private TrainSeatService train_seatService;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public CommonResp<Object> save(@Valid @RequestBody TrainSeatSaveReq req) {
        train_seatService.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<TrainSeatQueryResp>> queryList(@Valid TrainSeatQueryReq req) {
        PageResp<TrainSeatQueryResp> train_seatQueryList = train_seatService.queryList(req);
        return new CommonResp<>(train_seatQueryList);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id) {
        train_seatService.delete(id);
        return new CommonResp<>();
    }
}
