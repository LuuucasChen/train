package com.lucas.train.${module}.controller;

import com.lucas.common.context.MemberLoginContext;
import com.lucas.common.resp.CommonResp;
import com.lucas.common.resp.PageResp;
import com.lucas.train.${module}.req.${Domain}QueryReq;
import com.lucas.train.${module}.req.${Domain}SaveReq;
import com.lucas.train.${module}.resp.${Domain}QueryResp;
import com.lucas.train.${module}.service.${Domain}Service;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/${do_main}")
public class ${Domain}Controller {
    @Autowired
    private ${Domain}Service ${domain}Service;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public CommonResp<Object> save(@Valid @RequestBody ${Domain}SaveReq req) {
        ${domain}Service.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<${Domain}QueryResp>> queryList(@Valid ${Domain}QueryReq req) {
        req.setMemberId(MemberLoginContext.getId());
        PageResp<${Domain}QueryResp> ${domain}QueryList = ${domain}Service.queryList(req);
        return new CommonResp<>(${domain}QueryList);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id) {
        ${domain}Service.delete(id);
        return new CommonResp<>();
    }
}
