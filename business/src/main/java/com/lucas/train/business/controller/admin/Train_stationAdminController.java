package com.lucas.train.business.controller.admin;

import com.lucas.common.resp.CommonResp;
import com.lucas.common.resp.PageResp;
import com.lucas.train.business.req.Train_stationQueryReq;
import com.lucas.train.business.req.Train_stationSaveReq;
import com.lucas.train.business.resp.Train_stationQueryResp;
import com.lucas.train.business.service.Train_stationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/train-station")
public class Train_stationAdminController {
    @Autowired
    private Train_stationService train_stationService;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public CommonResp<Object> save(@Valid @RequestBody Train_stationSaveReq req) {
        train_stationService.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<Train_stationQueryResp>> queryList(@Valid Train_stationQueryReq req) {
        PageResp<Train_stationQueryResp> train_stationQueryList = train_stationService.queryList(req);
        return new CommonResp<>(train_stationQueryList);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id) {
        train_stationService.delete(id);
        return new CommonResp<>();
    }
}
