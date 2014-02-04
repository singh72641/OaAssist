package com.punwire.oa.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.punwire.oa.core.OaController;
import com.punwire.oa.core.OaDefaults;
import com.punwire.oa.domain.SysList;
import com.punwire.oa.domain.SysListValue;
import com.punwire.oa.ui.OaLovR;

import javax.ejb.Stateless;
import javax.management.ObjectName;
import javax.persistence.*;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.io.File;
import java.io.StringWriter;
import java.util.List;

/**
 * Created by kanwal on 1/17/14.
 */
@Stateless
@Path("/lov")
public class SysListS {

    @PersistenceContext
    private EntityManager entityManager;


    @GET
    @Path("{lovName}")
    public String getLov(@PathParam("lovName") String lovName) {
        StringWriter writer = new StringWriter();

        try {
            new OaLovR().render(writer);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return writer.toString();
    }


    public List<SysListValue> getValues(String name) {
        String sql = "select l.name, lv.* \n" +
                "from sys_list_values lv, sys_lists l\n" +
                "where l.id = lv.list_id\n" +
                "      and\n" +
                "      l.name = ?1 ";

        Query qry = entityManager.createNativeQuery(sql, SysListValue.class);
        qry.setParameter(1, name);
        System.out.println("Getting LIst of valuesa 3");
        List<SysListValue> list = qry.getResultList();

        return list;
    }

    public SysList getList(String name) {
        TypedQuery<SysList> query =
                entityManager.createNamedQuery("SysList.findByName", SysList.class);
        query.setParameter("name", name);

        try {
            SysList list = query.getSingleResult();
            return list;
        } catch (NoResultException ex) {
            //List does not exists
        }
        return null;
    }

    public SysList addList(ObjectNode input) {
        SysList list = new SysList();
        list.setName(input.get("name").asText());
        list.setType(input.get("type").asText());
        entityManager.persist(list);
        return list;
    }

    public SysListValue addListValue(SysList list, ObjectNode input) {
        SysListValue value = new SysListValue();
        value.setCode(input.get("code").asText());
        value.setValue(input.get("value").asText());
        value.setValueSeq(input.get("value_seq").asLong());
        value.setList(list);
        entityManager.persist(value);
        return value;
    }

    public void loadFile(String name) {
        try {
            String dataPath = OaDefaults.getAppPath() + File.separator + "data" + File.separator + name + ".json";
            ObjectNode data = (ObjectNode) OaDefaults.mapper.readTree(new File(dataPath));
            //We got the files
            if (data.has("SysList")) {
                ArrayNode listList = (ArrayNode) data.get("SysList");
                for (int l = 0; l < listList.size(); l++) {
                    //Need to create SysLists
                    ObjectNode listNode = (ObjectNode) listList.get(l);

                    //Check If List Already Exists
                    SysList list = getList(listNode.get("name").asText());

                    if (list == null) {
                        list = addList((ObjectNode) listNode);
                        System.out.println("List Created " + list.getId());
                    }

                    //Check if Values were passed
                    if (listNode.has("SysListValue")) {
                        System.out.println("Values provides");
                        ArrayNode nodeList = (ArrayNode) listNode.get("SysListValue");
                        for (int i = 0; i < nodeList.size(); i++) {
                            ObjectNode value = (ObjectNode) nodeList.get(i);
                            System.out.println("Loading value " + value.toString());

                            if (!value.has("value_seq")) value.put("value_seq", i + 1);
                            addListValue(list, value);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
