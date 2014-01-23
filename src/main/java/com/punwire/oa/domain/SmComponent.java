package com.punwire.oa.domain;

import oracle.sql.DATE;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.persistence.TemporalType.*;
import java.sql.Date;
import java.util.Calendar;

/**
 * Created by kanwal on 1/16/14.
 */
@Entity
@XmlRootElement
@Table(name = "sm_components")
public class SmComponent {

    @Id
    @SequenceGenerator(name="SEQ",sequenceName = "sm_component_s",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SEQ")
    @Column(name = "id", updatable = false, nullable = false)
    private Long id = null;

    @Column(name = "component_num")
    private String componentNum;

    @Column
    private String name;

    @Column(name = "component_type")
    private String componentType;

    @Column
    private String module;

    @Column
    private String track;

    @Column
    private String description;

    @Column(name="sm_stage")
    private String smStage;

    @Column(name="sm_stage_date")
    @Temporal(TemporalType.DATE)
    private Calendar startDate;

    public String getSmStage() {
        return smStage;
    }

    public void setSmStage(String smStage) {
        this.smStage = smStage;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComponentNum() {
        return componentNum;
    }

    public void setComponentNum(String componentNum) {
        this.componentNum = componentNum;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }
}
