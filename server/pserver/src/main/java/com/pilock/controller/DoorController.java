package com.pilock.controller;

import com.pilock.pojo.DataVo;
import com.pilock.service.interfaces.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
public class DoorController {

    @Autowired
    @Qualifier("dataservice1")
    DataService service;

    @GetMapping("/")
    public String home(){
        
        return "home";
    }


    @PostMapping("/{id}")
    public int setData(@RequestParam("msg") String msg,@PathVariable("id")String id){
        DataVo vo = new DataVo(msg,id);
        service.insertData(vo);
        return 1;
    }

    @GetMapping("/test")
    public int test(@RequestParam(value = "msg",required = false,defaultValue = "def") String msg){
        System.out.println(msg);
        return 1;
    }



}
