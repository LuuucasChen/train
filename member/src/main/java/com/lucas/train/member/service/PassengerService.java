package com.lucas.train.member.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.lucas.common.context.MemberLoginContext;
import com.lucas.common.util.SnowUtil;
import com.lucas.train.member.domain.Passenger;
import com.lucas.train.member.domain.PassengerExample;
import com.lucas.train.member.mapper.PassengerMapper;
import com.lucas.train.member.req.PassengerQueryReq;
import com.lucas.train.member.req.PassengerSaveReq;
import com.lucas.train.member.resp.PassengerQueryResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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

        passenger.setMemberId(MemberLoginContext.getId());
        passenger.setName(req.getName());
        passenger.setIdCard(req.getIdCard());
        passenger.setType(req.getType());
        passengerMapper.insert(passenger);
    }

    public List<PassengerQueryResp> queryList(PassengerQueryReq req) {
        PassengerExample passenger = new PassengerExample();
        PassengerExample.Criteria criteria = passenger.createCriteria();
        if (ObjectUtil.isNotNull(req.getMemberId())) {
            criteria.andMemberIdEqualTo(req.getMemberId());
        }
        List<Passenger> passengers = passengerMapper.selectByExample(passenger);
        List<PassengerQueryResp> passengerQueryList = BeanUtil.copyToList(passengers, PassengerQueryResp.class);
        return passengerQueryList;
    }
}
