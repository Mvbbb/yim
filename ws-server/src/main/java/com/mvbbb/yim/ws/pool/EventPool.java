package com.mvbbb.yim.ws.pool;

import com.mvbbb.yim.ws.event.EventEnum;
import com.mvbbb.yim.ws.event.IEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

public class EventPool {
    private static final Logger logger = LoggerFactory.getLogger(EventPool.class);
    private static EventPool eventPool = null;

    private EventPool() {
        events = new ConcurrentHashMap<>();
    }

    public static EventPool getInstance(){
        if(eventPool==null){
            synchronized (EventPool.class){
                if(eventPool==null){
                    eventPool = new EventPool();
                }
            }
        }
        return eventPool;
    }

    private ConcurrentHashMap<EventEnum, IEvent> events;

    public void register(EventEnum event,IEvent iEvent){
        logger.info("register event. event:{}",event);
        events.put(event,iEvent);
    }


    public IEvent find(EventEnum event){
        return events.get(event);
    }

}
