package com.punwire.oa.services;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.punwire.oa.core.OaConfig;
import com.punwire.oa.core.OaController;
import com.punwire.oa.core.OaDefaults;
import com.punwire.oa.core.OaSession;
import com.punwire.oa.domain.OaMenu;
import com.punwire.oa.ui.OaLayoutR;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by kanwal on 1/16/14.
 */
@Stateless
@Path("/")
public class OaHomeS extends OaController{

    private static Logger LOG = Logger.getLogger("OaHomeS");

    @Inject
    OaConfig config;

    @Inject
    private OaViewS viewS;

    @GET
    @Path("{viewName}")
    public String getView(@Context HttpServletRequest req, @PathParam("viewName") String viewName) {
        getSession(req);
        if( viewName.equals("home") )
        {
            OaDefaults.setConfig(config);
            config.setBaseURI(req.getContextPath());
            System.out.println( " >>> " + req.getContextPath());
            System.out.println( " >>> " + req.getPathInfo());
            System.out.println( " >>> " + req.getRequestURI());

            return layoutCenter("");
        }
        LOG.log(Level.INFO, "ui Post Controller for view " + viewName);
        ObjectNode input = newObject();
        return render(viewS.buildView("query/" + viewName, input).content);
    }

    @GET
    @Path("{viewName}/{param1}")
    public String getView(@Context HttpServletRequest req, @PathParam("viewName") String viewName, @PathParam("param1") String param1) {
        LOG.log(Level.INFO, "ui Post Controller for view " + viewName + " param " + param1);
        ObjectNode input = newObject();
        input.put("_param"+0,param1);
        return render(viewS.buildView("query/" + viewName, input).content);
    }
}
