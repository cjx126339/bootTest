package com.jfcat.boottest.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfcat.boottest.entity.TDictionary;
import com.jfcat.boottest.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    @Autowired
    DictionaryService dictionaryService;

    @RequestMapping("/test")
    public String test(@RequestBody String reqData, User user) {
        JSONObject jsonObject = JSONObject.parseObject(reqData);
        jsonObject.put("address",user);
        return JSONObject.toJSONString(jsonObject);
    }

    @RequestMapping("/test1")
    public String test1() {
        List<TDictionary> allD = dictionaryService.findAllD();
        return JSONObject.toJSONString(allD);
    }

}
