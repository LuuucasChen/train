package com.lucas.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lucas.common.exception.BusinessException;
import com.lucas.common.exception.BusinessExceptionEnum;
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
            // 保存之前，先校验唯一键是否存在
            Train trainDB = selectByUnique(req.getCode());
            if (ObjectUtil.isNotEmpty(trainDB)) {
                throw new BusinessException(BusinessExceptionEnum.BUSINESS_TRAIN_CODE_UNIQUE_ERROR);
            }
            train.setId(SnowUtil.getSnowflakeNextId());
            train.setCreateTime(now);
            train.setUpdateTime(now);
            trainMapper.insert(train);
        } else {
            train.setUpdateTime(now);
            trainMapper.updateByPrimaryKey(train);
        }
    }

    private Train selectByUnique(String code) {
        TrainExample trainExample = new TrainExample();
        trainExample.createCriteria()
                .andCodeEqualTo(code);
        List<Train> list = trainMapper.selectByExample(trainExample);
        if (CollUtil.isNotEmpty(list)) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public PageResp<TrainQueryResp> queryList(TrainQueryReq req) {
        TrainExample train = new TrainExample();
        train.setOrderByClause("code asc");
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
        train.setOrderByClause("id asc");
        List<Train> trains = trainMapper.selectByExample(train);
        List<TrainQueryResp> trainQueryList = BeanUtil.copyToList(trains, TrainQueryResp.class);

        return trainQueryList;
    }

    public void delete(Long id) {
        trainMapper.deleteByPrimaryKey(id);
    }

}
