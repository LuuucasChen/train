package com.lucas.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lucas.common.resp.PageResp;
import com.lucas.common.util.SnowUtil;
import com.lucas.train.business.domain.Train_carriage;
import com.lucas.train.business.domain.Train_carriageExample;
import com.lucas.train.business.mapper.Train_carriageMapper;
import com.lucas.train.business.req.Train_carriageQueryReq;
import com.lucas.train.business.req.Train_carriageSaveReq;
import com.lucas.train.business.resp.Train_carriageQueryResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Train_carriageService {

    @Autowired
    private Train_carriageMapper train_carriageMapper;

    public void save(Train_carriageSaveReq req) {
        DateTime now = DateTime.now();
        Train_carriage train_carriage = BeanUtil.copyProperties(req, Train_carriage.class);
        if (ObjectUtil.isNull(train_carriage.getId())) {
            train_carriage.setId(SnowUtil.getSnowflakeNextId());
            train_carriage.setCreateTime(now);
            train_carriage.setUpdateTime(now);
            train_carriageMapper.insert(train_carriage);
        } else {
            train_carriage.setUpdateTime(now);
            train_carriageMapper.updateByPrimaryKey(train_carriage);
        }
    }

    public PageResp<Train_carriageQueryResp> queryList(Train_carriageQueryReq req) {
        Train_carriageExample train_carriage = new Train_carriageExample();
        train_carriage.setOrderByClause("id desc");
        Train_carriageExample.Criteria criteria = train_carriage.createCriteria();

        PageHelper.startPage(req.getPage(), req.getSize());
        List<Train_carriage> train_carriages = train_carriageMapper.selectByExample(train_carriage);
        List<Train_carriageQueryResp> train_carriageQueryList = BeanUtil.copyToList(train_carriages, Train_carriageQueryResp.class);

        PageResp<Train_carriageQueryResp> resp = new PageResp<>();
        resp.setList(train_carriageQueryList);

        PageInfo<Train_carriage> pageInfo = new PageInfo<>(train_carriages);
        resp.setTotal(pageInfo.getTotal());

        return resp;
    }

    public void delete(Long id) {
        train_carriageMapper.deleteByPrimaryKey(id);
    }

}
