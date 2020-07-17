package com.candy.mz.po;

import lombok.Data;

/**
 * @author by wangchong
 * @Description 服务端响应消息对象
 * @Date 2020/7/17 11:12
 */
@Data
public class ResultMessage {

    private boolean isSystem;

    private String fromName;

    private Object message;


}
