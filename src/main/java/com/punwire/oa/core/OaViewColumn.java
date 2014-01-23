package com.punwire.oa.core;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang.WordUtils;

/**
 * Created by kanwal on 1/22/14.
 */
public class OaViewColumn {
    ObjectNode col;

    public OaViewColumn(ObjectNode c)
    {
        this.col = c;
    }

    public Object get(String field)
    {
        return col.get(field);
    }

    public String s(String field)
    {
        if(col.has(field)) return col.get(field).asText();
        return "";
    }

    public String getOrDefault(String field,String defaultValue)
    {
        if ( col.has(field) )  return col.get(field).asText();
        return defaultValue;
    }

    public String getDisplayType()
    {
        return getOrDefault("displayType","text").toLowerCase();
    }

    public boolean isTextArea()
    {
        return getDisplayType().equals("textarea");
    }

    public boolean isSelectBox()
    {
        return getDisplayType().equals("select");
    }

    public Boolean isVisible()
    {
        if ( col.has("visible") )  return col.get("visible").asBoolean();
        return true;
    }

    public boolean has(String field)
    {
        return col.has(field);
    }

    public String getType()
    {
        return getOrDefault("type","text").toLowerCase();
    }

    public String getSize()
    {
        return getOrDefault("size", "lg-3");
    }

    public String getName()
    {
        return s("name");
    }

    public String getPrompt()
    {
        return getOrDefault("prompt", WordUtils.capitalizeFully(getName()));
    }

    public String bind(ObjectNode row)
    {
        String f = getOrDefault("bind",getName());
        if( row.has(f))
        {
            return row.get(f).asText();
        }
        return "";
    }
}
