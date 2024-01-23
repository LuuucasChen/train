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
import com.lucas.train.business.domain.TrainStation;
import com.lucas.train.business.domain.TrainStationExample;
import com.lucas.train.business.mapper.TrainStationMapper;
import com.lucas.train.business.req.TrainStationQueryReq;
import com.lucas.train.business.req.TrainStationSaveReq;
import com.lucas.train.business.resp.TrainStationQueryResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainStationService {

    @Autowired
    private TrainStationMapper train_stationMapper;

    public void save(TrainStationSaveReq req) {
        DateTime now = DateTime.now();
        TrainStation train_station = BeanUtil.copyProperties(req, TrainStation.class);
        if (ObjectUtil.isNull(train_station.getId())) {
            // 保存之前，先校验唯一键是否存在
            TrainStation trainStationDB = selectByUnique(req.getTrainCode(), req.getIndex());
            if (ObjectUtil.isNotEmpty(trainStationDB)) {
                throw new BusinessException(BusinessExceptionEnum.BUSINESS_TRAIN_STATION_INDEX_UNIQUE_ERROR);
            }
            // 保存之前，先校验唯一键是否存在
            trainStationDB = selectByUnique(req.getTrainCode(), req.getName());
            if (ObjectUtil.isNotEmpty(trainStationDB)) {
                throw new BusinessException(BusinessExceptionEnum.BUSINESS_TRAIN_STATION_NAME_UNIQUE_ERROR);
            }
            train_station.setId(SnowUtil.getSnowflakeNextId());
            train_station.setCreateTime(now);
            train_station.setUpdateTime(now);
            train_stationMapper.insert(train_station);
        } else {
            train_station.setUpdateTime(now);
            train_stationMapper.updateByPrimaryKey(train_station);
        }
    }

    private TrainStation selectByUnique(String trainCode, Integer index) {
        TrainStationExample trainStationExample = new TrainStationExample();
        trainStationExample.createCriteria()
                .andTrainCodeEqualTo(trainCode)
                .andIndexEqualTo(index);
        List<TrainStation> list = train_stationMapper.selectByExample(trainStationExample);
        if (CollUtil.isNotEmpty(list)) {
            return list.get(0);
        } else {
            return null;
        }
    }

    private TrainStation selectByUnique(String trainCode, String name) {
        TrainStationExample trainStationExample = new TrainStationExample();
        trainStationExample.createCriteria()
                .andTrainCodeEqualTo(trainCode)
                .andNameEqualTo(name);
        List<TrainStation> list = train_stationMapper.selectByExample(trainStationExample);
        if (CollUtil.isNotEmpty(list)) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public PageResp<TrainStationQueryResp> queryList(TrainStationQueryReq req) {
        TrainStationExample train_station = new TrainStationExample();
        train_station.setOrderByClause("train_code asc, `index` asc");
        TrainStationExample.Criteria criteria = train_station.createCriteria();

        if (ObjectUtil.isNotEmpty(req.getTrainCode())) {
            criteria.andTrainCodeEqualTo(req.getTrainCode());
        }

        PageHelper.startPage(req.getPage(), req.getSize());
        List<TrainStation> train_stations = train_stationMapper.selectByExample(train_station);
        List<TrainStationQueryResp> train_stationQueryList = BeanUtil.copyToList(train_stations, TrainStationQueryResp.class);

        PageResp<TrainStationQueryResp> resp = new PageResp<>();
        resp.setList(train_stationQueryList);

        PageInfo<TrainStation> pageInfo = new PageInfo<>(train_stations);
        resp.setTotal(pageInfo.getTotal());

        return resp;
    }

    public void delete(Long id) {
        train_stationMapper.deleteByPrimaryKey(id);
    }

}
