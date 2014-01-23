package com.punwire.oa.domain;


import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;
import java.util.List;


/**
 * Created by kanwal on 1/17/14.
 */
@Entity
@XmlRootElement
@Table(name = "sys_lists")
@NamedQueries({
        @NamedQuery(name="SysList.findAll",
                query="SELECT c FROM SysList c"),
        @NamedQuery(name="SysList.findByName",
                query="SELECT c FROM SysList c WHERE c.name = :name"),
})
public class SysList {

    @Id
    @SequenceGenerator(name="SysListSeq",sequenceName = "sys_list_s",allocationSize=1)
    @GeneratedValue(generator = "SysListSeq")
    @Column(name = "id", updatable = false, nullable = false)
    private Long id = null;

    @Column
    private String name;

    @Column
    private String type;

    @Column
    private String sql;

    @OneToMany
    @JoinColumn(name = "LIST_ID")
    private List<SysListValue> values;

    public List<SysListValue> getValues() {
        return values;
    }

    public void setValues(List<SysListValue> vals) {
        values = vals;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
