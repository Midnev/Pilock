package com.pilock.service.interfaces;

import com.pilock.pojo.DataVo;

import java.util.ArrayList;

public interface DataService {
    ArrayList<DataVo> selectDatas();
    ArrayList<DataVo> selectDatasByDeviceId(String id);
    ArrayList<String> selectMsgByDeviceId(String id);
    void insertData(DataVo vo);
    void insertUser(String deviceId,String key);
    ArrayList<String> getDevideIds(String key);
    ArrayList<String> getTopics(String deviceId);
    String getStringData();
}
