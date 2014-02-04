package com.punwire.oa.domain;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Calendar;

/**
 * Created by kanwal on 1/30/14.
 */
@Entity
@XmlRootElement
@Table(name = "sm_comments")

@SqlResultSetMappings({
        @SqlResultSetMapping(
                name = "SmCommentV",
                entities = {
                        @EntityResult(
                                entityClass = com.punwire.oa.domain.SmComment.class,
                                fields = {
                                        @FieldResult(name="commentOwner", column="comment_owner")
                                }
                        )}
        )
})

public class SmComment {

    @Id
    @SequenceGenerator(name="SmCommentSeq",sequenceName = "sm_comment_s",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SmCommentSeq")
    @Column(name = "id", updatable = false, nullable = false)
    private Long id = null;

    @Column(name ="comment_text")
    private String commentText;

    @Column(name ="comment_owner_id")
    private Long commentOwnerId;

    @Column(name="comment_time")
    @Temporal(TemporalType.DATE)
    private Calendar commentTime;

    private String commentOwner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public Long getCommentOwnerId() {
        return commentOwnerId;
    }

    public void setCommentOwnerId(Long commentOwnerId) {
        this.commentOwnerId = commentOwnerId;
    }

    public String getCommentOwner() {
        return commentOwner;
    }

    public void setCommentOwner(String commentOwner) {
        System.out.println("Setting commentOwner " + commentOwner);
        this.commentOwner = commentOwner;
    }

    public Calendar getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Calendar commentTime) {
        this.commentTime = commentTime;
    }
}
