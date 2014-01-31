package com.punwire.oa.domain;


import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.SqlResultSetMapping;

/**
 * Created by kanwal on 1/28/14.
 */

public class SmObjectSource {

    protected Integer lineNum;

    protected String lineText;

    public SmObjectSource(Integer line, String text) {
        //System.out.println("Constructing " + text);
        this.lineNum = line;
        this.lineText = text;
    }

    public Integer getLineNum() {
        return lineNum;
    }

    public String getLineText() {
        return lineText;
    }

    public void setLineNum(Integer lineNum) {
        this.lineNum = lineNum;
    }

    public void setLineText(String lineText) {
        this.lineText = lineText;
    }
}
