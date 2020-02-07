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

    public String getStringData(){
        StringBuffer sb = new StringBuffer();
        ArrayList<DataVo> list = repository.selectDatas();
        for (DataVo vo : list){

            sb.append( vo.toMessage());
            sb.append(",");//to csv;
        }
        return sb.toString();
    }


    public ArrayList<DataVo> selectDatasByDeviceId(String id) {
        return repository.selectDatasById(id);
    }


    public ArrayList<String> selectMsgByDeviceId(String id) {
        ArrayList<DataVo> res = repository.selectDatasById(id);
        return null;
    }

    public void insertData(DataVo vo) {
        repository.insertData(vo);

    }


    public void insertUser(String deviceId, String key) {
        repository.insertUser(deviceId, key);
    }


    public ArrayList<String> getDevideIds(String key) {
        return repository.getDevices(key);
    }

    public ArrayList<String> getTopics(String deviceId) {
        return repository.getKeys(deviceId);
    }
}
