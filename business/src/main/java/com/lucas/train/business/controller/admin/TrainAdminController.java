package com.lucas.train.business.controller.admin;

import com.lucas.common.resp.CommonResp;
import com.lucas.common.resp.PageResp;
import com.lucas.train.business.req.TrainQueryReq;
import com.lucas.train.business.req.TrainSaveReq;
import com.lucas.train.business.resp.StationQueryResp;
import com.lucas.train.business.resp.TrainQueryResp;
import com.lucas.train.business.service.TrainService;
import com.lucas.train.business.service.Train_seatService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/train")
public class TrainAdminController {
    @Autowired
    private TrainService trainService;
    @Autowired
    private Train_seatService trainSeatService;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public CommonResp<Object> save(@Valid @RequestBody TrainSaveReq req) {
        trainService.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<TrainQueryResp>> queryList(@Valid TrainQueryReq req) {
        PageResp<TrainQueryResp> trainQueryList = trainService.queryList(req);
        return new CommonResp<>(trainQueryList);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id) {
        trainService.delete(id);
        return new CommonResp<>();
    }
    @GetMapping("/query-all")
    public CommonResp<List<TrainQueryResp>> queryList() {
        List<TrainQueryResp> trainQueryList = trainService.queryAll();
        return new CommonResp<>(trainQueryList);
    }

    @GetMapping("/gen-seat/{trainCode}")
    public CommonResp<List<StationQueryResp>> genSeat(@PathVariable String trainCode) {
        trainSeatService.genTrainSeat(trainCode);
        return new CommonResp<>();
    }

}
