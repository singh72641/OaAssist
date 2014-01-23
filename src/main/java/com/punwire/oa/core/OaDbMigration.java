package com.punwire.oa.core;

import com.googlecode.flyway.core.Flyway;

/**
 * Created by kanwal on 1/21/14.
 */
public class OaDbMigration {
    public static void main(String[] args)
    {
        Flyway flyway = new Flyway();
        String url = "jdbc:oracle:thin:@//r12dbdev1.misqa.edc.vrsn.com:7001/dev1";
        String user = "apps";
        String password = "apps";
        flyway.setDataSource(url, user, password);
        flyway.setSchemas("APPS");
        flyway.init();
        flyway.migrate();
    }
}
