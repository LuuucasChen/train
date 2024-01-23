package com.lucas.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lucas.common.resp.PageResp;
import com.lucas.common.util.SnowUtil;
import com.lucas.train.business.domain.Train_station;
import com.lucas.train.business.domain.Train_stationExample;
import com.lucas.train.business.mapper.Train_stationMapper;
import com.lucas.train.business.req.Train_stationQueryReq;
import com.lucas.train.business.req.Train_stationSaveReq;
import com.lucas.train.business.resp.Train_stationQueryResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Train_stationService {

    @Autowired
    private Train_stationMapper train_stationMapper;

    public void save(Train_stationSaveReq req) {
        DateTime now = DateTime.now();
        Train_station train_station = BeanUtil.copyProperties(req, Train_station.class);
        if (ObjectUtil.isNull(train_station.getId())) {
            train_station.setId(SnowUtil.getSnowflakeNextId());
            train_station.setCreateTime(now);
            train_station.setUpdateTime(now);
            train_stationMapper.insert(train_station);
        } else {
            train_station.setUpdateTime(now);
            train_stationMapper.updateByPrimaryKey(train_station);
        }
    }

    public PageResp<Train_stationQueryResp> queryList(Train_stationQueryReq req) {
        Train_stationExample train_station = new Train_stationExample();
        train_station.setOrderByClause("train_code asc, `index` asc");
        Train_stationExample.Criteria criteria = train_station.createCriteria();

        if (ObjectUtil.isNotEmpty(req.getTrainCode())) {
            criteria.andTrainCodeEqualTo(req.getTrainCode());
        }

        PageHelper.startPage(req.getPage(), req.getSize());
        List<Train_station> train_stations = train_stationMapper.selectByExample(train_station);
        List<Train_stationQueryResp> train_stationQueryList = BeanUtil.copyToList(train_stations, Train_stationQueryResp.class);

        PageResp<Train_stationQueryResp> resp = new PageResp<>();
        resp.setList(train_stationQueryList);

        PageInfo<Train_station> pageInfo = new PageInfo<>(train_stations);
        resp.setTotal(pageInfo.getTotal());

        return resp;
    }

    public void delete(Long id) {
        train_stationMapper.deleteByPrimaryKey(id);
    }

}
