package com.punwire.oa.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.punwire.oa.core.*;
import com.punwire.oa.domain.SmComponent;
import com.punwire.oa.sm.SmCompObjectListR;
import com.punwire.oa.sm.SmComponentDetailR;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.StringWriter;
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


    @PostConstruct
    public void init() {
        OaDefaults.viewS = viewS;
    }

    /**
     * ************************************************************************
     * This section is for UI
     * *************************************************************************
     */

    //This will return list of Ccmponents
    @GET
    public String searchComponent() {
        OaObject param = new OaObject();
        return aListComponent(param);
    }


    //This will return Add Component Form
    @GET
    @Path("add")
    public String addComponent() {
        OaObject param = new OaObject();
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
        return searchComponent();
    }

    //This will return list of Ccmponents
    @GET
    @Path("detail/{compName}")
    public String viewComponent(@PathParam("compName") String compName) {
        OaObject param = new OaObject();
        param.put("_param0", compName);

        return aDetailComponent(param);
    }

    @GET
    @Path("edit/{compName}")
    public String editComponent(@PathParam("compName") String compName) {
        OaObject param = new OaObject();
        param.put("_param0", compName);
        return aDetailComponent(param);
    }

    //This will return list of Ccmponents
    @GET
    @Path("addObject/{compName}")
    public String addObject(@PathParam("compName") String compName) {
        OaObject param = new OaObject();
        param.put("_param0", compName);

        OaViewResult o = viewS.buildView("sm/smObjectAdd", param);
        return o.content;
    }

    @GET
    @Path("objectlist/{id}")
    public String editComponent(@PathParam("id") Long id) {
        StringWriter writer = new StringWriter();
        try {
            new SmCompObjectListR().render(writer, this, id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return writer.toString();
    }


    /**
     * ************************************************************************
     * This section is for Ui Actions
     * *************************************************************************
     */

    public String aListComponent(OaObject param) {
        StringWriter writer = new StringWriter();
        try {
            //new SmComponentR().render(writer,"sm/smComponentDetail",row,viewS);
            JsonNode row = viewS.build(writer, "sm/smComponentList", param, false);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return writer.toString();
    }

    public String aDetailComponent(OaObject param) {
        StringWriter writer = new StringWriter();
        try {
            //Build Component Details View

//             JsonNode row = viewS.build(writer, "sm/smComponentDetail",param,false);
//
//             //Get Id and pass to Object List View
//             OaObject oParam = new OaObject();
//             oParam.put("comp_id",row.get("id").asLong());
//             System.out.println("Getting Object List " + oParam.toString());
//
//             row = viewS.build(writer, "sm/smComponentObjectList",oParam,false);

            //JsonNode row = viewS.build(writer, "sm/smTest",param,false);
            ObjectNode row = viewS.getViewRow("sm/smComponentDetail", param);
            new SmComponentDetailR().render(writer, this, new OaObject(row));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return writer.toString();
    }


    /**
     * ************************************************************************
     * This section is for Data Processing
     * *************************************************************************
     */

    public ArrayNode qComponentObjects(Long id) {
        OaObject param = new OaObject();
        param.put("comp_id", id);
        ArrayNode rows = viewS.getViewRows("sm/smComponentObjectList", param);
        return rows;
    }

    public SmComponent findById(Long id) {

        return this.entityManager.find(SmComponent.class, id);
    }

    public void save(SmComponent entity) {
        entityManager.persist(entity);
    }


    public SmComponent add(ObjectNode entity) {

        System.out.println("Adding SmComponent " + entity.toString());
        SmComponent component = new SmComponent();
        if (entity.has("id")) {
            component = entityManager.find(SmComponent.class, entity.get("id").asLong());
        }
        component.setComponentNum(entity.get("component_num").asText());
        component.setName(entity.get("name").asText());
        component.setDescription(entity.get("description").asText());
        component.setComponentType(entity.get("component_type").asText());
        component.setTrack(entity.get("track").asText());
        component.setModule(entity.get("module").asText());
        component.setSmStage(entity.get("sm_stage").asText());
        String startDate = entity.get("sm_stage_date").asText();
        try {
            SimpleDateFormat sd = new SimpleDateFormat("MM/dd/yyyy");
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
