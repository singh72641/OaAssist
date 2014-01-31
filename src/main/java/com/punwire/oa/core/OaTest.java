package com.punwire.oa.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.punwire.oa.services.OaViewS;


import java.io.File;


/**
 * Created by kanwal on 12/29/13.
 */
public class OaTest {
    static ObjectMapper mapper = new ObjectMapper();
//            Map<String, Object> iNode =  mapper.readValue(f, new TypeReference<Map<String, Object>>(){});

    public static void main(String[] args) {
        try{
            //String file = OaDefaults.getAppPath() + File.separator + "ui" + File.separator + "test" + File.separator + "testEntityAdd.json";
            String file = "h:/projects/OaAssist/app" + File.separator + "ui" + File.separator + "test" + File.separator + "testEntityAdd.json";
            JsonNode view =  mapper.readTree(new File(file));

            JsonNode node = view.get(1);

            System.out.println(view.toString());


//            String webappDirLocation = "src/main/webapp/";
//            Tomcat tomcat = new Tomcat();
//
//            //The port that we should run on can be set into an environment variable
//            //Look for that variable and default to 8080 if it isn't there.
//            String webPort = "8081";
//
//            Context ctx = tomcat.addContext("/oa", new File(".").getAbsolutePath());
//
//            tomcat.setPort(Integer.valueOf(webPort));
//
//            tomcat.addServlet(ctx, "test", new OaTestServlet());
//
//            ctx.addServletMapping("/*", "test");
//
//            tomcat.addWebapp("/", new File(webappDirLocation).getAbsolutePath());
//            //System.out.println("configuring app with basedir: " + new File("./" + webappDirLocation).getAbsolutePath());
//
//            tomcat.start();
//            tomcat.getServer().await();

//            SoyFileSet sfs = new SoyFileSet.Builder().add(new File(appPath + File.separator  + "templates" + File.separator + "OaResultList.soy")).build();
//            SoyTofu tofu = sfs.compileToTofu();
//
//            File f = new File( appPath + File.separator + "ui" + File.separator + "qComponentList" + ".json");
//
//            ObjectMapper mapper = new ObjectMapper();
//            Map<String, Object> iNode =  mapper.readValue(f, new TypeReference<Map<String, Object>>(){});
//
//            SoyTofu.Renderer r = tofu.newRenderer("com.punwire.oa.oaResultList");
//            r.setData(new SoyMapData("ui", iNode));
//            System.out.println(r.render());
//              ObjectMapper mapper = new ObjectMapper();
//              ObjectNode column = mapper.createObjectNode();
//              column.put("name","name");
//              column.put("prompt","Query Name");
//
//              Object v = Ognl.getValue(" '/query/' + get('name').asText() + '/details' ",column);
//              System.out.println("Value: " + v);
//              String link = "\"test\" + \"sdf\" ";
//              OaExpression exp = new OaExpression(link);
//              OgnlContext ctx = new OgnlContext();
//              Object value = exp.getValue(ctx, null);
//            System.out.println(value);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
