package com.viettel.msm.smartphone.repository.smartphone.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "ITEM_CONFIG")
public class ItemConfig {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "SMARTPHONE_ITEM_CONFIG_SEQ_GEN")
    @SequenceGenerator(
            name = "SMARTPHONE_ITEM_CONFIG_SEQ_GEN",
            sequenceName = "SMARTPHONE.ITEM_CONFIG_SEQ",
            allocationSize = 1
    )
    @Column(name = "ID")
    private Long id;

    @Column(name = "CHANNEL_TYPE")
    private String channelType;

    @Column(name = "CHANNEL_TYPE_ID")
    private Long channelTypeId;

    @Column(name = "EVALUATION_ID")
    private Long evaluationId;

    @Column(name = "GROUP_ID")
    private Long groupId;

    @Column(name = "JOB_ID")
    private Long jobId;

    @Column(name = "PERCENT")
    private Long percent;

    @Column(name = "OK")
    private Long ok;

    @Column(name = "NOK")
    private Long nok;

    @Column(name = "NA")
    private Long na;

    @Column(name = "VALIDATION")
    private String validation;

    @Column(name = "URL")
    private String url;

    @Column(name = "STATUS")
    private Long status;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "LAST_UPDATE")
    private Date lastUpdate;
}
