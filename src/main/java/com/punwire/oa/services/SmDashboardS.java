package com.punwire.oa.services;

import com.punwire.oa.core.OaController;
import com.punwire.oa.domain.SmComment;
import com.punwire.oa.sm.SmCommentListR;
import com.punwire.oa.sm.SmDashboardR;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import java.io.StringWriter;
import java.util.List;

/**
 * Created by kanwal on 1/30/14.
 */
@Stateless
@Path("/SmDashboard")
public class SmDashboardS extends OaController {
    @Context
    HttpServletRequest req;

    @PersistenceContext
    private EntityManager entityManager;


    @GET
    public String home() {
        StringWriter writer = new StringWriter();

        try{
            new SmDashboardR().render( writer,this);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return writer.toString();
    }

}
