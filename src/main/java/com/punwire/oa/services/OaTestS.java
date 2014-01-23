package com.punwire.oa.services;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

/**
 * Created by kanwal on 1/18/14.
 */
@Path("/build/test")
public class OaTestS {

    public OaTestS()
    {
        System.out.println("TESTING..");
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String saveRegion( ObjectNode form) {
        System.out.println("In the post " + form.toString());
        //Map<String,String[]> form = request.getParameterMap();
        return form.toString();
    }

}
