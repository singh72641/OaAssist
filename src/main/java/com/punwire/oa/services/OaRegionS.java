package com.punwire.oa.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.punwire.oa.core.OaDefaults;
import com.punwire.oa.core.OaService;
import com.punwire.oa.domain.OaMenu;
import com.punwire.oa.domain.OaMenuItem;
import com.punwire.oa.domain.OaRegion;
import com.punwire.oa.domain.SysList;
import org.apache.commons.lang.WordUtils;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.File;
import java.sql.*;

/**
 * Created by kanwal on 1/18/14.
 */
@Stateless
public class OaRegionS extends OaService {

    protected ObjectMapper mapper = new ObjectMapper();

    @PersistenceContext
    private EntityManager entityManager;

    public OaRegionS() {
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.enable(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS);

    }

    public void add(ObjectNode node) {
        OaRegion region = new OaRegion();
        region.setName(node.get("name").asText());
        region.setDescription(node.get("description").asText());
        region.setType(node.get("type").asText());
        region.setSource(node.get("source").asText());
//        entityManager.persist(region);
//        try {
//            mapper.writeValue(new File(path + region.getName() + ".json"), node);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
        String name = node.get("name").asText();
        String sourceType = node.get("type").asText();
        String source = node.get("source").asText();

        System.out.println("Souce TYpe " + sourceType);
        System.out.println("Souce " + source);
        //Create The filles for the region
        String module = node.get("module").asText();

        String listFile = OaDefaults.getAppPath() + File.separator + "ui" + File.separator + module.toLowerCase() + File.separator + region.getName() + "List.json";
        String addFile = OaDefaults.getAppPath() + File.separator + "ui" + File.separator + module.toLowerCase() + File.separator + region.getName() + "Add.json";
        String detailFile = OaDefaults.getAppPath() + File.separator + "ui" + File.separator + module.toLowerCase() + File.separator + region.getName() + "Detail.json";


        try {
            //Create List View
            ObjectNode view = mapper.createObjectNode();
            view.put("title", name.substring(module.length()));
            view.put("type", "OaSearchPageR");
            view.put("action", name);
            view.put("addaction", name + "/add");
            view.put("regionSize", "lg-8");
            view.put("showPagination", true);

            //Build Query
            ObjectNode query = mapper.createObjectNode();
            ArrayNode sql = mapper.createArrayNode();
            sql.add("select rownum row_num, e.*");
            sql.add("from " + source + " e ");
            sql.add(" &where& ");
            query.put("sql", sql);
            query.put("dynamic", true);
            view.put("query", query);

            ArrayNode cols = generate(source);
            ObjectNode secondCol = (ObjectNode) cols.get(1);
            String secondName = secondCol.get("name").asText();

            view.put("columns", cols);
            mapper.writeValue(new File(listFile), view);


            //Create Add View
            view = mapper.createObjectNode();
            view.put("title", "'Add " + name.substring(module.length()) + "'");
            view.put("type", "OaFormR");
            view.put("action", name + "/add");
            view.put("regionSize", "lg-6");
            view.put("method", "POST");
            view.put("columns", cols);
            mapper.writeValue(new File(addFile), view);

            //Create Details View
            view = mapper.createObjectNode();
            view.put("title", "'Add " + name.substring(module.length()) + "'");
            view.put("type", "OaFormViewR");
            view.put("action", name + "/add");
            view.put("regionSize", "lg-6");
            view.put("method", "POST");

            ArrayNode inputs = mapper.createArrayNode();
            ObjectNode input = mapper.createObjectNode();
            input.put("name", secondName);
            input.put("type", "String");
            input.put("required", true);
            inputs.add(input);


            view.put("inputs", inputs);
            //Build Query
            query = mapper.createObjectNode();
            sql = mapper.createArrayNode();
            sql.add("select e.*");
            sql.add("from " + source + " e ");
            sql.add("where " + secondName + " = ?");
            query.put("sql", sql);
            view.put("query", query);

            view.put("columns", cols);


            mapper.writeValue(new File(detailFile), view);

            //Add to the main Menu
            System.out.println("Adding to Menu");
            try {
                TypedQuery<OaMenu> q =
                        entityManager.createNamedQuery("OaMenu.findByName", OaMenu.class);
                q.setParameter("name", "OaHomePageLeft");
                OaMenu menu = q.getSingleResult();

                System.out.println("Found Menu " + menu.getName());
                OaMenuItem mItem = new OaMenuItem();
                mItem.setName(name);
                mItem.setPrompt(name.substring(module.length()));
                mItem.setTargetType("Link");
                mItem.setMenu(menu);
                mItem.setTarget("front/"+module+"/" + region.getName() + "List");
                entityManager.persist(mItem);
                System.out.println("Menu Items persisted");
            } catch (NoResultException ex) {
                //List does not exists
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public ObjectNode getRegion(String name) {
        try {
            String fName = OaDefaults.getAppPath() + File.separator + "regions" + File.separator + name + ".json";
            return (ObjectNode) mapper.readTree(new File(fName));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public ArrayNode generate(String tableName) {
        //Get Columns from Database
        System.out.println("Getting Columns from database " + tableName);
        ArrayNode cols = mapper.createArrayNode();
        try {
            Connection cnn = entityManager.unwrap(java.sql.Connection.class);
            DatabaseMetaData dmd = cnn.getMetaData();
            ResultSet result = dmd.getColumns(null, null, tableName.toUpperCase(), null);

            while (result.next()) {
                String columnName = result.getString(4);
                int columnType = result.getInt(5);
                String columnTypeS = result.getString(6);
                System.out.println("Columns " + columnName + " " + columnTypeS);
                ObjectNode col = mapper.createObjectNode();
                col.put("name", columnName.toLowerCase());
                col.put("bind", columnName.toLowerCase());
                col.put("prompt", WordUtils.capitalizeFully(columnName.toLowerCase()));
                col.put("size", 2);
                if (columnTypeS.equals("NUMBER"))
                    col.put("type", "Number");
                else if (columnTypeS.equals("DATE"))
                    col.put("type", "Date");
                else
                    col.put("type", "String");
                col.put("displaytype", "text");
                if (!columnName.endsWith("id"))
                {
                    col.put("search", true);
                    col.put("visible", true);
                }
                else
                {
                    col.put("search", false);
                    col.put("visible", false);
                }
                cols.add(col);
            }

            return cols;
            //mapper.writeValue(new File(path + tableName + ".json"), cols);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return cols;
    }
}
