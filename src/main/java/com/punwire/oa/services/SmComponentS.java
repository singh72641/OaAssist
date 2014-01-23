package com.punwire.oa.services;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.punwire.oa.core.OaController;
import com.punwire.oa.core.OaSession;
import com.punwire.oa.core.OaViewResult;
import com.punwire.oa.domain.SmComponent;
import com.punwire.oa.sm.SmComponentR;


import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.io.StringWriter;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by kanwal on 1/16/14.
 */
@Stateless
@Path("/comp")
public class SmComponentS extends OaController {


    @Inject
    private OaViewS viewS;

    @Inject
    private SmObjectS objectS;

    @Context
    HttpServletRequest req;

    @PersistenceContext(type = PersistenceContextType.TRANSACTION)
    private EntityManager entityManager;

    /**
     * ************************************************************************
     * This section is for UI
     * *************************************************************************
     */

    //This will return list of Ccmponents
    @GET
    public String searchComponent() {
        ObjectNode param = newObject();
        OaViewResult o = viewS.buildView("sm/smComponentList", param);
        return o.content;
    }


    //This will return Add Component Form
    @GET
    @Path("add")
    public String addComponent() {
        ObjectNode param = newObject();
        OaViewResult o = viewS.buildView("sm/smComponentAdd", param);
        return o.content;
    }

    //This will process Add Component Form
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("add")
    public String saveComponent(ObjectNode form) {
        //Save the component Details
        add(form);
        return addComponent();
    }

    //This will return list of Ccmponents
    @GET
    @Path("detail/{compName}")
    public String viewComponent(@PathParam("compName") String compName) {
        ObjectNode param = newObject();
        param.put("_param0",compName);

        ObjectNode row = viewS.getViewRow("sm/smComponentDetail",param);
        System.out.println(row.toString());
        StringWriter writer = new StringWriter();
        try
        {
            new SmComponentR().render(writer,"sm/smComponentDetail",row,viewS);
        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return writer.toString();
    }

    @GET
    @Path("addObject/{compName}")
    public String addObject(@PathParam("compName") String compName) {
        ObjectNode param = newObject();
        param.put("_param0",compName);
        OaViewResult o = viewS.buildView("sm/smObjectAdd", param);
        return o.content;
    }


    /**
     * ************************************************************************
     * This section is for Data Processing
     * *************************************************************************
     */
    public SmComponent findById(Long id) {

        return this.entityManager.find(SmComponent.class, id);
    }

    public void save(SmComponent entity) {
        entityManager.persist(entity);
    }


    public SmComponent add(ObjectNode entity) {
        SmComponent component = new SmComponent();
        component.setComponentNum(entity.get("component_num").asText());
        component.setName(entity.get("name").asText());
        component.setDescription(entity.get("description").asText());
        component.setComponentType(entity.get("component_type").asText());
        component.setTrack(entity.get("track").asText());
        component.setModule(entity.get("module").asText());
        component.setSmStage(entity.get("sm_stage").asText());
        String startDate = entity.get("sm_stage_date").asText();
        try {
            SimpleDateFormat sd = new SimpleDateFormat("MM/DD/YYYY");
            java.util.Date d = sd.parse(startDate);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            component.setStartDate(c);
        } catch (Exception ex) {

        }
        entityManager.persist(component);
        return component;
    }


}
