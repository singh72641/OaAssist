package com.punwire.oa.core;


import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;

/**
 * Created by kanwal on 1/17/14.
 */
public class OaDefaults {
    protected static OaConfig config;

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
