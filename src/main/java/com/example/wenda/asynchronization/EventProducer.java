package com.example.wenda.asynchronization;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.wenda.util.JedisAdapter;
import com.example.wenda.util.RedisKeyGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: weilei
 * @CreateTime: 2019/4/19  11:01
 * @Description: 该类便是事件的生产者，将产生的事件推入到队列中
 */
@Service
public class EventProducer {
    private static final Logger logger = LoggerFactory.getLogger(EventProducer.class);
    @Autowired
    JedisAdapter jedisAdapter;

    public boolean fireEvent(EventEntity eventEntity) {
        try {
            String eventJson = JSON.toJSONString(eventEntity);
            String eventKey = RedisKeyGenerator.getEventKey();
            jedisAdapter.lpush(eventKey, eventJson);
            return true;
        } catch (Exception e) {
            logger.error("将事件推入队列时失败：" + e.getMessage());
            return false;
        }
    }
}
