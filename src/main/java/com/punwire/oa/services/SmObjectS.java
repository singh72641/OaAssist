package com.punwire.oa.services;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.punwire.oa.core.OaController;
import com.punwire.oa.core.OaJpaMapper;
import com.punwire.oa.core.OaObject;
import com.punwire.oa.core.OaViewResult;
import com.punwire.oa.domain.SmComponent;
import com.punwire.oa.domain.SmIssue;
import com.punwire.oa.domain.SmObject;
import com.punwire.oa.domain.SmObjectSource;
import com.punwire.oa.sm.SmCompIssueListR;
import com.punwire.oa.sm.SmDbObjectViewR;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.StringWriter;
import java.util.List;

/**
 * Created by kanwal on 1/20/14.
 */
@Stateless
@Path("/SmObject")
public class SmObjectS extends OaController {
    @Inject
    private OaViewS viewS;

    @Context
    HttpServletRequest req;

    @PersistenceContext
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
        OaObject param = new OaObject();
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

    //This will display source for the object
    @GET
    @Path("view/{id}")
    public String viewObject(@PathParam("id") Long id) {
        StringWriter writer = new StringWriter();
        SmObject obj = getObjectById(id);
        try {
            new SmDbObjectViewR().render(writer, this, obj);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return writer.toString();
    }

    @GET
    @Path("view/{id}/{page}")
    public String viewObjectPage(@PathParam("id") Long id, @PathParam("page") int page) {
        StringWriter writer = new StringWriter();
        SmObject obj = getObjectById(id,page);
        try {
            new SmDbObjectViewR().render(writer, this, obj);
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

    public SmObject getObjectById(Long id) {
        return getObjectById(id, 0);
    }

    public SmObject getObjectById(Long id, int page) {
        SmObject object = entityManager.find(SmObject.class, id);

        if (object.getObjectType().startsWith("PACKAGE")) {
            //Get Source from database
            String sql = "select line, text from all_source \n" +
                    "where name = '" + object.getName() + "'\n" +
                    "and type = '" + object.getObjectType() + "'\n" +
                    "and line between " + (page * 50) + " and " + (page + 1) * 50 + "\n" +
                    "order by line\n";
            Query q = entityManager.createNativeQuery(sql, "SmObjectSourceV");

            //List<SmObjectSource> lines = new OaJpaMapper().getList(q,SmObjectSource.class);

            List<SmObjectSource> lines = q.getResultList();

            System.out.println("DB Source Lines " + lines.size());

            System.out.println("0:" + lines.get(0).getLineText());
            System.out.println("0:" + lines.get(1).getLineText());
            System.out.println("0:" + lines.get(2).getLineText());

            object.setDbSource(lines);
        }
        return object;
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
