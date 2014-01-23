package com.punwire.oa.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.punwire.oa.domain.SysListValue;
import com.punwire.oa.services.OaViewS;
import com.punwire.oa.services.SysListS;

import java.io.File;
import java.util.List;

/**
 * Created by klobana on 1/22/14.
 */
public class OaView {
    ObjectMapper mapper = new ObjectMapper();
    ObjectNode view;
    ArrayNode cols;
    SysListS listS;

    public OaView(String name, SysListS ls)
    {
        this.listS = ls;
        String file = OaDefaults.getAppPath()  + File.separator + "ui" + File.separator + name + ".json";
        try
        {
            view =  (ObjectNode)mapper.readTree(new File(file));
            cols = (ArrayNode)view.get("columns");

        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public ObjectNode getView()
    {
        return view;
    }

    public String s(String field)
    {
        if(view.has(field)) return view.get(field).asText();
        return "";
    }

    public String getMethod()
    {
        return getOrDefault("method","POST");
    }

    public String getAction()
    {
        return getOrDefault("action","");
    }

    public String getName()
    {
       return getOrDefault("name","frmDefault1");
    }

    public Object get(String field)
    {
        return view.get(field);
    }

    public String getOrDefault(String field,String defaultValue)
    {
        if ( view.has(field) )  return view.get(field).asText();
        return defaultValue;
    }

    public OaViewColumn getColumn(int c)
    {
        return new OaViewColumn((ObjectNode) cols.get(c));
    }

    public int getColCount()
    {
        return cols.size();
    }

    public void layout()
    {

    }

    public List<SysListValue> getLov(String name)
    {
        return listS.getValues(name);
    }
}
