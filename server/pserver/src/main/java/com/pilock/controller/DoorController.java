package com.pilock.controller;



import com.pilock.pojo.DataVo;
import com.pilock.service.interfaces.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@RestController
public class DoorController {

    @Autowired
    @Qualifier("dataservice1")
    DataService service;

    @PostMapping("/save")
    public int setData(@RequestParam("msg") String msg,@RequestParam("id")String id){
        DataVo vo = new DataVo(msg,
                (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()).toString(),
                            id);
        service.insertData(vo);

        return 1;
    }

    @GetMapping("/test")
    public int test(@RequestParam(value = "msg",required = false,defaultValue = "def") String msg){
        System.out.println(msg);
        return 1;
    }



}
