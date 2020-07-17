package com.candy.mz.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.candy.mz.po.ResultMessage;


/**
 * @author by wangchong
 * @Description
 * @Date 2020/7/17 11:15
 */
public class MessageUtil {

    public static String getMessage(boolean isSystemMessage, String fromName, Object message) {
        try {
            ResultMessage resultMessage = new ResultMessage();
            resultMessage.setSystem(isSystemMessage);
            resultMessage.setMessage(message);
            if (StrUtil.isBlank(fromName)) {
                resultMessage.setFromName(fromName);
            }
            return JSONUtil.toJsonStr(resultMessage);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
