package com.lucas.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lucas.common.resp.PageResp;
import com.lucas.common.util.SnowUtil;
import com.lucas.train.business.domain.Train;
import com.lucas.train.business.domain.TrainExample;
import com.lucas.train.business.mapper.TrainMapper;
import com.lucas.train.business.req.TrainQueryReq;
import com.lucas.train.business.req.TrainSaveReq;
import com.lucas.train.business.resp.TrainQueryResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainService {

    @Autowired
    private TrainMapper trainMapper;

    public void save(TrainSaveReq req) {
        DateTime now = DateTime.now();
        Train train = BeanUtil.copyProperties(req, Train.class);
        if (ObjectUtil.isNull(train.getId())) {
            train.setId(SnowUtil.getSnowflakeNextId());
            train.setCreateTime(now);
            train.setUpdateTime(now);
            trainMapper.insert(train);
        } else {
            train.setUpdateTime(now);
            trainMapper.updateByPrimaryKey(train);
        }
    }

    public PageResp<TrainQueryResp> queryList(TrainQueryReq req) {
        TrainExample train = new TrainExample();
        train.setOrderByClause("id desc");
        TrainExample.Criteria criteria = train.createCriteria();

        PageHelper.startPage(req.getPage(), req.getSize());
        List<Train> trains = trainMapper.selectByExample(train);
        List<TrainQueryResp> trainQueryList = BeanUtil.copyToList(trains, TrainQueryResp.class);

        PageResp<TrainQueryResp> resp = new PageResp<>();
        resp.setList(trainQueryList);

        PageInfo<Train> pageInfo = new PageInfo<>(trains);
        resp.setTotal(pageInfo.getTotal());

        return resp;
    }

    public List<TrainQueryResp> queryAll() {
        TrainExample train = new TrainExample();
        train.setOrderByClause("id desc");
        List<Train> trains = trainMapper.selectByExample(train);
        List<TrainQueryResp> trainQueryList = BeanUtil.copyToList(trains, TrainQueryResp.class);

        return trainQueryList;
    }

    public void delete(Long id) {
        trainMapper.deleteByPrimaryKey(id);
    }

}
