package com.punwire.oa.services;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.punwire.oa.core.OaController;
import com.punwire.oa.core.OaViewResult;
import com.punwire.oa.domain.SmComponent;
import com.punwire.oa.domain.SmObject;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * Created by kanwal on 1/20/14.
 */
@Stateless
@Path("/smObject")
public class SmObjectS extends OaController{
    @Inject
    private OaViewS viewS;

    @Context
    HttpServletRequest req;

    @PersistenceContext(type = PersistenceContextType.TRANSACTION)
    private EntityManager entityManager;

    /**
     * ************************************************************************
     * This section is for UI
     * *************************************************************************
     */

    //This will return Add Component Form
    @GET
    @Path("add")
    public String addObject() {
        ObjectNode param = newObject();
        OaViewResult o = viewS.buildView("sm/smObjectAdd", param);
        return o.content;
    }

    //This will process Add Component Form
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("add")
    public String saveComponent(ObjectNode form) {
        //Save the component Details
        add(form);
        return addObject();
    }

    public SmObject add(ObjectNode entity) {
        SmObject object = new SmObject();
        object.setName(entity.get("name").asText());
        object.setObjectSource(entity.get("object_source").asText());
        object.setObjectType(entity.get("object_type").asText());
        entityManager.persist(object);
        return object;
    }
}
