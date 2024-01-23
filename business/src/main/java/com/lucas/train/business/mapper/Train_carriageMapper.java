package com.lucas.train.business.mapper;

import com.lucas.train.business.domain.Train_carriage;
import com.lucas.train.business.domain.Train_carriageExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface Train_carriageMapper {
    long countByExample(Train_carriageExample example);

    int deleteByExample(Train_carriageExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Train_carriage record);

    int insertSelective(Train_carriage record);

    List<Train_carriage> selectByExample(Train_carriageExample example);

    Train_carriage selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Train_carriage record, @Param("example") Train_carriageExample example);

    int updateByExample(@Param("record") Train_carriage record, @Param("example") Train_carriageExample example);

    int updateByPrimaryKeySelective(Train_carriage record);

    int updateByPrimaryKey(Train_carriage record);
}