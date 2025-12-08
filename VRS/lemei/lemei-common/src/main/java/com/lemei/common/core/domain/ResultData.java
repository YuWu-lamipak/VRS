package com.lemei.common.core.domain;

/**
 * @author yq
 * @CLassName ResultData
 * @Description
 * @date 2022/5/10 13:54
 **/

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ResultData extends HashMap implements Map{

    private static final long serialVersionUID = 1L;

    Map map;


    public ResultData() {
        map = new HashMap();
    }

    @Override
    public Object get(Object key) {
        return map.get(key);
    }

    public String getString(Object key) {
        return (String)get(key);
    }

    @Override
    public Object put(Object key, Object value) {
        return map.put(key, value);
    }

    @Override
    public Object remove(Object key) {
        return map.remove(key);
    }

    public void clear() {
        map.clear();
    }

    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    public Set entrySet() {
        return map.entrySet();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public Set keySet() {
        return map.keySet();
    }

    @SuppressWarnings("unchecked")
    public void putAll(Map t) {
        map.putAll(t);
    }

    public int size() {
        return map.size();
    }

    public Collection values() {
        return map.values();
    }

}
