package com.lucas.train.business.controller.admin;

import com.lucas.common.context.MemberLoginContext;
import com.lucas.common.resp.CommonResp;
import com.lucas.common.resp.PageResp;
import com.lucas.train.business.req.Train_carriageQueryReq;
import com.lucas.train.business.req.Train_carriageSaveReq;
import com.lucas.train.business.resp.Train_carriageQueryResp;
import com.lucas.train.business.service.Train_carriageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/train-carriage")
public class Train_carriageAdminController {
    @Autowired
    private Train_carriageService train_carriageService;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public CommonResp<Object> save(@Valid @RequestBody Train_carriageSaveReq req) {
        train_carriageService.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<Train_carriageQueryResp>> queryList(@Valid Train_carriageQueryReq req) {
        PageResp<Train_carriageQueryResp> train_carriageQueryList = train_carriageService.queryList(req);
        return new CommonResp<>(train_carriageQueryList);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id) {
        train_carriageService.delete(id);
        return new CommonResp<>();
    }
}
