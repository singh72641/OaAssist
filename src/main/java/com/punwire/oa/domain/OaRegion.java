package com.punwire.oa.domain;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by kanwal on 1/18/14.
 */
@Entity
@XmlRootElement
@Table(name = "oa_regions")
public class OaRegion {
    @Id
    @SequenceGenerator(name="OaRegionS",sequenceName = "oa_region_s",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "OaRegionS")
    @Column(name = "id", updatable = false, nullable = false)
    private Long id = null;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private String type;

    @Column
    private String source;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
