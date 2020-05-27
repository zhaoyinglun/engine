package com.ruoyi.project.system.domain.api;

import lombok.Data;

// 告诉前台  接收到的数据样式是什么样子
@Data
public class ApiDataExample<T> {
    private int code;
    private String msg;
    private T data;
}
