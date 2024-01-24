package com.lucas.train.business.controller.admin;

import com.lucas.common.context.MemberLoginContext;
import com.lucas.common.resp.CommonResp;
import com.lucas.common.resp.PageResp;
import com.lucas.train.business.req.DailyTrainSeatQueryReq;
import com.lucas.train.business.req.DailyTrainSeatSaveReq;
import com.lucas.train.business.resp.DailyTrainSeatQueryResp;
import com.lucas.train.business.service.DailyTrainSeatService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/daily-train-seat")
public class DailyTrainSeatAdminController {
    @Autowired
    private DailyTrainSeatService dailyTrainSeatService;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public CommonResp<Object> save(@Valid @RequestBody DailyTrainSeatSaveReq req) {
        dailyTrainSeatService.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<DailyTrainSeatQueryResp>> queryList(@Valid DailyTrainSeatQueryReq req) {
        PageResp<DailyTrainSeatQueryResp> dailyTrainSeatQueryList = dailyTrainSeatService.queryList(req);
        return new CommonResp<>(dailyTrainSeatQueryList);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id) {
        dailyTrainSeatService.delete(id);
        return new CommonResp<>();
    }
}
