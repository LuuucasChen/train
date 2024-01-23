package com.lucas.train.business.controller.admin;

import com.lucas.common.resp.CommonResp;
import com.lucas.common.resp.PageResp;
import com.lucas.train.business.req.TrainStationQueryReq;
import com.lucas.train.business.req.TrainStationSaveReq;
import com.lucas.train.business.resp.TrainStationQueryResp;
import com.lucas.train.business.service.TrainStationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/train-station")
public class Train_stationAdminController {
    @Autowired
    private TrainStationService train_stationService;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public CommonResp<Object> save(@Valid @RequestBody TrainStationSaveReq req) {
        train_stationService.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<TrainStationQueryResp>> queryList(@Valid TrainStationQueryReq req) {
        PageResp<TrainStationQueryResp> train_stationQueryList = train_stationService.queryList(req);
        return new CommonResp<>(train_stationQueryList);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id) {
        train_stationService.delete(id);
        return new CommonResp<>();
    }
}
