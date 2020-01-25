package com.pilock.service.interfaces;

import com.pilock.pojo.DataVo;

import java.util.ArrayList;

public interface DataService {
    ArrayList<DataVo> selectDatas();
    ArrayList<DataVo> selectDatasByDeviceId(String id);
    ArrayList<String> selectMsgByDeviceId(String id);
    void insertData(DataVo vo);
}
