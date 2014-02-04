package com.punwire.oa.services;

import com.punwire.oa.core.OaController;
import com.punwire.oa.core.OaObject;
import com.punwire.oa.domain.SmChangeRequest;
import com.punwire.oa.domain.SmComment;
import com.punwire.oa.domain.SmObject;
import com.punwire.oa.sm.SmCommentListR;
import com.punwire.oa.sm.SmCrListR;

import javax.ejb.Stateless;
import javax.inject.Inject;
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
@Path("/SmComment")
public class SmCommentS extends OaController{
    @Context
    HttpServletRequest req;

    @PersistenceContext
    private EntityManager entityManager;



    @GET
    @Path("for/{linkType}/{linkId}")
    public String listComments(@PathParam("linkType") String linkType, @PathParam("linkId") Long linkId) {
        StringWriter writer = new StringWriter();
        List<SmComment> list = getCommentsFor(linkType, linkId);

        try{
            new SmCommentListR().render(writer,this,list);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return writer.toString();
    }

    /**
     * ************************************************************************
     * This section is for Data Processing
     * *************************************************************************
     */


    public List<SmComment> getCommentsFor(String linkType, Long id)
    {
        String sql = "select u.display_name comment_owner, c.*\n" +
                "from sm_comments c, sm_comment_links cl,  sm_users u \n" +
                "where c.id = cl.comment_id\n" +
                "      and  c.comment_owner_id = u.id \n" +
                "      and cl.link_to = ?1 \n" +
                "      and cl.link_id = ?2 \n";


        List<SmComment> list = entityManager.createNativeQuery(sql, "SmCommentV")
                .setParameter(1, linkType)
                .setParameter(2,id)
                .getResultList();
        return list;
    }

    public SmComment getCommentById(Long id) {
        return entityManager.find(SmComment.class,id);
    }


    public List<SmComment> getCommentList(){
        String sql = "select u.display_name comment_owner, cr.*  \n" +
                "from sm_comments c, sm_users u \n" +
                "where cr.cr_owner_id = u.id and cr_close_date is null";

        List<SmComment> list = entityManager.createNativeQuery(sql, "SmCommentV")
                .getResultList();
        return list;
    }

}
