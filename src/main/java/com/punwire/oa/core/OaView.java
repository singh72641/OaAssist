package com.punwire.oa.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.punwire.oa.domain.SysListValue;
import com.punwire.oa.services.OaViewS;
import com.punwire.oa.services.SysListS;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.File;
import java.util.Iterator;
import java.util.List;

/**
 * Created by klobana on 1/22/14.
 */
public class OaView {
    ObjectMapper mapper = new ObjectMapper();
    ObjectNode view;
    ArrayNode cols;
    SysListS listS;
    String viewName;
    Boolean isSearchForm=false;

    public OaView(String name, SysListS ls)
    {
        this.listS = ls;
        this.viewName = name;
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

    public Boolean has(String field)
    {
        return view.has(field);
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

    public void setSearch(Boolean s)
    {
        this.isSearchForm = s;
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
        return new OaViewColumn((ObjectNode) cols.get(c),this);
    }

    public int getColCount()
    {
        return cols.size();
    }

    public void layout()
    {
        String file = OaDefaults.getAppPath()  + File.separator + "ui" + File.separator + viewName + "L.json";
        try
        {
            ObjectNode viewLayout =  (ObjectNode)mapper.readTree(new File(file));
            Iterator<String> fields = viewLayout.fieldNames();
            while(fields.hasNext())
            {
                String field = fields.next();
                view.set(field, viewLayout.get(field));
            }
            ArrayNode colLayouts = (ArrayNode)view.get("columns");

            for(int c=0;c<colLayouts.size();c++)
            {
                //Apply Column Layouts
                ObjectNode colLayout = (ObjectNode)colLayouts.get(c);
                ObjectNode col = (ObjectNode)colLayouts.get(c);
                fields = colLayout.fieldNames();
                while(fields.hasNext())
                {
                    String field = fields.next();
                    view.set(field, viewLayout.get(field));
                }
            }

        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public List<SysListValue> getLov(String name)
    {
        return listS.getValues(name);
    }
}
