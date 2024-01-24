package com.lucas.train.business.controller.admin;

import com.lucas.common.context.MemberLoginContext;
import com.lucas.common.resp.CommonResp;
import com.lucas.common.resp.PageResp;
import com.lucas.train.business.req.DailyTrainCarriageQueryReq;
import com.lucas.train.business.req.DailyTrainCarriageSaveReq;
import com.lucas.train.business.resp.DailyTrainCarriageQueryResp;
import com.lucas.train.business.service.DailyTrainCarriageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/daily-train-carriage")
public class DailyTrainCarriageAdminController {
    @Autowired
    private DailyTrainCarriageService dailyTrainCarriageService;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public CommonResp<Object> save(@Valid @RequestBody DailyTrainCarriageSaveReq req) {
        dailyTrainCarriageService.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<DailyTrainCarriageQueryResp>> queryList(@Valid DailyTrainCarriageQueryReq req) {
        PageResp<DailyTrainCarriageQueryResp> dailyTrainCarriageQueryList = dailyTrainCarriageService.queryList(req);
        return new CommonResp<>(dailyTrainCarriageQueryList);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id) {
        dailyTrainCarriageService.delete(id);
        return new CommonResp<>();
    }
}
