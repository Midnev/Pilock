package com.pilock.controller;

import com.pilock.pojo.DataVo;
import com.pilock.service.ServersideFireBaseService;
import com.pilock.service.interfaces.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class DoorController {

    @Autowired
    @Qualifier("dataservice1")
    DataService service;

    @Autowired
    ServersideFireBaseService s2;

    @GetMapping("/")
    public String home(){
/*        try {
            System.out.println(s2.sendAutoDoorNotification("title 1","body 2","test"));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }*/


        return "home";
    }

    @PostMapping("/{id}")
    public int setData(@RequestParam(name="msg",defaultValue = "",required = false) String msg,
                       @PathVariable("id")String id){
        try {
            if (!msg.equals("")) {
                ArrayList<String> list = service.getTopics(id);
                for (String topic : list){
                    System.out.println(s2.sendAutoDoorNotification("Pilock Alarm", msg, topic));
                }
               //
                DataVo vo = new DataVo(msg, id);
                service.insertData(vo);
            } else
                return 0;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return 0;
        }
        return 1;
    }
/*
    @GetMapping("/test")
    public int test(@RequestParam(value = "msg",required = false,defaultValue = "def") String msg){
        System.out.println(msg);
        return 1;
    }*/



}
