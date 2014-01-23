package com.punwire.oa.core;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Created by kanwal on 1/16/14.
 */
public class OaViewResult {
    public String content;
    public JsonNode data;

    public OaViewResult(String content)
    {
        this(content, null);
    }

    public OaViewResult(String content, JsonNode data)
    {
        this.content = content;
        this.data = data;
    }

    public String toString(){
        return content;
    }
}
