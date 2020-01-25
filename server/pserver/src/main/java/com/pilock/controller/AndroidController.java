package com.pilock.controller;

import com.pilock.pojo.DataVo;
import com.pilock.service.interfaces.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

@Controller
@RequestMapping("/android")
public class AndroidController {

    @Autowired
    @Qualifier("dataservice1")
    DataService service;

    @GetMapping("/")
    public ArrayList<DataVo> getList(){
        ArrayList<DataVo> list = service.selectDatasByDeviceId("");
        return list;
    }

    @PostMapping("/init")
    public void addSetting(@RequestParam("str") String str){
        //add to database search data
        String deviceId = str.substring(0,15);
        String key= str.substring(16);
    }

}
