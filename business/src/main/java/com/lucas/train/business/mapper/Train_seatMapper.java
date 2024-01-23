package com.lucas.train.business.mapper;

import com.lucas.train.business.domain.Train_seat;
import com.lucas.train.business.domain.Train_seatExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface Train_seatMapper {
    long countByExample(Train_seatExample example);

    int deleteByExample(Train_seatExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Train_seat record);

    int insertSelective(Train_seat record);

    List<Train_seat> selectByExample(Train_seatExample example);

    Train_seat selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Train_seat record, @Param("example") Train_seatExample example);

    int updateByExample(@Param("record") Train_seat record, @Param("example") Train_seatExample example);

    int updateByPrimaryKeySelective(Train_seat record);

    int updateByPrimaryKey(Train_seat record);
}