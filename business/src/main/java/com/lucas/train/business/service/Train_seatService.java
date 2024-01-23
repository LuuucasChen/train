package com.lucas.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lucas.common.resp.PageResp;
import com.lucas.common.util.SnowUtil;
import com.lucas.train.business.domain.Train_seat;
import com.lucas.train.business.domain.Train_seatExample;
import com.lucas.train.business.mapper.Train_seatMapper;
import com.lucas.train.business.req.Train_seatQueryReq;
import com.lucas.train.business.req.Train_seatSaveReq;
import com.lucas.train.business.resp.Train_seatQueryResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Train_seatService {

    @Autowired
    private Train_seatMapper train_seatMapper;

    public void save(Train_seatSaveReq req) {
        DateTime now = DateTime.now();
        Train_seat train_seat = BeanUtil.copyProperties(req, Train_seat.class);
        if (ObjectUtil.isNull(train_seat.getId())) {
            train_seat.setId(SnowUtil.getSnowflakeNextId());
            train_seat.setCreateTime(now);
            train_seat.setUpdateTime(now);
            train_seatMapper.insert(train_seat);
        } else {
            train_seat.setUpdateTime(now);
            train_seatMapper.updateByPrimaryKey(train_seat);
        }
    }

    public PageResp<Train_seatQueryResp> queryList(Train_seatQueryReq req) {
        Train_seatExample train_seat = new Train_seatExample();
        train_seat.setOrderByClause("id desc");
        Train_seatExample.Criteria criteria = train_seat.createCriteria();

        PageHelper.startPage(req.getPage(), req.getSize());
        List<Train_seat> train_seats = train_seatMapper.selectByExample(train_seat);
        List<Train_seatQueryResp> train_seatQueryList = BeanUtil.copyToList(train_seats, Train_seatQueryResp.class);

        PageResp<Train_seatQueryResp> resp = new PageResp<>();
        resp.setList(train_seatQueryList);

        PageInfo<Train_seat> pageInfo = new PageInfo<>(train_seats);
        resp.setTotal(pageInfo.getTotal());

        return resp;
    }

    public void delete(Long id) {
        train_seatMapper.deleteByPrimaryKey(id);
    }

}
