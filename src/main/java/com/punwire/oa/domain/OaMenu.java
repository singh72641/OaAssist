package com.punwire.oa.domain;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by kanwal on 1/19/14.
 */
@Entity
@XmlRootElement
@Table(name = "oa_menus")
@NamedQueries({
        @NamedQuery(name="OaMenu.findAll",
                query="SELECT c FROM OaMenu c"),
        @NamedQuery(name="OaMenu.findByName",
                query="SELECT c FROM OaMenu c WHERE c.name = :name"),
})
public class OaMenu {
    @Id
    @SequenceGenerator(name="OaMenuSeq",sequenceName = "oa_menu_s",allocationSize=1)
    @GeneratedValue(generator = "OaMenuSeq")
    @Column(name = "id", updatable = false, nullable = false)
    private Long id = null;

    @Column
    private String name;

    @Column
    private String description;

    @OneToMany
    @JoinColumn(name = "MENU_ID")
    private List<OaMenuItem> items;

    public List<OaMenuItem> getItems() {
        return items;
    }

    public void setItems(List<OaMenuItem> items) {
        this.items = items;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
