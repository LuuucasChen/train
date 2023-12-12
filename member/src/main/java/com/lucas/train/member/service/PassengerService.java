package com.lucas.train.member.service;

import com.lucas.common.util.SnowUtil;
import com.lucas.train.member.domain.Passenger;
import com.lucas.train.member.mapper.PassengerMapper;
import com.lucas.train.member.req.PassengerSaveReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PassengerService {

    @Autowired
    private PassengerMapper passengerMapper;

    public void save(PassengerSaveReq req) {
        Date date = new Date();
        Passenger passenger = new Passenger();
        passenger.setId(SnowUtil.getSnowflakeNextId());
        passenger.setCreateTime(date);
        passenger.setUpdateTime(date);

        passenger.setMemberId(req.getMemberId());
        passenger.setName(req.getName());
        passenger.setIdCard(req.getIdCard());
        passenger.setType(req.getType());
        passengerMapper.insert(passenger);
    }
}
