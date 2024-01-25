package com.lucas.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lucas.common.resp.PageResp;
import com.lucas.common.util.SnowUtil;
import com.lucas.train.business.domain.DailyTrainStation;
import com.lucas.train.business.domain.DailyTrainStationExample;
import com.lucas.train.business.domain.TrainStation;
import com.lucas.train.business.mapper.DailyTrainStationMapper;
import com.lucas.train.business.req.DailyTrainStationQueryReq;
import com.lucas.train.business.req.DailyTrainStationSaveReq;
import com.lucas.train.business.resp.DailyTrainStationQueryResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class DailyTrainStationService {

    private static final Logger LOG = LoggerFactory.getLogger(DailyTrainStationService.class);
    @Autowired
    private DailyTrainStationMapper dailyTrainStationMapper;

    @Autowired
    private TrainStationService trainStationService;

    public void save(DailyTrainStationSaveReq req) {
        DateTime now = DateTime.now();
        DailyTrainStation dailyTrainStation = BeanUtil.copyProperties(req, DailyTrainStation.class);
        if (ObjectUtil.isNull(dailyTrainStation.getId())) {
            dailyTrainStation.setId(SnowUtil.getSnowflakeNextId());
            dailyTrainStation.setCreateTime(now);
            dailyTrainStation.setUpdateTime(now);
            dailyTrainStationMapper.insert(dailyTrainStation);
        } else {
            dailyTrainStation.setUpdateTime(now);
            dailyTrainStationMapper.updateByPrimaryKey(dailyTrainStation);
        }
    }

    public PageResp<DailyTrainStationQueryResp> queryList(DailyTrainStationQueryReq req) {
        DailyTrainStationExample dailyTrainStation = new DailyTrainStationExample();
        dailyTrainStation.setOrderByClause("date desc, train_code asc, `index` asc");
        DailyTrainStationExample.Criteria criteria = dailyTrainStation.createCriteria();

        if (ObjUtil.isNotNull(req.getDate())) {
            criteria.andDateEqualTo(req.getDate());
        }
        if (ObjUtil.isNotEmpty(req.getTrainCode())) {
            criteria.andTrainCodeEqualTo(req.getTrainCode());
        }

        PageHelper.startPage(req.getPage(), req.getSize());
        List<DailyTrainStation> dailyTrainStations = dailyTrainStationMapper.selectByExample(dailyTrainStation);
        List<DailyTrainStationQueryResp> dailyTrainStationQueryList = BeanUtil.copyToList(dailyTrainStations, DailyTrainStationQueryResp.class);

        PageResp<DailyTrainStationQueryResp> resp = new PageResp<>();
        resp.setList(dailyTrainStationQueryList);

        PageInfo<DailyTrainStation> pageInfo = new PageInfo<>(dailyTrainStations);
        resp.setTotal(pageInfo.getTotal());

        return resp;
    }

    public void delete(Long id) {
        dailyTrainStationMapper.deleteByPrimaryKey(id);
    }

    public void genDaily(Date date, String trainCode) {
        LOG.info("开始生成日期【{}】 车次【{}】 的车站信息", DateUtil.formatDate(date), trainCode);

        //删除某日某车次的车站信息
        DailyTrainStationExample dailyTrainStationExample = new DailyTrainStationExample();
        dailyTrainStationExample.createCriteria()
                .andDateEqualTo(date)
                .andTrainCodeEqualTo(trainCode);
        dailyTrainStationMapper.deleteByExample(dailyTrainStationExample);

        //生成该车次车站数据
        List<TrainStation> trainStations = trainStationService.selectByTrainCode(trainCode);
        if (trainStations.isEmpty()) {
            LOG.info("该【{}】车次车站为空,生成车站信息结束",trainCode);
            return;
        }
        for (TrainStation trainStation : trainStations) {
            DateTime now = DateTime.now();
            DailyTrainStation dailyTrain = BeanUtil.copyProperties(trainStation, DailyTrainStation.class);
            dailyTrain.setId(SnowUtil.getSnowflakeNextId());
            dailyTrain.setCreateTime(now);
            dailyTrain.setUpdateTime(now);
            dailyTrain.setDate(date);
            dailyTrainStationMapper.insert(dailyTrain);
        }

    }

}
