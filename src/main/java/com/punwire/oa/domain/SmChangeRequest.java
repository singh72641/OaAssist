package com.punwire.oa.domain;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Calendar;

/**
 * Created by kanwal on 1/26/14.
 */
@Entity
@XmlRootElement
@Table(name = "sm_change_requests")

@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "SmChangeRequestV",
        entities = {
        @EntityResult(
            entityClass = SmChangeRequest.class,
            fields = {
                @FieldResult(name="crOwner", column="cr_owner"),
                @FieldResult(name="crComponentName", column="cr_comp")
            }
        )}
    )
})

public class SmChangeRequest {

    @Id
    @SequenceGenerator(name="SmCrSeq",sequenceName = "sm_change_request_s",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SmCrSeq")
    @Column(name = "id", updatable = false, nullable = false)
    private Long id = null;

    @Column(name = "cr_num")
    private String crNumber;

    @Column(name = "cr_name")
    private String crName;

    @Column(name = "cr_description")
    private String crDescription;

    @Column(name = "cr_type")
    private String crType;

    @Column(name = "cr_status")
    private String crStatus;

    @Column(name="cr_open_date")
    @Temporal(TemporalType.DATE)
    private Calendar crOpenDate;

    @Column(name="cr_close_date")
    @Temporal(TemporalType.DATE)
    private Calendar crCloseDate;

    @Column(name="cr_target_date")
    @Temporal(TemporalType.DATE)
    private Calendar crTargetDate;

    @Column(name="cr_owner_id")
    private Long crOwnerId;

    @Column(name="comp_id")
    private Long crCompId;

    @Transient
    private String crOwner;

    @Transient
    private String crComponentName;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCrNumber() {
        return crNumber;
    }

    public String getCrStatus() {
        return crStatus;
    }

    public void setCrStatus(String crStatus) {
        this.crStatus = crStatus;
    }

    public void setCrNumber(String crNumber) {
        this.crNumber = crNumber;
    }

    public String getCrName() {
        return crName;
    }

    public String getCrComponentName() {
        return crComponentName;
    }

    public void setCrComponentName(String crComponentName) {
        this.crComponentName = crComponentName;
    }

    public void setCrName(String crName) {
        this.crName = crName;
    }

    public String getCrDescription() {
        return crDescription;
    }

    public void setCrDescription(String crDescription) {
        this.crDescription = crDescription;
    }

    public String getCrType() {
        return crType;
    }

    public void setCrType(String crType) {
        this.crType = crType;
    }

    public Calendar getCrOpenDate() {
        return crOpenDate;
    }

    public void setCrOpenDate(Calendar crOpenDate) {
        this.crOpenDate = crOpenDate;
    }

    public Calendar getCrCloseDate() {
        return crCloseDate;
    }

    public void setCrCloseDate(Calendar crCloseDate) {
        this.crCloseDate = crCloseDate;
    }

    public Calendar getCrTargetDate() {
        return crTargetDate;
    }

    public void setCrTargetDate(Calendar crTargetDate) {
        this.crTargetDate = crTargetDate;
    }

    public Long getCrOwnerId() {
        return crOwnerId;
    }

    public void setCrOwnerId(Long crOwnerId) {
        this.crOwnerId = crOwnerId;
    }

    public Long getCrCompId() {
        return crCompId;
    }

    public void setCrCompId(Long crCompId) {
        this.crCompId = crCompId;
    }

    public String getCrOwner() {
        return crOwner;
    }

    public void setCrOwner(String crOwner) {
        this.crOwner = crOwner;
    }
}
