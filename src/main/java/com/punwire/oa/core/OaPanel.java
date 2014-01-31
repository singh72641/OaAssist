package com.punwire.oa.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.punwire.oa.ui.OaInputFieldR;
import com.punwire.oa.ui.OaRowR;
import com.punwire.oa.ui.OaTabPageR;
import com.punwire.oa.ui.OaTabR;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;


/**
 * Created by kanwal on 1/24/14.
 */
public class OaPanel {
    public OaObject panel;

    static OaInputFieldR inputFieldR = new OaInputFieldR();
    static OaRowR rowR = new OaRowR();

    public OaPanel(OaObject panel) {
        this.panel = panel;
    }

    public OaPanel(ObjectNode p) {
        this.panel = new OaObject(p);
    }

    public String render(OaView view, JsonNode row) {
        StringWriter writer = new StringWriter();
        render(writer, view, row);
        return writer.toString();
    }


    public void renderView(Writer writer, String viewName, OaObject params) {


    }


    public void render(Writer writer, OaView view, JsonNode row) {
        try {

            System.out.println("Building Panel " + view.getName()+ " >>>>> " + row.toString());
            Iterator<Map.Entry<String, JsonNode>> fields = panel.node.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                if (field.getKey().startsWith("OaInputFieldR")) {
                    System.out.println("Render >>>>>>  OaInputField");
                    Map<String, OaObject> cols = view.getColumnMap();
                    JsonNode f = field.getValue();
                    String colName = f.get("name").asText();
                    new OaInputFieldR()
                            .setReadOnly(view.isReadOnly)
                            .render(writer, new OaViewColumn(cols.get(colName), view), row);
                } else if (field.getKey().startsWith("OaRow")) {
                    System.out.println("Render >>>>>>  OaRow");
                    new OaRowR().render(writer, view, row, new OaPanel((ObjectNode) field.getValue()));
                } else if (field.getKey().startsWith("OaTabPage")) {
                    System.out.println("Render >>>>>>  OaTabPage");
                    new OaTabPageR().render(writer, view, row, new OaPanel((ObjectNode) field.getValue()));
                } else if (field.getKey().startsWith("OaTab")) {
                    System.out.println("Render >>>>>>  OaTab");
                    new OaTabR().render(writer, view, row, new OaPanel((ObjectNode) field.getValue()));
                } else if (field.getKey().startsWith("OaView")) {
                    System.out.println("Render >>>>>>  OaView");
                    ObjectNode nView = (ObjectNode)field.getValue();
                    String viewName = nView.get("name").asText();
                    ObjectNode inputMap = (ObjectNode)nView.get("inputMap");
                    OaObject params = mapParameters(new OaObject(inputMap), new OaObject((ObjectNode)row));
                    JsonNode row1 = OaDefaults.viewS.build(writer, viewName, params , false);
                    //Map Output
                    String v = viewName;
                    if( viewName.indexOf('/') > 0 ) v = viewName.substring(viewName.indexOf('/')+1);
                    ((ObjectNode) row).put(v, row1);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public OaObject mapParameters(OaObject map, OaObject params)
    {
        System.out.println("Parameter Map - Map: " + map.toString());
        System.out.println("Parameter Map - params: " + params.toString());
        Iterator<Map.Entry<String, JsonNode>> inputs = map.node.fields();
        OaObject outParams = new OaObject();
        while (inputs.hasNext()) {
            Map.Entry<String, JsonNode> input = inputs.next();
            String fieldName = input.getKey();
            String fieldMap = input.getValue().asText();

            if( fieldMap.indexOf('.') > 0 )
            {
                //Nexted Map
                String objectName = fieldMap.substring(0,fieldMap.indexOf('.'));
                String objectField = fieldMap.substring(fieldMap.indexOf('.')+1);
                System.out.println("ObjectName " + objectName);
                System.out.println("ObjectName " + objectField);
                ObjectNode obj = (ObjectNode) params.node.get(objectName);
                outParams.put(fieldName, obj.get(objectField).asText());
            }
            else
            if( params.has(fieldMap) ) outParams.put(fieldName, params.s(fieldMap));
        }
        System.out.println("Parameter Map " + outParams.toString());
        return outParams;
    }

}
