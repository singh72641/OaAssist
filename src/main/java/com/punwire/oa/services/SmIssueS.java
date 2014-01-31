package com.punwire.oa.services;

import com.punwire.oa.core.OaController;
import com.punwire.oa.core.OaDefaults;
import com.punwire.oa.core.OaObject;
import com.punwire.oa.domain.SmChangeRequest;
import com.punwire.oa.domain.SmIssue;
import com.punwire.oa.sm.SmCompIssueListR;
import com.punwire.oa.sm.SmCrListR;
import com.punwire.oa.sm.SmIssueListR;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kanwal on 1/26/14.
 */
@Stateless
@Path("/issue")
public class SmIssueS extends OaController {

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

    //This will return List Change Requests
    @GET
    public String listChangeRequests() {
        OaObject param = new OaObject();
        StringWriter writer = new StringWriter();

        List<SmIssue> list = getIssueList();

        try {
            System.out.println("Redning issues " + list.size());
            new SmIssueListR().render(writer, this, list);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return writer.toString();
    }

    //This will return List Change Requests
    @GET
    @Path("forcomp/{compId}")
    public String listIssuesForComp(@PathParam("compId") Long compId) {
        StringWriter writer = new StringWriter();
        List<SmIssue> list = getCompIssueList(compId);
        try {
            new SmCompIssueListR().render(writer, this, list);
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

    /**
     * ************************************************************************
     * This section is for Data Processing
     * *************************************************************************
     */
    public List<SmIssue> getIssueList() {
        String sql = "select u.display_name i_owner, co.name i_comp, i.*  \n" +
                "from sm_issues i, sm_users u, sm_components co \n" +
                "where i.i_owner_id = u.id and i.comp_id = co.id(+)";

        List<SmIssue> list = entityManager.createNativeQuery(sql, "SmIssueV")
                .getResultList();
        return list;
    }

    public List<SmIssue> getCompIssueList(Long id) {
        System.out.println("Getting Issues for component " + id);
        String sql = "select u.display_name i_owner, co.name i_comp, i.*  \n" +
                "from sm_issues i, sm_users u, sm_components co \n" +
                "where i.i_owner_id = u.id and i.comp_id = co.id(+) and co.id = ?1";

        List<SmIssue> list = entityManager.createNativeQuery(sql, "SmIssueV")
                .setParameter(1, id)
                .getResultList();
        return list == null ? new ArrayList<SmIssue>() : list;
    }

    public SmIssue getIssueById(Long id) {
        String sql = "select u.display_name i_owner, co.name i_comp, i.*  \n" +
                "from sm_issues i, sm_users u, sm_components co \n" +
                "where i.i_owner_id = u.id and i.comp_id = co.id(+) and i.id = ?1";

        SmIssue issue = (SmIssue) entityManager.createNativeQuery(sql, "SmIssueV")
                .setParameter(1, id)
                .getSingleResult();

        return issue;
    }

}
