package com.lucas.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lucas.common.resp.PageResp;
import com.lucas.common.util.SnowUtil;
import com.lucas.train.business.domain.DailyTrain;
import com.lucas.train.business.domain.DailyTrainExample;
import com.lucas.train.business.domain.Train;
import com.lucas.train.business.mapper.DailyTrainMapper;
import com.lucas.train.business.req.DailyTrainQueryReq;
import com.lucas.train.business.req.DailyTrainSaveReq;
import com.lucas.train.business.resp.DailyTrainQueryResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class DailyTrainService {

    private static final Logger LOG = LoggerFactory.getLogger(DailyTrainService.class);
    @Autowired
    private DailyTrainMapper dailyTrainMapper;

    @Autowired
    private TrainService trainService;

    @Autowired
    private DailyTrainStationService dailyTrainStationService;

    public void save(DailyTrainSaveReq req) {
        DateTime now = DateTime.now();
        DailyTrain dailyTrain = BeanUtil.copyProperties(req, DailyTrain.class);
        if (ObjectUtil.isNull(dailyTrain.getId())) {
            dailyTrain.setId(SnowUtil.getSnowflakeNextId());
            dailyTrain.setCreateTime(now);
            dailyTrain.setUpdateTime(now);
            dailyTrainMapper.insert(dailyTrain);
        } else {
            dailyTrain.setUpdateTime(now);
            dailyTrainMapper.updateByPrimaryKey(dailyTrain);
        }
    }

    public PageResp<DailyTrainQueryResp> queryList(DailyTrainQueryReq req) {
        DailyTrainExample dailyTrain = new DailyTrainExample();
        dailyTrain.setOrderByClause("date desc, code asc");
        DailyTrainExample.Criteria criteria = dailyTrain.createCriteria();
        if (ObjectUtil.isNotEmpty(req.getDate())) {
            criteria.andDateEqualTo(req.getDate());
        }
        if (ObjectUtil.isNotEmpty(req.getCode())) {
            criteria.andCodeEqualTo(req.getCode());
        }

        PageHelper.startPage(req.getPage(), req.getSize());
        List<DailyTrain> dailyTrains = dailyTrainMapper.selectByExample(dailyTrain);
        List<DailyTrainQueryResp> dailyTrainQueryList = BeanUtil.copyToList(dailyTrains, DailyTrainQueryResp.class);

        PageResp<DailyTrainQueryResp> resp = new PageResp<>();
        resp.setList(dailyTrainQueryList);

        PageInfo<DailyTrain> pageInfo = new PageInfo<>(dailyTrains);
        resp.setTotal(pageInfo.getTotal());

        return resp;
    }

    public void delete(Long id) {
        dailyTrainMapper.deleteByPrimaryKey(id);
    }

    /**
     * 生成某日所有车次信息，包括车次 车站 车厢 座位
     * @param date
     */
    public void genDaily(Date date) {
        List<Train> trains = trainService.selectAll();
        if (CollUtil.isEmpty(trains)) {
            LOG.info("没有车次信息，任务结束");
            return;
        }
        for (Train train : trains) {
            genDailyTrain(date, train);

        }
    }

    private void genDailyTrain(Date date, Train train) {
        //删除该车次已有的数据
        DailyTrainExample dailyTrainExample = new DailyTrainExample();
        dailyTrainExample.createCriteria()
                .andDateEqualTo(date)
                .andCodeEqualTo(train.getCode());
        dailyTrainMapper.deleteByExample(dailyTrainExample);

        //生成该车次数据
        DateTime now = DateTime.now();
        DailyTrain dailyTrain = BeanUtil.copyProperties(train, DailyTrain.class);
        dailyTrain.setId(SnowUtil.getSnowflakeNextId());
        dailyTrain.setCreateTime(now);
        dailyTrain.setUpdateTime(now);
        dailyTrain.setDate(date);
        dailyTrainMapper.insert(dailyTrain);

        //生成该车次的车站信息
        dailyTrainStationService.genDaily(date, dailyTrain.getCode());
    }

}
