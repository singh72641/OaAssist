package com.punwire.oa.services;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.punwire.oa.core.OaController;
import com.punwire.oa.core.OaViewResult;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

/**
 * Created by kanwal on 1/20/14.
 */

@Path("/front")
@Stateless
public class OaFrontS extends OaController {

    @Context
    HttpServletRequest req;

    @Inject
    private OaViewS viewS;

    @GET
    @Path("{module}/{viewName}")
    public String get(@PathParam("module") String module, @PathParam("viewName") String viewName) {
        OaViewResult o = viewS.buildView(module+ "/" + viewName, newObject());
        return o.content;
    }

}
