package com.viettel.msm.smartphone.repository.smartphone.entity;

import lombok.Data;
import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "PLAN",schema = "SMARTPHONE")
public class Plan {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "PLAN_SEQ_GEN")
    @SequenceGenerator(
            name = "PLAN_SEQ_GEN",
            sequenceName = "SMARTPHONE.PLAN_SEQ",
            allocationSize = 1
    )
    @Column(name = "PLAN_ID")
    private Long planId;

    @Column(name = "OBJECT_TYPE")
    private Long objectType;

    @Column(name = "OBJECT_LEVEL")
    private Long objectLevel;

    @Column(name = "CHANNEL_TYPE_ID")
    private Long channelTypeId;

    @Column(name = "BRANCH_ID")
    private Long branchId;

    @Column(name = "SHOP_ID")
    private Long shopId;

    @Column(name = "CENTER_ID")
    private Long centerId;

    @Column(name = "STAFF_ID")
    private Long staffId;

    @Column(name = "FREQUENCY")
    private Long frequency;

    @Column(name = "FREQUENCY_UNIT")
    private Long frequencyUnit;

    @Column(name = "NOTE")
    private String note;

    @Column(name = "STATUS")
    private Long status;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "LAST_UPDATE")
    private Date lastUpdate;

    @Column(name = "CC_AUDIT")
    private Long ccAudit;

    @Column(name = "FUNCTION_TYPE")
    private Long functionType;

    @Column(name = "LOCATION")
    private Long location;

}

