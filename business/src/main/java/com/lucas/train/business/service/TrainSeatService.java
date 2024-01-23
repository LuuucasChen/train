package com.lucas.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lucas.common.resp.PageResp;
import com.lucas.common.util.SnowUtil;
import com.lucas.train.business.domain.TrainCarriage;
import com.lucas.train.business.domain.TrainSeat;
import com.lucas.train.business.domain.TrainSeatExample;
import com.lucas.train.business.enmus.SeatColEnum;
import com.lucas.train.business.mapper.TrainSeatMapper;
import com.lucas.train.business.req.TrainSeatQueryReq;
import com.lucas.train.business.req.TrainSeatSaveReq;
import com.lucas.train.business.resp.TrainSeatQueryResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TrainSeatService {

    @Autowired
    private TrainSeatMapper train_seatMapper;

    @Autowired
    private TrainCarriageService trainCarriageService;

    public void save(TrainSeatSaveReq req) {
        DateTime now = DateTime.now();
        TrainSeat train_seat = BeanUtil.copyProperties(req, TrainSeat.class);
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

    public PageResp<TrainSeatQueryResp> queryList(TrainSeatQueryReq req) {
        TrainSeatExample train_seat = new TrainSeatExample();
        train_seat.setOrderByClause("train_code asc, carriage_index asc, carriage_seat_index asc");
        TrainSeatExample.Criteria criteria = train_seat.createCriteria();

        if (ObjectUtil.isNotEmpty(req.getTrainCode())) {
            criteria.andTrainCodeEqualTo(req.getTrainCode());
        }

        PageHelper.startPage(req.getPage(), req.getSize());
        List<TrainSeat> train_seats = train_seatMapper.selectByExample(train_seat);
        List<TrainSeatQueryResp> train_seatQueryList = BeanUtil.copyToList(train_seats, TrainSeatQueryResp.class);

        PageResp<TrainSeatQueryResp> resp = new PageResp<>();
        resp.setList(train_seatQueryList);

        PageInfo<TrainSeat> pageInfo = new PageInfo<>(train_seats);
        resp.setTotal(pageInfo.getTotal());

        return resp;
    }

    public void delete(Long id) {
        train_seatMapper.deleteByPrimaryKey(id);
    }

    @Transactional
    public void genTrainSeat(String trainCode) {
        DateTime now = DateTime.now();
        //清空所有座位记录
        TrainSeatExample train_seat = new TrainSeatExample();
        TrainSeatExample.Criteria criteria = train_seat.createCriteria();
        criteria.andTrainCodeEqualTo(trainCode);
        train_seatMapper.deleteByExample(train_seat);

        //查找当前车次下得所有车厢
        List<TrainCarriage> trainCarriages = trainCarriageService.selectByTrainCode(trainCode);

        //循环生成每个车厢的座位
        for (TrainCarriage trainCarriage : trainCarriages) {
            //拿到车厢数据：行数 座位类型（得到列数）
            Integer rowCount = trainCarriage.getRowCount();
            String seatType = trainCarriage.getSeatType();
            int seatIndex = 1;
            //根据车厢类型，筛选出所有的列
            List<SeatColEnum> colEnumList = SeatColEnum.getColsByType(seatType);
            //循环行数
            for (int row = 1; row <= rowCount; row++) {
                //循环列数
                for (SeatColEnum seatColEnum : colEnumList) {
                    TrainSeat trainSeat = new TrainSeat();
                    trainSeat.setId(SnowUtil.getSnowflakeNextId());
                    trainSeat.setTrainCode(trainCode);
                    trainSeat.setCarriageIndex(trainCarriage.getIndex());
                    trainSeat.setRow(StrUtil.fillBefore(String.valueOf(row), '0', 2));
                    trainSeat.setCol(seatColEnum.getCode());
                    trainSeat.setSeatType(seatType);
                    trainSeat.setCarriageSeatIndex(seatIndex++);
                    trainSeat.setCreateTime(now);
                    trainSeat.setUpdateTime(now);
                    train_seatMapper.insert(trainSeat);
                }
            }
        }
    }

}
