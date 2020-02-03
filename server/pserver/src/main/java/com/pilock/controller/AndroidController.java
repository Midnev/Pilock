package com.pilock.controller;

import com.pilock.pojo.DataVo;
import com.pilock.service.interfaces.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/android")
public class AndroidController {

    @Autowired
    @Qualifier("dataservice1")
    DataService service;

    @GetMapping("/")
    public ArrayList<DataVo> getList(@RequestParam(name = "key",defaultValue = "",required = false) String key){
        if (key.equals(""))
            return null;
        String deviceId = service.getDevideIds(key).get(0);
        ArrayList<DataVo> list = service.selectDatasByDeviceId(deviceId);
        return list;
    }
    @GetMapping("/csv")
    public String getListStr(){
        String ret = service.getStringData();
        if (ret == null||ret.equals(""))
            return "non";
        System.out.println("contr"+ret);
        return ret;
    }

    @PostMapping("/init")
    public void addSetting(@RequestParam(name = "id",defaultValue = "",required = false) String id,
                           @RequestParam(name = "key",defaultValue = "",required = false) String key){

        //add to database search data
        service.insertUser(id,key);
    }

}
