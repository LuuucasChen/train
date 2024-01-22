package com.lucas.train.business.controller.admin;

import com.lucas.common.resp.CommonResp;
import com.lucas.common.resp.PageResp;
import com.lucas.train.business.req.StationQueryReq;
import com.lucas.train.business.req.StationSaveReq;
import com.lucas.train.business.resp.StationQueryResp;
import com.lucas.train.business.service.StationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/station")
public class StationAdminController {
    @Autowired
    private StationService stationService;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public CommonResp<Object> save(@Valid @RequestBody StationSaveReq req) {
        stationService.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<StationQueryResp>> queryList(@Valid StationQueryReq req) {
        PageResp<StationQueryResp> stationQueryList = stationService.queryList(req);
        return new CommonResp<>(stationQueryList);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id) {
        stationService.delete(id);
        return new CommonResp<>();
    }
}
