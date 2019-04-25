package com.example.wenda.asynchronization;

import com.alibaba.fastjson.JSON;
import com.example.wenda.util.JedisAdapter;
import com.example.wenda.util.RedisKeyGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: weilei
 * @CreateTime: 2019/4/18  19:28
 * @Description:
 */
@Service
public class EventConsumer implements InitializingBean, ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);
    private static Map<EventType, List<EventHandler>> config = new HashMap<>();
    private static ApplicationContext applicationContext;
    @Autowired
    JedisAdapter jedisAdapter;

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, EventHandler> eventHandlerMap = applicationContext.getBeansOfType(EventHandler.class);
        if (eventHandlerMap != null) {
            for (Map.Entry<String, EventHandler> entry : eventHandlerMap.entrySet()) {
                List<EventType> eventTypeList = entry.getValue().getSupportedEventType();
                if (eventTypeList==null){
                    continue;
                }
                for (EventType eventType : eventTypeList) {
                    if (!config.containsKey(eventType)) {
                        config.put(eventType, new ArrayList<>());
                    }
                    config.get(eventType).add(entry.getValue());
                }
            }
        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    String key = RedisKeyGenerator.getEventKey();
                    List<String> list = jedisAdapter.brpop(0, key);
                    for (String s : list) {
                        if (s.equals(key)) {
                            continue;
                        }
                        EventEntity eventEntity = JSON.parseObject(s, EventEntity.class);
                        if (!config.containsKey(eventEntity.getEventType())) {
                            logger.error("不能识别的事件");
                            continue;
                        }
                        for (EventHandler eventHandler : config.get(eventEntity.getEventType())){
                            eventHandler.handleEvent(eventEntity);
                        }
                    }
                }
            }
        });
        thread.start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
