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

    @Insert("insert into datas(deviceId,message) values(#{vo.deviceId},#{vo.message})")
    void insertData(@Param("vo") DataVo vo);

    @Select("select dataId, deviceId, message, DATE_FORMAT(createdDate, '%Y-%m-%d %T')as createdDate from datas")
    ArrayList<DataVo> selectDatas();

    @Select("select dataId, deviceId, message, DATE_FORMAT(createdDate, '%Y-%m-%d %T')as createdDate from datas where deviceId = #{id}")
    ArrayList<DataVo> selectDatasById(@Param("id") String id);

    @Insert("insert into users(deviceId,keyId) values(#{deviceId},#{keyId})")
    void insertUser(@Param("deviceId") String deviceId, @Param("keyId") String keyId);

    @Select("select deviceId from users where keyId = #{key}")
    ArrayList<String> getDevices(@Param("key") String key);

    @Select("select keyId from users where deviceId = #{deviceId}")
    ArrayList<String> getKeys(@Param("deviceId") String deviceId);

}
