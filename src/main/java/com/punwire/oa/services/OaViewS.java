package com.punwire.oa.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.punwire.oa.core.OaDefaults;
import com.punwire.oa.core.OaMissingParameter;
import com.punwire.oa.core.OaView;
import com.punwire.oa.core.OaViewResult;
import com.punwire.oa.domain.OaMenu;
import com.punwire.oa.domain.SysListValue;
import com.punwire.oa.ui.OaFormR;
import com.punwire.oa.ui.OaFormTableR;
import com.punwire.oa.ui.OaFormViewR;
import com.punwire.oa.ui.OaSearchPageR;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.File;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;

/**
 * Created by kanwal on 1/16/14.
 */
@Stateless
public class OaViewS {
    protected ObjectMapper mapper = new ObjectMapper();

    @Inject
    protected OaQueryS queryService;


    @Inject
    protected SysListS lovService;


    public ObjectNode getView(String name) {
        try {
            String fName = OaDefaults.getAppPath() + File.separator + "ui" + File.separator + name + ".json";
            return (ObjectNode) mapper.readTree(new File(fName));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public List<SysListValue> getList(String name) {
        if (lovService == null) System.out.println("LOV SERVICE IS NULL");
        return lovService.getValues(name);
    }

    public ObjectNode initParam(String name, Long value)
    {
        ObjectNode param = mapper.createObjectNode();
        param.put(name,value);
        return param;
    }

    public ObjectNode getViewRow(String name, ObjectNode params) {
        try {
            ObjectNode view = getView(name);
            ObjectNode query = (ObjectNode) view.get("query");
            ObjectNode parameters = getParameters(view, params, false);
            ArrayNode columns = (ArrayNode) view.get("columns");
            ObjectNode row = queryService.runSingleQuery(query, columns, parameters);
            return row;
        } catch (Exception ex) {
            ex.printStackTrace();
            //System.out.println("Error while getting parameters");
        }
        return null;
    }

    public ArrayNode getViewRows(String name, ObjectNode params) {
        try {
            ObjectNode view = getView(name);
            ObjectNode query = (ObjectNode) view.get("query");
            ObjectNode parameters = getParameters(view, params, false);
            ArrayNode columns = (ArrayNode) view.get("columns");
            System.out.println("params; " + params.toString());
            System.out.println("parameters; " + parameters.toString());
            System.out.println("columns: " + columns.toString());
            ArrayNode rows = queryService.runQuery(query, columns, parameters);
            return rows;
        } catch (Exception ex) {
            ex.printStackTrace();
            //System.out.println("Error while getting parameters");
        }
        return null;
    }

    public ObjectNode getViewRow(String name) {
        return getViewRow(name, mapper.createObjectNode());
    }



    public OaViewResult buildView(String name, ObjectNode params) {
        ObjectNode view = getView(name);
        System.out.println("Building view " + view.toString());
        System.out.println("Building view params " + params.toString());
        StringWriter writer = new StringWriter();
        try {
            String vType = view.get("type").asText();
            if (vType.equals("OaComposite")) {
                //Its a Composite View, build in sequence
                System.out.println("Starting to build Composite..");
                ArrayNode regions = (ArrayNode) view.get("regions");
                JsonNode rData = null;
                for (int r = 0; r < regions.size(); r++) {
                    ObjectNode region = (ObjectNode) regions.get(r);
                    OaViewResult rOutput = buildView(region.get("name").asText(), params);
                    writer.append(rOutput.content);
                    if (rOutput.data != null) rData = rOutput.data;
                }
                return new OaViewResult(writer.toString(), rData);
            }

            boolean isDynamic = false;

            ObjectNode query = (ObjectNode) view.get("query");

            if (query != null && query.has("dynamic")) isDynamic = query.get("dynamic").asBoolean();

            //Get parameters
            ObjectNode parameters = getParameters(view, params, isDynamic);
            System.out.println("Parameters>>>>>");
            System.out.println(parameters.toString());
            //Get Query
            ArrayNode columns = (ArrayNode) view.get("columns");
            ObjectNode row = mapper.createObjectNode();
            if (query != null) {
                if (vType.equals("OaFormR") || vType.equals("OaFormViewR")) {
                    row = queryService.runSingleQuery(query, columns, parameters);
                }
            }
            if (vType.equals("OaFormR")) {
                System.out.println(row.toString());
                new OaFormR().render(writer, new OaView(name,lovService), row);
                return new OaViewResult(writer.toString(), row);
            } else if (vType.equals("OaSearchPageR")) {
                ArrayNode rows = queryService.runQuery(query, columns, parameters);
                System.out.println("Search Page Rows");
                System.out.println(rows.toString());
                ObjectNode searchDefault = mapper.createObjectNode();
                new OaSearchPageR().render(writer, new OaView(name,lovService), searchDefault, rows);
            } else if (vType.equals("OaFormViewR")) {
                System.out.println(row.toString());
                ObjectNode searchDefault = mapper.createObjectNode();
                new OaFormViewR().render(writer, view, row,this);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            //System.out.println("Error while getting parameters");
        }

        return new OaViewResult(writer.toString(), null);
    }

    public ObjectNode getParameters(ObjectNode view, ObjectNode params, boolean isDynamic) throws OaMissingParameter {
        //Check if inputs are provided
        System.out.println("getParameters1");
        System.out.println(params.toString());
        ArrayNode inputs = (ArrayNode) view.get("inputs");
        ObjectNode p = mapper.createObjectNode();
        if (inputs != null) {
            System.out.println("inputs>>>>");
            System.out.println(inputs.toString());
            for (int i = 0; i < inputs.size(); i++) {
                ObjectNode input = (ObjectNode) inputs.get(i);
                String iName = input.get("name").asText();
                System.out.println("Searching parameters " + iName + " or " + ("_param" + i));
                boolean isRequired = input.get("required").asBoolean();
                if (params.has(iName)) {
                    //Parameter specified by name
                    String value = params.get(iName).asText();
                    p.put(iName, value);
                } else if (params.has("_param" + i)) {
                    //Getting parameter by position
                    p.put(iName, params.get("_param" + i).asText());
                } else if (isRequired) throw new OaMissingParameter("Missing " + iName);
            }
        }

        //Add direct parameters
        if (isDynamic) {
            Iterator<String> fields = params.fieldNames();

            while (fields.hasNext()) {
                String key = fields.next();
                //See if we have parameters not specified in the inputs
                //for dynamic queries
                if (!key.startsWith("_") && (!p.has(key))) {
                    p.put(key, params.get(key).asText());
                }
            }
        }
        return p;
    }

}
