package com.punwire.oa.core;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;

import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.inject.Named;

/**
 * Created by kanwal on 1/17/14.
 */
@Singleton
public class OaConfig {

    protected String baseURI = "/";
    protected String appPath = "h:/projects/OaWeb/app";
    protected String resourceURI = baseURI + "resources/";
    protected CompositeConfiguration config;
    protected PropertiesConfiguration pConfig;
    protected boolean isLoaded = false;

    public OaConfig()
    {
        config = new CompositeConfiguration();
        config.addConfiguration(new SystemConfiguration());

        try {
            pConfig = new PropertiesConfiguration("oaassist.properties");
            pConfig.setAutoSave(true);
            config.addConfiguration(pConfig);

            System.out.println(config.getString("oa.appPath"));
            baseURI = config.getString("oa.baseURI","/");
            resourceURI = config.getString("oa.baseURI" + "/resources/");
            appPath = config.getString("oa.appPath");
            isLoaded = true;

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public String getAppPath() {
        return appPath;
    }

    public void setAppPath(String appPath) {
        this.appPath = appPath;
    }

    public String getBaseURI() {
        return baseURI;
    }

    public void setBaseURI(String baseURI) {
        if( baseURI == null || baseURI.length() == 0) baseURI = "/";
        this.baseURI = baseURI;
        System.out.println(">>>> Setting Base URI " + baseURI);
        pConfig.setProperty("oa.baseURI",baseURI);
        setResourceURI(baseURI + "resources/");
    }

    public String getResourceURI() {
        return resourceURI;
    }

    public void setResourceURI(String resourceURI) {
        this.resourceURI = resourceURI;
        System.out.println(">>>> Setting Resource URI " + resourceURI);
        pConfig.setProperty("oa.resourceURI",resourceURI);
    }

}