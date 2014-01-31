package com.punwire.oa.core;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Iterator;

/**
 * Created by kanwal on 1/24/14.
 */
public class OaObject {
    public ObjectNode node;

    public OaObject()
    {
        node = OaDefaults.mapper.createObjectNode();
    }

    public OaObject(ObjectNode node)
    {
        this.node = node;
    }

    public Boolean b(String name)
    {
        return b(name,null);
    }
    public Boolean b(String name,Boolean defaultValue)
    {
        if(node.has(name)) return node.get(name).asBoolean();
        return defaultValue;
    }

    public Long l(String name)
    {
        return node.get(name).asLong();
    }

    public String s(String name)
    {
        return s(name,"");
    }
    public String s(String name,String defaultValue)
    {
        if(node.has(name)) return node.get(name).asText();
        return defaultValue;
    }

    public String toString()
    {
        return node.toString();
    }

    public Boolean has(String field)
    {
        return node.has(field);
    }

    public Iterator<String> fieldNames()
    {
        return node.fieldNames();
    }

    public OaObject put(String field, String value)
    {
        node.put(field, value);
        return this;
    }

    public OaObject put(String field, Long value)
    {
        node.put(field, value);
        return this;
    }
    public OaObject put(String field, Boolean value)
    {
        node.put(field, value);
        return this;
    }
}
