package com.punwire.oa.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Map;


/**
 * Created by kanwal on 1/4/14.
 */
public class OaUiHelper {
    static ObjectMapper mapper = new ObjectMapper();

    public static String getString(ObjectNode node, String field)
    {
        if( node.has(field))
        {
            return node.get(field).asText();
        }
        return "";
    }

    public static String get(ObjectNode row, ObjectNode col, String field)
    {
        if( ! col.has(field) ) return "";
        String f = col.get(field).asText();

        if( row.has(f))
        {
            return row.get(f).asText();
        }
        return "";
    }

    public static ObjectNode toObjectNode(Map<String, String> input)
    {
        ObjectNode out = mapper.createObjectNode();
        for(String key: input.keySet())
        {
            out.put(key,input.get(key));
        }
        return out;
    }
}
