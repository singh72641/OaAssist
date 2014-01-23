package com.punwire.oa.core;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.punwire.oa.domain.OaMenu;

/**
 * Created by kanwal on 1/16/14.
 */
public class OaSession {


    protected String userName;

    protected String operatingUnit;

    protected OaMenu menu;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOperatingUnit() {
        return operatingUnit;
    }

    public void setOperatingUnit(String operatingUnit) {
        this.operatingUnit = operatingUnit;
    }

    public OaMenu getMenu() {
        return menu;
    }

    public void setMenu(OaMenu menu) {
        this.menu = menu;
    }
}
