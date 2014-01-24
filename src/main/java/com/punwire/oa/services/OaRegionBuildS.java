package com.punwire.oa.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.punwire.oa.core.OaConfig;
import com.punwire.oa.core.OaController;
import com.punwire.oa.core.OaView;
import com.punwire.oa.core.OaViewResult;
import com.punwire.oa.ui.OaFormR;
import com.punwire.oa.ui.OaFormTableR;
import com.punwire.oa.ui.TestFormR;
import com.punwire.oa.ui.TestPageR;
import com.sun.org.apache.xpath.internal.SourceTree;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.io.StringWriter;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by kanwal on 1/17/14.
 */

@Path("/build/region")
@Stateless
public class OaRegionBuildS extends OaController {

    private static Logger LOG = Logger.getLogger("OaRegionBuildS");

    @Inject
    OaConfig config;

    @Inject
    private OaViewS viewS;

    @Inject
    private OaRegionS regionS;

    @Inject
    private SysListS sysListS;

    @Context
    HttpServletRequest req;

    @GET
    @Produces("application/json")
    public String addRegion() {
        ObjectNode view = viewS.getView("query/uRegionAdd");
        return view.toString();

    }

    @GET
    @Produces("text/html")
    public String addRegionUi() {
        OaViewResult o = viewS.buildView("query/uRegionAdd", newObject());
        return o.content;
    }


    @GET
    @Produces("text/html")
    @Path("testu/{viewName}")
    public String testUi(@PathParam("viewName") String viewName) {
        getSession(req);
        System.out.println("initializing view " + "test/" + viewName);
        OaView view = new OaView("test/" + viewName, sysListS);
        StringWriter writer = new StringWriter();
        try {
            new TestFormR().render(writer, view, newObject());

            ObjectNode con = newObject();
            con.put("center",writer.toString());
            writer = new StringWriter();
            System.out.println(con.toString());
            new TestPageR().render(writer,con,oaSession);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return writer.toString();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String saveRegion(ObjectNode form) {
        System.out.println("In the post " + form.toString());
        regionS.add(form);
        //Map<String,String[]> form = request.getParameterMap();
        return form.toString();
    }

    @GET
    @Path("list/{fileName}")
    public String addRegion(@Context HttpServletRequest req, @PathParam("fileName") String fileName) {
        System.out.println("Loading list file " + fileName);
        sysListS.loadFile(fileName);
        return "File Loaded";
    }


    @GET
    @Path("{viewName}")
    @Produces("text/html")
    public String editRegion(@Context HttpServletRequest req, @PathParam("viewName") String viewName) {
        ObjectNode param = newObject();
        param.put("name", viewName);
        OaViewResult o = viewS.buildView("query/uRegionDetail", param);
        return o.content;
    }

    @GET
    @Path("{tableName}/gen")
    public String genRegion(@Context HttpServletRequest req, @PathParam("tableName") String tableName) {
        ArrayNode cols = regionS.generate(tableName);
        return cols.toString();
    }

    @GET
    @Path("{viewName}/columns")
    public String genColumns(@Context HttpServletRequest req, @PathParam("viewName") String viewName) {
        ObjectNode region = regionS.getRegion(viewName);
        ArrayNode cols = (ArrayNode) region.get("columns");
        ObjectNode view = getView("query/uRegionColumns");
        StringWriter writer = new StringWriter();
        try {
            new OaFormTableR().render(writer, view, cols, viewS);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return writer.toString();
    }

    @GET
    @Path("{viewName}/add")
    public String addColumn(@Context HttpServletRequest req, @PathParam("viewName") String viewName) {
        ObjectNode region = regionS.getRegion(viewName);

        ObjectNode row = mapper.createObjectNode();

        OaView view = new OaView("query/uRegionColumnAdd",sysListS);
        StringWriter writer = new StringWriter();
        try {
            new OaFormR().render(writer, view, row);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return writer.toString();
    }
}