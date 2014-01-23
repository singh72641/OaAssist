package com.punwire.oa.domain;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by kanwal on 1/19/14.
 */
@Entity
@XmlRootElement
@Table(name = "oa_menu_items")
public class OaMenuItem {
    @Id
    @SequenceGenerator(name="OaMenuItemSeq",sequenceName = "oa_menu_item_s",allocationSize=1)
    @GeneratedValue(generator = "OaMenuItemSeq")
    @Column(name = "id", updatable = false, nullable = false)
    private Long id = null;

    @Column
    private String name;

    @Column
    private String prompt;

    @Column(name = "target_type")
    private String targetType;

    @Column
    private String target;

    @ManyToOne
    @JoinColumn(name = "MENU_ID", referencedColumnName = "ID", nullable = false)
    private OaMenu menu;

    public OaMenu getMenu() {
        return menu;
    }

    public void setMenu(OaMenu menu) {
        this.menu = menu;
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

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
