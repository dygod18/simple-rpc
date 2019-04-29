package com.wumu.rpc.simplerpc.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by dydy on 2019/4/27.
 */
public class LocalCache {

    private static final Map<String, Object> CACHE = new ConcurrentHashMap<>();

    private static volatile LocalCache INSTANCE = null;

    private LocalCache() {};

    public static  LocalCache getInstance() {
        if(INSTANCE == null){
            synchronized(LocalCache.class){
                if(INSTANCE == null){
                    INSTANCE = new LocalCache();
                }
            }
        }
        return INSTANCE;
    }

    public Object getResut(String invokeId){
        return CACHE.get(invokeId);
    }

    /**
     * 将结果写入到缓存中
     * @param invokeId
     * @param result
     */
    public void putResult(String invokeId, Object result){
        CACHE.put(invokeId, result);
    }

}
