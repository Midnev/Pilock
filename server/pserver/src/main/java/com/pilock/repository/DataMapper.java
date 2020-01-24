package com.pilock.repository;


import com.pilock.pojo.DataVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
@Mapper
public interface DataMapper {

    @Insert("insert into datas(deviceId,message,createdDate) values(#{vo.deviceId},#{vo.message},#{vo.createdDate})")
    void insertData(@Param("vo") DataVo vo);

    @Select("select * from datas")
    ArrayList<DataVo> selectDatas();

}
