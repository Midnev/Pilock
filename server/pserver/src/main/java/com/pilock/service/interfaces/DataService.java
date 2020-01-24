package com.pilock.service.interfaces;

import com.pilock.pojo.DataVo;

import java.util.ArrayList;

public interface DataService {
    ArrayList<DataVo> selectDatas();
    void insertData(DataVo vo);
}
