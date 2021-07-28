package com.jfcat.boottest.service.impl;

import com.jfcat.boottest.entity.TDictionary;
import com.jfcat.boottest.mapper.DictionaryMapper;
import com.jfcat.boottest.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictionaryServiceImpl implements DictionaryService {

    @Autowired
    DictionaryMapper dictionaryMapper;

    @Override
    public List<TDictionary> findAllD() {
        return dictionaryMapper.queryAllDictionary();
    }
}
