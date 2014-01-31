package com.punwire.oa.core;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.punwire.oa.services.OaViewS;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;

/**
 * Created by kanwal on 1/17/14.
 */
public class OaDefaults {
    protected static OaConfig config;

    public static ObjectMapper mapper = new ObjectMapper();

    public static OaViewS viewS;

    public static void setConfig(OaConfig cfg)
    {
        config = cfg;
    }

    public static String getAppPath() {
        return config.getAppPath();
    }

    public static String base() {
        return config.getBaseURI();
    }

    public static String rBase() {
        return config.getResourceURI();
    }
}
