package com.lucas.train.business.controller.admin;

import com.lucas.common.resp.CommonResp;
import com.lucas.common.resp.PageResp;
import com.lucas.train.business.req.TrainCarriageQueryReq;
import com.lucas.train.business.req.TrainCarriageSaveReq;
import com.lucas.train.business.resp.TrainCarriageQueryResp;
import com.lucas.train.business.service.TrainCarriageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/train-carriage")
public class Train_carriageAdminController {
    @Autowired
    private TrainCarriageService train_carriageService;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public CommonResp<Object> save(@Valid @RequestBody TrainCarriageSaveReq req) {
        train_carriageService.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<TrainCarriageQueryResp>> queryList(@Valid TrainCarriageQueryReq req) {
        PageResp<TrainCarriageQueryResp> train_carriageQueryList = train_carriageService.queryList(req);
        return new CommonResp<>(train_carriageQueryList);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id) {
        train_carriageService.delete(id);
        return new CommonResp<>();
    }
}
