package com.lucas.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lucas.common.resp.PageResp;
import com.lucas.common.util.SnowUtil;
import com.lucas.train.business.domain.Train_carriage;
import com.lucas.train.business.domain.Train_seat;
import com.lucas.train.business.domain.Train_seatExample;
import com.lucas.train.business.enmus.SeatColEnum;
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

    @Autowired
    private Train_carriageService trainCarriageService;

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
        train_seat.setOrderByClause("train_code asc, carriage_index asc, carriage_seat_index asc");
        Train_seatExample.Criteria criteria = train_seat.createCriteria();

        if (ObjectUtil.isNotEmpty(req.getTrainCode())) {
            criteria.andTrainCodeEqualTo(req.getTrainCode());
        }

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

    public void genTrainSeat(String trainCode) {
        DateTime now = DateTime.now();
        //清空所有座位记录
        Train_seatExample train_seat = new Train_seatExample();
        Train_seatExample.Criteria criteria = train_seat.createCriteria();
        criteria.andTrainCodeEqualTo(trainCode);
        train_seatMapper.deleteByExample(train_seat);

        //查找当前车次下得所有车厢
        List<Train_carriage> trainCarriages = trainCarriageService.selectByTrainCode(trainCode);

        //循环生成每个车厢的座位
        for (Train_carriage trainCarriage : trainCarriages) {
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
                    Train_seat trainSeat = new Train_seat();
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
