package com.lucas.train.member.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lucas.common.context.MemberLoginContext;
import com.lucas.common.resp.PageResp;
import com.lucas.common.util.SnowUtil;
import com.lucas.train.member.domain.${Domain};
import com.lucas.train.member.domain.${Domain}Example;
import com.lucas.train.member.mapper.${Domain}Mapper;
import com.lucas.train.member.req.${Domain}QueryReq;
import com.lucas.train.member.req.${Domain}SaveReq;
import com.lucas.train.member.resp.${Domain}QueryResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ${Domain}Service {

    @Autowired
    private ${Domain}Mapper ${domain}Mapper;

    public void save(${Domain}SaveReq req) {
        Date date = new Date();
        ${Domain} ${domain} = new ${Domain}();
        if (ObjectUtil.isNull(req.getId())) {
            //新增乘车人
            ${domain}.setId(SnowUtil.getSnowflakeNextId());
            ${domain}.setCreateTime(date);
            ${domain}.setUpdateTime(date);
            ${domain}.setMemberId(MemberLoginContext.getId());
            ${domain}.setName(req.getName());
            ${domain}.setIdCard(req.getIdCard());
            ${domain}.setType(req.getType());
            ${domain}Mapper.insert(${domain});
        } else {
            //更新乘车人
            ${domain}.setId(req.getId());
            ${domain}.setMemberId(MemberLoginContext.getId());
            ${domain}.setUpdateTime(date);
            ${domain}.setName(req.getName());
            ${domain}.setIdCard(req.getIdCard());
            ${domain}.setType(req.getType());
            ${domain}Mapper.updateByPrimaryKeySelective(${domain});
        }
    }

    public PageResp<${Domain}QueryResp> queryList(${Domain}QueryReq req) {
        ${Domain}Example ${domain} = new ${Domain}Example();
        ${domain}.setOrderByClause("id desc");
        ${Domain}Example.Criteria criteria = ${domain}.createCriteria();
        if (ObjectUtil.isNotNull(req.getMemberId())) {
            criteria.andMemberIdEqualTo(req.getMemberId());
        }
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
