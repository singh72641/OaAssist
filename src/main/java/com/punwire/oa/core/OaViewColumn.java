package com.punwire.oa.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.punwire.oa.domain.SysListValue;
import org.apache.commons.lang.WordUtils;

import java.util.List;

/**
 * Created by kanwal on 1/22/14.
 */
public class OaViewColumn {
    ObjectNode col;
    OaView view;

    public OaViewColumn(ObjectNode c, OaView v)
    {
        this.col = c;
        this.view = v;
    }

    public OaViewColumn(OaObject c, OaView v)
    {
        this.col = c.node;
        this.view = v;
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

    public Boolean isSearchable()
    {
        if(col.has("search")) return col.get("search").asBoolean();
        return false;
    }

    public String getDisplayType()
    {
        return getOrDefault("displayType","text").toLowerCase();
    }

    public Boolean isHidden()
    {
        if ( getOrDefault("displayType","text").equals("hidden") ) return true;
        return false;
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
        Boolean v = true;
        if ( col.has("visible") )  v = col.get("visible").asBoolean();

        if( view.isSearchForm  ) return isSearchable();
        return v;
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

    public String bind(JsonNode row)
    {
        String f = getOrDefault("bind",getName());
        if( row.has(f))
        {
            return row.get(f).asText();
        }
        return "";
    }

    public List<SysListValue> getLov(String name)
    {
        return view.getLov(name);
    }
}
