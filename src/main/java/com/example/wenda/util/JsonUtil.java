package com.example.wenda.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

/**
 * @Author: weilei
 * @CreateTime: 2019/4/11  9:32
 * @Description:
 */
@Service
public class JsonUtil {
    private static JSONObject jsonObject = new JSONObject();
    public JSONObject toJsonObject(String code,String msg){
        jsonObject.put("code",code);
        jsonObject.put("msg",msg);
        return jsonObject;
    }
    public JSONObject toJsonObject(String code,String msg,Object data){
        jsonObject.put("code",code);
        jsonObject.put("msg",msg);
        jsonObject.put("data",data);
        return jsonObject;
    }
}
