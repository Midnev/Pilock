package com.pilock.service;


import com.pilock.pojo.DataVo;
import com.pilock.repository.DataMapper;
import com.pilock.service.interfaces.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service("dataservice1")
public class DataServiceImple implements DataService {

    @Autowired
    DataMapper repository;


    public ArrayList<DataVo> selectDatas() {
        ArrayList<DataVo> list = repository.selectDatas();
        return list;
    }


    public void insertData(DataVo vo) {
        repository.insertData(vo);

    }
}
