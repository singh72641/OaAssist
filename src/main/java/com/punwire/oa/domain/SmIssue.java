package com.punwire.oa.domain;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Calendar;

/**
 * Created by kanwal on 1/27/14.
 */
@Entity
@XmlRootElement
@Table(name = "sm_issues")

@SqlResultSetMappings({
        @SqlResultSetMapping(
                name = "SmIssueV",
                entities = {
                        @EntityResult(
                                entityClass = SmIssue.class,
                                fields = {
                                        @FieldResult(name="issueOwner", column="i_owner"),
                                        @FieldResult(name="issueComponentName", column="i_comp")
                                }
                        )}
        )
})

public class SmIssue {
    @Id
    @SequenceGenerator(name="SmIssueSeq",sequenceName = "sm_issue_s",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SmIssueSeq")
    @Column(name = "id", updatable = false, nullable = false)
    private Long id = null;

    @Column(name = "i_name")
    private String issueName;

    @Column(name = "i_description")
    private String issueDescription;

    @Column(name = "i_type")
    private String issueType;

    @Column(name = "i_status")
    private String issueStatus;

    @Column(name = "i_priority")
    private String issuePriority;

    @Column(name="i_open_date")
    @Temporal(TemporalType.DATE)
    private Calendar issueOpenDate;

    @Column(name="i_close_date")
    @Temporal(TemporalType.DATE)
    private Calendar issueCloseDate;

    @Column(name="i_target_date")
    @Temporal(TemporalType.DATE)
    private Calendar issueTargetDate;

    @Column(name="i_owner_id")
    private Long issueOwnerId;

    @Transient
    private String issueOwner;

    @Transient
    private String issueComponentName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIssueName() {
        return issueName;
    }

    public void setIssueName(String issueName) {
        this.issueName = issueName;
    }

    public String getIssueDescription() {
        return issueDescription;
    }

    public String getIssueStatus() {
        return issueStatus;
    }

    public void setIssueStatus(String issueStatus) {
        this.issueStatus = issueStatus;
    }

    public void setIssueDescription(String issueDescription) {
        this.issueDescription = issueDescription;
    }

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public String getIssuePriority() {
        return issuePriority;
    }

    public void setIssuePriority(String issuePriority) {
        this.issuePriority = issuePriority;
    }

    public Calendar getIssueOpenDate() {
        return issueOpenDate;
    }

    public void setIssueOpenDate(Calendar issueOpenDate) {
        this.issueOpenDate = issueOpenDate;
    }

    public Calendar getIssueCloseDate() {
        return issueCloseDate;
    }

    public void setIssueCloseDate(Calendar issueCloseDate) {
        this.issueCloseDate = issueCloseDate;
    }

    public Calendar getIssueTargetDate() {
        return issueTargetDate;
    }

    public void setIssueTargetDate(Calendar issueTargetDate) {
        this.issueTargetDate = issueTargetDate;
    }

    public Long getIssueOwnerId() {
        return issueOwnerId;
    }

    public void setIssueOwnerId(Long issueOwnerId) {
        this.issueOwnerId = issueOwnerId;
    }

    public String getIssueOwner() {
        return issueOwner;
    }

    public void setIssueOwner(String issueOwner) {
        this.issueOwner = issueOwner;
    }

    public String getIssueComponentName() {
        return issueComponentName;
    }

    public void setIssueComponentName(String issueComponentName) {
        this.issueComponentName = issueComponentName;
    }
}
