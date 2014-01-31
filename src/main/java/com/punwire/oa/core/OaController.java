package com.punwire.oa.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.punwire.oa.domain.OaMenu;
import com.punwire.oa.services.OaMenuS;
import com.punwire.oa.services.SysListS;
import com.punwire.oa.ui.OaLayoutR;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by kanwal on 1/3/14.
 */
@Stateless
public class OaController {
    protected final ObjectMapper mapper = new ObjectMapper();
    protected final SimpleDateFormat sd = new SimpleDateFormat("MM/dd/yyyy");

    protected static OaLayoutR layout = new OaLayoutR();
    protected boolean wrapLayout=false;
    protected OaSession oaSession;

    private String appPath = "h:\\projects\\OaWeb\\app";


    @Inject
    OaMenuS menuS;

    @Inject
    SysListS lovService;

    public OaController() {
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public SysListS getLovService()
    {
        return lovService;
    }

    public OaSession getSession(HttpServletRequest req){
        HttpSession session= req.getSession(true);
        oaSession = (OaSession)session.getAttribute("oasession");
        if( oaSession == null )
        {
            oaSession = new OaSession();
            session.setAttribute("oasession",oaSession);

            //New Session, Initialize Menu
            OaMenu menu = menuS.getMenu("OaHomePageLeft");
            oaSession.setMenu(menu);
        }
        return oaSession;
    }

    public String layout(ObjectNode content) {
        StringWriter writer = new StringWriter();
        try {
            new OaLayoutR().render(writer, content,oaSession);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return writer.toString();
    }

    public String layoutCenter(String center) {
        ObjectNode content = mapper.createObjectNode();
        content.put("center", center);

        StringWriter writer = new StringWriter();
        try {
            new OaLayoutR().render(writer, content,oaSession);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("LayoutCenter >>>>>>>>>>>");
        System.out.println(writer.toString());
        return writer.toString();
    }

    public ObjectNode getView(String name) {
        try {
            String fName = appPath + File.separator + "ui" + File.separator + name + ".json";
            return (ObjectNode) mapper.readTree(new File(fName));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public ObjectNode newObject(){
        return mapper.createObjectNode();
    }

    public ArrayNode newArray() {
        return mapper.createArrayNode();
    }

    public String render(StringWriter content)
    {
        if(wrapLayout)
        {
            return layoutCenter(content.toString());
        }
        else
        {
            return content.toString();
        }
    }

    public String render(String content)
    {
        if(wrapLayout)
        {
            return layoutCenter(content);
        }
        else
        {
            return content;
        }
    }

    public Calendar getDate(String input){

        if( input == null || input.length() < 6) return null;
        Calendar c = Calendar.getInstance();
        try {
            java.util.Date d = sd.parse(input);
            c.setTime(d);
            return c;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String formatDate(Calendar input){

        if( input == null ) return "";
        try {
            String out = sd.format(input.getTime());
            return out;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return input.getTime().toString();
    }
}
