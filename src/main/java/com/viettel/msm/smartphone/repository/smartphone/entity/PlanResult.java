package com.viettel.msm.smartphone.repository.smartphone.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@Table(name = "PLAN_RESULT")
public class PlanResult {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "SEQ_GEN")
    @javax.persistence.SequenceGenerator(
            name = "SEQ_GEN",
            sequenceName = "PLAN_RESULT_SEQ",
            allocationSize = 1
    )
    @Column(name = "PLAN_RESULT_ID")
    private Long planResultId;

    @Column(name = "VISIT_PLAN_ID")
    private Long visitPlanId;

    @Column(name = "PLAN_JOB_ID")
    private Long planJobId;

    @Column(name = "RESULT")
    private String result;

    @Column(name = "REASON_ID")
    private Long reasonId;

    @Column(name = "FILE_PATH")
    private String filePath;

    @Column(name = "STATUS")
    private Long status;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "LAST_UPDATE")
    private Date lastUpdate;

    @Column(name = "LONGITUDE")
    private Double longitude;

    @Column(name = "LATITUDE")
    private Double latitude;

    @Column(name = "OBJECT_TYPE")
    private Long objectType;

    @Column(name = "OBJECT_ID")
    private Long objectId;

    @Column(name = "ITEM_CONFIG_ID")
    private Long itemConfigId;

    @Transient
    private Long percent;

    public PlanResult(Long planResultId, Long visitPlanId, Long planJobId, String result, Long reasonId, String filePath, Long status, Date createdDate, Date lastUpdate, Double longitude, Double latitude, Long objectType, Long objectId, Long itemConfigId, Long percent) {
        this.planResultId = planResultId;
        this.visitPlanId = visitPlanId;
        this.planJobId = planJobId;
        this.result = result;
        this.reasonId = reasonId;
        this.filePath = filePath;
        this.status = status;
        this.createdDate = createdDate;
        this.lastUpdate = lastUpdate;
        this.longitude = longitude;
        this.latitude = latitude;
        this.objectType = objectType;
        this.objectId = objectId;
        this.itemConfigId = itemConfigId;
        this.percent = percent;
    }

    public PlanResult(Long planResultId, Long visitPlanId, Long planJobId, String result, Long reasonId, String filePath, Long status, Date createdDate, Date lastUpdate, Double longitude, Double latitude, Long objectType, Long objectId, Long itemConfigId) {
        this.planResultId = planResultId;
        this.visitPlanId = visitPlanId;
        this.planJobId = planJobId;
        this.result = result;
        this.reasonId = reasonId;
        this.filePath = filePath;
        this.status = status;
        this.createdDate = createdDate;
        this.lastUpdate = lastUpdate;
        this.longitude = longitude;
        this.latitude = latitude;
        this.objectType = objectType;
        this.objectId = objectId;
        this.itemConfigId = itemConfigId;
    }

    public PlanResult(Long planResultId, Long visitPlanId, String result, Long itemConfigId, Long percent) {
        this.planResultId = planResultId;
        this.visitPlanId = visitPlanId;
        this.result = result;
        this.itemConfigId = itemConfigId;
        this.percent = percent;
    }
}
