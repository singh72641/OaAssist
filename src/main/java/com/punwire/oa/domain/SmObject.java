package com.punwire.oa.domain;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by kanwal on 1/20/14.
 */
@Entity
@XmlRootElement
@Table(name = "sm_objects")
public class SmObject {

    @Id
    @SequenceGenerator(name="SmObjectSeq",sequenceName = "sm_object_s",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SmObjectSeq")
    @Column(name = "id", updatable = false, nullable = false)
    private Long id = null;

    @Column
    private String name;

    @Column(name = "object_type")
    private String objectType;

    @Column(name = "object_source")
    private String objectSource;

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

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getObjectSource() {
        return objectSource;
    }

    public void setObjectSource(String objectSource) {
        this.objectSource = objectSource;
    }
}
