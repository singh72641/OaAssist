package com.punwire.oa.domain;


import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by kanwal on 1/17/14.
 */

@Entity
@XmlRootElement
@Table(name = "sys_list_values")
public class SysListValue {

    @Id
    @SequenceGenerator(name="SysListValSeq",sequenceName = "sys_list_value_s",allocationSize=1)
    @GeneratedValue(generator = "SysListValSeq")
    @Column(name = "id", updatable = false, nullable = false)
    private Long id = null;

    @Column
    private String code;

    @Column
    private String value;

    @Column(name = "value_seq")
    private Long valueSeq;

    @ManyToOne
    @JoinColumn(name = "LIST_ID", referencedColumnName = "ID", nullable = false)
    private SysList list;

    public SysList getList() {
        return list;
    }

    public void setList(SysList list) {
        this.list = list;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getValueSeq() {
        return valueSeq;
    }

    public void setValueSeq(Long valueSeq) {
        this.valueSeq = valueSeq;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
