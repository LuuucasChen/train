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
import com.lucas.train.business.domain.Station;
import com.lucas.train.business.domain.StationExample;
import com.lucas.train.business.mapper.StationMapper;
import com.lucas.train.business.req.StationQueryReq;
import com.lucas.train.business.req.StationSaveReq;
import com.lucas.train.business.resp.StationQueryResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StationService {

    @Autowired
    private StationMapper stationMapper;

    public void save(StationSaveReq req) {
        DateTime now = DateTime.now();
        Station station = BeanUtil.copyProperties(req, Station.class);
        if (ObjectUtil.isNull(station.getId())) {

            //保存之前车站是否存在
            Station stations = selectByUnique(req.getName());
            if (ObjectUtil.isNotNull(stations)) {
                throw new BusinessException(BusinessExceptionEnum.BUSINESS_STATION_NAME_UNIQUE_ERROR);
            }

            station.setId(SnowUtil.getSnowflakeNextId());
            station.setCreateTime(now);
            station.setUpdateTime(now);
            stationMapper.insert(station);
        } else {
            station.setUpdateTime(now);
            stationMapper.updateByPrimaryKey(station);
        }
    }

    private Station selectByUnique(String name) {
        StationExample stationExample = new StationExample();
        StationExample.Criteria criteria = stationExample.createCriteria();
        criteria.andNameEqualTo(name);
        List<Station> stations = stationMapper.selectByExample(stationExample);
        if (CollUtil.isNotEmpty(stations)) {
            return stations.get(0);
        } else {
            return null;
        }
    }

    public PageResp<StationQueryResp> queryList(StationQueryReq req) {
        StationExample station = new StationExample();
        station.setOrderByClause("id desc");
        StationExample.Criteria criteria = station.createCriteria();

        PageHelper.startPage(req.getPage(), req.getSize());
        List<Station> stations = stationMapper.selectByExample(station);
        List<StationQueryResp> stationQueryList = BeanUtil.copyToList(stations, StationQueryResp.class);

        PageResp<StationQueryResp> resp = new PageResp<>();
        resp.setList(stationQueryList);

        PageInfo<Station> pageInfo = new PageInfo<>(stations);
        resp.setTotal(pageInfo.getTotal());

        return resp;
    }

    public void delete(Long id) {
        stationMapper.deleteByPrimaryKey(id);
    }

    public List<StationQueryResp> queryAll() {
        StationExample stationExample = new StationExample();
        stationExample.setOrderByClause("name_pinyin asc");
        List<Station> stationList = stationMapper.selectByExample(stationExample);
        return BeanUtil.copyToList(stationList, StationQueryResp.class);
    }


}
