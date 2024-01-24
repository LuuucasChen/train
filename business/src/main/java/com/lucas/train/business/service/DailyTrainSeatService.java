package com.lucas.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lucas.common.resp.PageResp;
import com.lucas.common.util.SnowUtil;
import com.lucas.train.business.domain.DailyTrainSeat;
import com.lucas.train.business.domain.DailyTrainSeatExample;
import com.lucas.train.business.mapper.DailyTrainSeatMapper;
import com.lucas.train.business.req.DailyTrainSeatQueryReq;
import com.lucas.train.business.req.DailyTrainSeatSaveReq;
import com.lucas.train.business.resp.DailyTrainSeatQueryResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DailyTrainSeatService {

    @Autowired
    private DailyTrainSeatMapper dailyTrainSeatMapper;

    public void save(DailyTrainSeatSaveReq req) {
        DateTime now = DateTime.now();
        DailyTrainSeat dailyTrainSeat = BeanUtil.copyProperties(req, DailyTrainSeat.class);
        if (ObjectUtil.isNull(dailyTrainSeat.getId())) {
            dailyTrainSeat.setId(SnowUtil.getSnowflakeNextId());
            dailyTrainSeat.setCreateTime(now);
            dailyTrainSeat.setUpdateTime(now);
            dailyTrainSeatMapper.insert(dailyTrainSeat);
        } else {
            dailyTrainSeat.setUpdateTime(now);
            dailyTrainSeatMapper.updateByPrimaryKey(dailyTrainSeat);
        }
    }

    public PageResp<DailyTrainSeatQueryResp> queryList(DailyTrainSeatQueryReq req) {
        DailyTrainSeatExample dailyTrainSeat = new DailyTrainSeatExample();
        dailyTrainSeat.setOrderByClause("train_code asc, carriage_index asc, carriage_seat_index asc");
        DailyTrainSeatExample.Criteria criteria = dailyTrainSeat.createCriteria();

        if (ObjectUtil.isNotEmpty(req.getTrainCode())) {
            criteria.andTrainCodeEqualTo(req.getTrainCode());
        }

        PageHelper.startPage(req.getPage(), req.getSize());
        List<DailyTrainSeat> dailyTrainSeats = dailyTrainSeatMapper.selectByExample(dailyTrainSeat);
        List<DailyTrainSeatQueryResp> dailyTrainSeatQueryList = BeanUtil.copyToList(dailyTrainSeats, DailyTrainSeatQueryResp.class);

        PageResp<DailyTrainSeatQueryResp> resp = new PageResp<>();
        resp.setList(dailyTrainSeatQueryList);

        PageInfo<DailyTrainSeat> pageInfo = new PageInfo<>(dailyTrainSeats);
        resp.setTotal(pageInfo.getTotal());

        return resp;
    }

    public void delete(Long id) {
        dailyTrainSeatMapper.deleteByPrimaryKey(id);
    }

}
