package com.punwire.oa.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.punwire.oa.domain.OaMenu;
import com.punwire.oa.services.OaMenuS;
import com.punwire.oa.ui.OaLayoutR;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.StringWriter;

/**
 * Created by kanwal on 1/3/14.
 */
@Stateless
public class OaController {
    protected static ObjectMapper mapper = new ObjectMapper();
    protected static OaLayoutR layout = new OaLayoutR();
    protected boolean wrapLayout=false;
    protected OaSession oaSession;

    private String appPath = "h:\\projects\\OaWeb\\app";


    @Inject
    OaMenuS menuS;

    public OaController() {
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
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
}
