package com.viettel.msm.smartphone.repository.smartphone.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "PENALTY_RESULT")
public class PenaltyResult {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "SMARTPHONE_PENALTY_RESULT_SEQ_GEN")
    @SequenceGenerator(
            name = "SMARTPHONE_PENALTY_RESULT_SEQ_GEN",
            sequenceName = "SMARTPHONE.PENALTY_RESULT_SEQ",
            allocationSize = 1
    )
    @Column(name = "PENALTY_RESULT_ID")
    private Long penaltyResultId;

    @Column(name = "BRANCH_ID")
    private Long branchId;

    @Column(name = "BRANCH_CODE")
    private String branchCode;

    @Column(name = "CHANNEL_TYPE_ID")
    private Long channelTypeId;

    @Column(name = "SHOP_ID")
    private Long shopId;

    @Column(name = "SHOP_CODE")
    private String shopCode;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "AUDITOR_ID")
    private Long auditorId;

    @Column(name = "AUDITOR_CODE")
    private String auditorCode;

    @Column(name = "DATE_PLAN")
    private Date datePlan;

    @Column(name = "EVALUATION_ID")
    private Long evaluationId;

    @Transient
    private String channelTypeName;

    public PenaltyResult(Long penaltyResultId, Long branchId, String branchCode, Long channelTypeId, Long shopId, String shopCode, Date createdDate, Long auditorId, String auditorCode, Date datePlan, String channelTypeName, Long evaluationId) {
        this.penaltyResultId = penaltyResultId;
        this.branchId = branchId;
        this.branchCode = branchCode;
        this.channelTypeId = channelTypeId;
        this.shopId = shopId;
        this.shopCode = shopCode;
        this.createdDate = createdDate;
        this.auditorId = auditorId;
        this.auditorCode = auditorCode;
        this.datePlan = datePlan;
        this.channelTypeName = channelTypeName;
        this.evaluationId = evaluationId;
    }
}
