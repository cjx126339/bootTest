package com.jfcat.boottest.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TDictionary {
    private int id;
    private String type;
    private String data;
    private int displayOrder;
}
