package com.punwire.oa.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.punwire.oa.core.*;
import com.punwire.oa.domain.OaMenu;
import com.punwire.oa.domain.SysList;
import com.punwire.oa.domain.SysListValue;
import com.punwire.oa.ui.*;
import org.jamon.AbstractTemplateProxy;

import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

/**
 * Created by kanwal on 1/16/14.
 */
@Stateless
@Singleton
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

    public ObjectNode initParam(String name, Long value) {
        ObjectNode param = mapper.createObjectNode();
        param.put(name, value);
        return param;
    }

    public ObjectNode getViewRow(OaView view, OaObject params) {
        try {
            OaObject query = view.o("query");
            ObjectNode parameters = getParameters(view, params, false);
            ArrayNode columns = (ArrayNode) view.get("columns");
            ObjectNode row = queryService.runSingleQuery(query.node, columns, parameters);
            return row;
        } catch (Exception ex) {
            ex.printStackTrace();
            //System.out.println("Error while getting parameters");
        }
        return null;
    }

    public ObjectNode getViewRow(String name, OaObject params) {
        OaView view = new OaView(name, lovService);
        return getViewRow(view, params);
    }

    public ArrayNode getViewRows(String name, OaObject params) {
        return getViewRows(new OaView(name, lovService), params);
    }

    public ArrayNode getViewRows(OaView view, OaObject params) {
        try {
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

    public SysListS getListService() {
        return lovService;
    }


    public JsonNode build(Writer writer, String name, OaObject params, Boolean readOnly) {
        System.out.println("Building View " + name);
        JsonNode data ;
        try {

            //Get View Definition
            OaView view = new OaView(name, lovService);
            ObjectNode parameters = getParameters(view, params, false);

            //Get Query Data
            if (view.has("query")) {
                if (view.isList()) {
                    data = getViewRows(view, params);
                } else {
                    data = getViewRow(view, params);
                }
            }
            else
            {
                data = params.node;
            }
            view.setReadOnly(readOnly);

            //Render View
            String tempType = view.getTemplate();
            if (tempType.equals("OaFormR"))
                new OaFormR().render(writer, view, data);
            else if (tempType.equals("OaGridR"))
                new OaGridR().render(writer, view, data);
            else if (tempType.equals("OaSearchPageR"))
                new OaSearchPageR().render(writer, view, params.node, data);
            else if (tempType.equals("OaCompositeR"))
                new OaPanelR().render(writer, view, parameters, view.getFormPanel());
            //Map Outputs if any
            return data;
        } catch (Exception ex) {
            ex.printStackTrace();
            //System.out.println("Error while getting parameters");
        }

        return null;
    }

    public void buildTemplate(String template) {

    }

    public OaViewResult buildView(String name, OaObject params) {
        OaView oaView = new OaView(name, lovService);
        System.out.println("Building view " + oaView.toString());
        System.out.println("Building view params " + params.toString());
        StringWriter writer = new StringWriter();
        try {
            String vType = oaView.s("type");

            boolean isDynamic = false;

            OaObject query = oaView.o("query");

            if (query != null && query.has("dynamic")) isDynamic = query.b("dynamic");

            //Get parameters
            ObjectNode parameters = getParameters(oaView, params, isDynamic);
            System.out.println("Parameters>>>>>");
            System.out.println(parameters.toString());
            //Get Query
            ArrayNode columns = (ArrayNode) oaView.get("columns");
            ObjectNode row = mapper.createObjectNode();
            if (query != null) {
                if (vType.equals("OaFormR") || vType.equals("OaFormViewR")) {
                    row = queryService.runSingleQuery(query.node, columns, parameters);
                }
            }
            if (vType.equals("OaFormR")) {
                System.out.println(row.toString());
                new OaFormR().render(writer, oaView, row);
                return new OaViewResult(writer.toString(), row);
            } else if (vType.equals("OaSearchPageR")) {
                ArrayNode rows = queryService.runQuery(query.node, columns, parameters);
                System.out.println("Search Page Rows");
                System.out.println(rows.toString());
                ObjectNode searchDefault = mapper.createObjectNode();
                //new OaSearchPageR().render(writer, oaView, searchDefault, rows);
            } else if (vType.equals("OaFormViewR")) {
                System.out.println(row.toString());
                ObjectNode searchDefault = mapper.createObjectNode();
                new OaFormViewR().render(writer, oaView, row);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            //System.out.println("Error while getting parameters");
        }

        return new OaViewResult(writer.toString(), null);
    }

    public ObjectNode getParameters(OaView view, OaObject params, boolean isDynamic) throws OaMissingParameter {
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
                    String value = params.s(iName);
                    p.put(iName, value);
                } else if (params.has("_param" + i)) {
                    //Getting parameter by position
                    p.put(iName, params.s("_param" + i));
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
                    p.put(key, params.s(key));
                }
            }
        }
        return p;
    }

}
