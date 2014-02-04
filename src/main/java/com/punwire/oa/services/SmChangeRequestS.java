package com.punwire.oa.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.punwire.oa.core.OaController;
import com.punwire.oa.core.OaDefaults;
import com.punwire.oa.core.OaObject;
import com.punwire.oa.core.OaViewResult;
import com.punwire.oa.domain.SmChangeRequest;
import com.punwire.oa.domain.SmComponent;
import com.punwire.oa.sm.SmCompCrListR;
import com.punwire.oa.sm.SmCrDetailR;
import com.punwire.oa.sm.SmCrListR;
import org.springframework.jdbc.core.RowMapper;

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
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by kanwal on 1/26/14.
 */
@Stateless
@Path("/cr")
public class SmChangeRequestS extends OaController {

    @Inject
    private OaViewS viewS;

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

    //This will return Add Change Request Form
    @GET
    @Path("add")
    public String addComponent() {
        SmChangeRequest cr = new SmChangeRequest();
        cr.setCrOpenDate(Calendar.getInstance());
        cr.setCrOwner("KSINGH");
        ObjectNode ret = newObject();
        StringWriter writer = new StringWriter();
        try {
            new SmCrDetailR().render(writer, this, cr);
            ret.put("ui", writer.toString());
            ret.put("fdata", toJson(cr));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ret.toString();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("add")
    public String saveChangeRequest(ObjectNode form) {
        //Save the component Details
        add(form);
        return addComponent();
    }

    @GET
    @Path("forcomp/{compId}")
    public String listForComponent(@PathParam("compId") Long compId) {
        StringWriter writer = new StringWriter();

        List<SmChangeRequest> list = getCompCrList(compId);
        try {
            new SmCompCrListR().render(writer, this, list);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return writer.toString();
    }

    //This will return List Change Requests
    @GET
    public String listChangeRequests() {
        OaObject param = new OaObject();
        StringWriter writer = new StringWriter();

        List<SmChangeRequest> list = getCrList();

        try {
            new SmCrListR().render(writer, this, list);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return writer.toString();
    }

    @GET
    @Path("detail/{id}")
    public String editCr(@PathParam("id") Long id) {
        SmChangeRequest cr = getCrById(id);
        ObjectNode ret = newObject();
        StringWriter writer = new StringWriter();
        try {
            new SmCrDetailR().render(writer, this, cr);
            ret.put("ui", writer.toString());
            ret.put("fdata", toJson(cr));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ret.toString();
    }

    public ObjectNode toJson(SmChangeRequest cr) {
        ObjectNode o = newObject();
        o.put("id", cr.getId());
        o.put("crNumber", cr.getCrNumber());
        o.put("crName", cr.getCrName());
        o.put("crDescription", cr.getCrDescription());
        o.put("crType", cr.getCrType());
        o.put("crStatus", cr.getCrStatus());
        o.put("crOpenDate", formatDate(cr.getCrOpenDate()));
        o.put("crCloseDate", formatDate(cr.getCrCloseDate()));
        o.put("crTargetDate", formatDate(cr.getCrTargetDate()));
        o.put("crOwnerId", cr.getCrOwnerId());
        o.put("crCompId", cr.getCrCompId());
        o.put("crOwner", cr.getCrOwner());
        o.put("crComponentName", cr.getCrComponentName());
        return o;
    }


    /**
     * ************************************************************************
     * This section is for Ui Actions
     * *************************************************************************
     */

    /**
     * ************************************************************************
     * This section is for Data Processing
     * *************************************************************************
     */

    public List<SmChangeRequest> getCrList() {
        String sql = "select u.display_name cr_owner, cr.*  \n" +
                "from sm_change_requests cr, sm_users u \n" +
                "where cr.cr_owner_id = u.id and cr_close_date is null";

        List<SmChangeRequest> list = entityManager.createNativeQuery(sql, "SmChangeRequestV")
                .getResultList();
        return list;
    }

    public List<SmChangeRequest> getCompCrList(Long id) {
        String sql = "select u.display_name cr_owner, co.name cr_comp, cr.*  \n" +
                "from sm_change_requests cr, sm_users u, sm_components co \n" +
                "where cr.cr_owner_id = u.id and cr.comp_id = co.id and co.id = ?1";

        List<SmChangeRequest> list = entityManager.createNativeQuery(sql, "SmChangeRequestV")
                .setParameter(1, id)
                .getResultList();
        return list;
    }

    public SmChangeRequest getCrById(Long id) {
        String sql = "select u.display_name cr_owner, co.name cr_comp, cr.*  \n" +
                "from sm_change_requests cr, sm_users u, sm_components co \n" +
                "where cr.cr_owner_id = u.id and cr.comp_id = co.id and cr.id = ?1";

        SmChangeRequest cr = (SmChangeRequest) entityManager.createNativeQuery(sql, "SmChangeRequestV")
                .setParameter(1, id)
                .getSingleResult();

        return cr;
    }

    public SmChangeRequest add(ObjectNode entity) {
        System.out.println("Adding SmChangeRequest " + entity.toString());
        SmChangeRequest cr = new SmChangeRequest();
        if (entity.has("id")) {
            cr = entityManager.find(SmChangeRequest.class, entity.get("id").asLong());
        }
        cr.setCrName(entity.get("cr_name").asText());
        cr.setCrDescription(entity.get("cr_description").asText());
        cr.setCrType(entity.get("cr_type").asText());

        Calendar c = getDate(entity.get("cr_open_date").asText());
        if (c != null) cr.setCrOpenDate(c);

        c = getDate(entity.get("cr_close_date").asText());
        if (c != null) cr.setCrCloseDate(c);
        c = getDate(entity.get("cr_target_date").asText());
        if (c != null) cr.setCrTargetDate(c);

        cr.setCrOwner(entity.get("cr_owner").asText());
        cr.setCrCompId(new Long("1"));

        entityManager.persist(cr);

        return cr;
    }
}
