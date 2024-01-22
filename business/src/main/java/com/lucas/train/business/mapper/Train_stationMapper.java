package com.lucas.train.business.mapper;

import com.lucas.train.business.domain.Train_station;
import com.lucas.train.business.domain.Train_stationExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface Train_stationMapper {
    long countByExample(Train_stationExample example);

    int deleteByExample(Train_stationExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Train_station record);

    int insertSelective(Train_station record);

    List<Train_station> selectByExample(Train_stationExample example);

    Train_station selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Train_station record, @Param("example") Train_stationExample example);

    int updateByExample(@Param("record") Train_station record, @Param("example") Train_stationExample example);

    int updateByPrimaryKeySelective(Train_station record);

    int updateByPrimaryKey(Train_station record);
}