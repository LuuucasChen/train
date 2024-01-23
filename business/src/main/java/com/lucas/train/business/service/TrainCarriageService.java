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
import com.lucas.train.business.domain.TrainCarriage;
import com.lucas.train.business.domain.TrainCarriageExample;
import com.lucas.train.business.enmus.SeatColEnum;
import com.lucas.train.business.mapper.TrainCarriageMapper;
import com.lucas.train.business.req.TrainCarriageQueryReq;
import com.lucas.train.business.req.TrainCarriageSaveReq;
import com.lucas.train.business.resp.TrainCarriageQueryResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainCarriageService {

    @Autowired
    private TrainCarriageMapper train_carriageMapper;

    public void save(TrainCarriageSaveReq req) {
        DateTime now = DateTime.now();

        // 自动计算出列数和总座位数
        List<SeatColEnum> seatColEnums = SeatColEnum.getColsByType(req.getSeatType());
        req.setColCount(seatColEnums.size());
        req.setSeatCount(req.getColCount() * req.getRowCount());

        TrainCarriage train_carriage = BeanUtil.copyProperties(req, TrainCarriage.class);
        if (ObjectUtil.isNull(train_carriage.getId())) {
            // 保存之前，先校验唯一键是否存在
            TrainCarriage trainCarriageDB = selectByUnique(req.getTrainCode(), req.getIndex());
            if (ObjectUtil.isNotEmpty(trainCarriageDB)) {
                throw new BusinessException(BusinessExceptionEnum.BUSINESS_TRAIN_CARRIAGE_INDEX_UNIQUE_ERROR);
            }
            train_carriage.setId(SnowUtil.getSnowflakeNextId());
            train_carriage.setCreateTime(now);
            train_carriage.setUpdateTime(now);
            train_carriageMapper.insert(train_carriage);
        } else {
            train_carriage.setUpdateTime(now);
            train_carriageMapper.updateByPrimaryKey(train_carriage);
        }
    }

    private TrainCarriage selectByUnique(String trainCode, Integer index) {
        TrainCarriageExample train_carriageExample = new TrainCarriageExample();
        TrainCarriageExample.Criteria criteria = train_carriageExample.createCriteria();
        criteria.andTrainCodeEqualTo(trainCode).andIndexEqualTo(index);
        List<TrainCarriage> train_carriages = train_carriageMapper.selectByExample(train_carriageExample);
        if (CollUtil.isNotEmpty(train_carriages)) {
            return train_carriages.get(0);
        } else {
            return null;
        }
    }

    public PageResp<TrainCarriageQueryResp> queryList(TrainCarriageQueryReq req) {
        TrainCarriageExample train_carriage = new TrainCarriageExample();
        train_carriage.setOrderByClause("train_code asc, `index` asc");
        TrainCarriageExample.Criteria criteria = train_carriage.createCriteria();

        if (ObjectUtil.isNotEmpty(req.getTrainCode())) {
            criteria.andTrainCodeEqualTo(req.getTrainCode());
        }

        PageHelper.startPage(req.getPage(), req.getSize());
        List<TrainCarriage> train_carriages = train_carriageMapper.selectByExample(train_carriage);
        List<TrainCarriageQueryResp> train_carriageQueryList = BeanUtil.copyToList(train_carriages, TrainCarriageQueryResp.class);

        PageResp<TrainCarriageQueryResp> resp = new PageResp<>();
        resp.setList(train_carriageQueryList);

        PageInfo<TrainCarriage> pageInfo = new PageInfo<>(train_carriages);
        resp.setTotal(pageInfo.getTotal());

        return resp;
    }

    public void delete(Long id) {
        train_carriageMapper.deleteByPrimaryKey(id);
    }

    public List<TrainCarriage> selectByTrainCode(String trainCode) {
        TrainCarriageExample train_carriage = new TrainCarriageExample();
        train_carriage.setOrderByClause("`index` asc");
        TrainCarriageExample.Criteria criteria = train_carriage.createCriteria();
        criteria.andTrainCodeEqualTo(trainCode);
        List<TrainCarriage> train_carriages = train_carriageMapper.selectByExample(train_carriage);
        return train_carriages;
    }

}
