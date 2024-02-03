package com.viettel.msm.smartphone.repository.sm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "VISIT_PLAN_MAP")
public class VisitPlanMap {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "SEQ_GEN")
    @javax.persistence.SequenceGenerator(
            name = "SEQ_GEN",
            sequenceName = "visit_plan_map_SEQ",
            allocationSize = 1
    )
    @Column(name = "VISIT_PLAN_ID")
    private Long id;

    @Column(name = "BRANCH_ID")
    private Long branchId;

    @Column(name = "BRANCH_CODE")
    private String branchCode;

    @Column(name = "BC_ID")
    private Long bcId;

    @Column(name = "BC_CODE")
    private String bcCode;

    @Column(name = "CHANNEL_FROM_ID")
    private Long zonalId;

    @Column(name = "CHANNEL_FROM_CODE")
    private String zonalCode;

    @Column(name = "CHANNEL_TO_ID")
    private Long pdvId;

    @Column(name = "CHANNEL_TO_CODE")
    private String pdvCode;

    @Column(name = "OBJECT_TYPE")
    private String pdvChannelObjectType;

    @Column(name = "DATE_PLAN")
    @Temporal(TemporalType.DATE)
    private Date datePlan;

    @Column(name = "STATUS")
    private Long status;

    @Column(name = "IS_DETAIL")
    private Long isDetail;

    @Column(name = "CHECKLIST_RESULT_STATUS")
    private Integer checkListResultStatus;

    @Column(name = "CHECKLIST_RESULT_APPROVE_USER")
    private String checkListResultApproveUser;

    @Column(name = "CHECKLIST_RESULT_COMMENT")
    private String checkListResultCommnet;

    @CreationTimestamp
    @Column(name = "CREATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @UpdateTimestamp
    @Column(name = "UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;

    @Column(name = "VISIT_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date visitTime;

    @Column(name = "FUNCTION_TYPE")
    private int functionType;

    @Transient
    private String comment;
    @Transient
    private Integer index;

    @Transient
    private String datePlanText;

    @Column(name = "USER_REVIEW_ID")
    private Long userReviewId;

    @Column(name = "USER_REVIEW_NAME")
    private String userReviewName;

    @Column(name = "SCORE")
    private Float score;

    @Column(name = "REVIEW_DATE")
    private Date reviewDate;

    @Column(name = "PARENT_ID")
    private Long parentId;

    @Column(name = "CHANNEL_TYPE_ID")
    private Long channelTypeId;

    @Column(name = "IS_ACTION_PLAN")
    private Integer isActionPlan;

    @Transient
    private String channelTypeName;

    @Column(name = "JOB_ID")
    private Long jobId;

    @Transient
    private String jobName;

    @Transient
    private String statusName;

    @Override
    public boolean equals(Object obj) {
        VisitPlanMap v = (VisitPlanMap) obj;
        return this.zonalCode.equalsIgnoreCase(v.getZonalCode())
                && this.pdvCode.equalsIgnoreCase(v.getPdvCode())
                && this.datePlanText.compareTo(v.datePlanText) == 0;
    }

    @PrePersist
    public void prePersist() {
        createdDate = new Date();
        updatedDate = new Date();
        if (this.status == null){
            this.status = 0l;
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedDate = new Date();
    }
}