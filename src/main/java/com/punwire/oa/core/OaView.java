package com.punwire.oa.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.punwire.oa.domain.SysListValue;
import com.punwire.oa.services.SysListS;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by klobana on 1/22/14.
 */
public class OaView {
    static ObjectMapper mapper = new ObjectMapper();
    ObjectNode view;
    ArrayNode cols;
    SysListS listS;
    String viewName;
    Boolean isSearchForm=false;
    Boolean isReadOnly=false;
    HashMap<String,OaObject> columnMap=null;

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

    public void setReadOnly(Boolean rOnly)
    {
        this.isReadOnly = rOnly;
    }

    public Boolean isReadOnly(){
        return isReadOnly;
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

    public String getTemplate()
    {
        return getOrDefault("type","OaFormR");
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

    public Boolean hasLayout()
    {
        if( view.has("layout") ) return true;
        return false;
    }

    public OaPanel getFormPanel()
    {
        ObjectNode layout;
        if( ! hasLayout() ) layout = genFormLayout();
        else layout = (ObjectNode)view.get("layout");

        return new OaPanel(layout);
    }

    public List<SysListValue> getLov(String name)
    {
        return listS.getValues(name);
    }

    public HashMap<String, OaObject> getColumnMap()
    {
        if( columnMap != null ) return columnMap;
        columnMap = new HashMap<>();
        int cCount = cols.size();
        for(int c=0;c<cCount;c++ )
        {
            OaObject col = new OaObject( (ObjectNode)cols.get(c) );
            columnMap.put( col.s("name"),col );
        }
        return columnMap;
    }

    public Boolean isList()
    {
        if( ! view.has("queryResult") ) return false;

        if( view.get("queryResult").asText().equals("List")) return true;

        return false;
    }

    public OaObject o(String name)
    {
        if( view.has(name) ) return new OaObject((ObjectNode)view.get(name));
        return null;
    }

    public ObjectNode genFormLayout()
    {
        ObjectNode layout = mapper.createObjectNode();

        int colCount = cols.size();
        for(int c=0;c<colCount;c++)
        {
            ObjectNode row = mapper.createObjectNode();
            ObjectNode col = (ObjectNode) cols.get(c);
            ObjectNode inputField = mapper.createObjectNode();
            inputField.put("name",col.get("name"));
            row.put("OaInputFieldR_1", inputField);
            layout.put("OaRowR_"+c,row);
        }
        return layout;
    }

}
