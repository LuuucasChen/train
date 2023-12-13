package com.lucas.train.${module}.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lucas.train.common.resp.PageResp;
import com.lucas.train.common.util.SnowUtil;
import com.lucas.train.${module}.domain.${Domain};
import com.lucas.train.${module}.domain.${Domain}Example;
import com.lucas.train.${module}.mapper.${Domain}Mapper;
import com.lucas.train.${module}.req.${Domain}QueryReq;
import com.lucas.train.${module}.req.${Domain}SaveReq;
import com.lucas.train.${module}.resp.${Domain}QueryResp;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public class ${Domain}Service {

    @Autowired
    private ${Domain}Mapper ${domain}Mapper;

    public void save(${Domain}SaveReq req) {
        DateTime now = DateTime.now();
        ${Domain} ${domain} = BeanUtil.copyProperties(req, ${Domain}.class);
        if (ObjectUtil.isNull(${domain}.getId())) {
            ${domain}.setId(SnowUtil.getSnowflakeNextId());
            ${domain}.setCreateTime(now);
            ${domain}.setUpdateTime(now);
            ${domain}Mapper.insert(${domain});
        } else {
            ${domain}.setUpdateTime(now);
            ${domain}Mapper.updateByPrimaryKey(${domain});
        }
    }

    public PageResp<${Domain}QueryResp> queryList(${Domain}QueryReq req) {
        ${Domain}Example ${domain} = new ${Domain}Example();
        ${domain}.setOrderByClause("id desc");
        ${Domain}Example.Criteria criteria = ${domain}.createCriteria();

        PageHelper.startPage(req.getPage(), req.getSize());
        List<${Domain}> ${domain}s = ${domain}Mapper.selectByExample(${domain});
        List<${Domain}QueryResp> ${domain}QueryList = BeanUtil.copyToList(${domain}s, ${Domain}QueryResp.class);

        PageResp<${Domain}QueryResp> resp = new PageResp<>();
        resp.setList(${domain}QueryList);

        PageInfo<${Domain}> pageInfo = new PageInfo<>(${domain}s);
        resp.setTotal(pageInfo.getTotal());

        return resp;
    }

    public void delete(Long id) {
        ${domain}Mapper.deleteByPrimaryKey(id);
    }

}
