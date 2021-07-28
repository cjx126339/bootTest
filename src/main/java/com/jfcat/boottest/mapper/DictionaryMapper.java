package com.jfcat.boottest.mapper;

import com.jfcat.boottest.entity.TDictionary;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DictionaryMapper {
    List<TDictionary> queryAllDictionary();
}
