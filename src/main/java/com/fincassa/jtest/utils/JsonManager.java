package com.fincassa.jtest.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Map;

/**
 * Created by vyn on 14.11.2016.
 */
public class JsonManager {
    private static final ObjectMapper _jsonMapper= new ObjectMapper();

    public static String encodeToJson(Map<String,String> pData) {
        ObjectNode out= _jsonMapper.createObjectNode();

        for(String key: pData.keySet()) {
            out.put(key,pData.get(key));
        }

        return out.toString();
    }

}
