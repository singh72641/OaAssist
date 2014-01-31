package com.punwire.oa.core;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Created by kanwal on 1/24/14.
 */
public class OaObjectList {
    public ArrayNode list;

    public OaObjectList(ArrayNode list)
    {
        this.list = list;
    }

    public int size()
    {
        return list.size();
    }

    public OaObject get(int i)
    {
        return new OaObject((ObjectNode)list.get(i));
    }
}
