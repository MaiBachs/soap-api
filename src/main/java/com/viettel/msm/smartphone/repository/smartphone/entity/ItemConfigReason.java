package com.viettel.msm.smartphone.repository.smartphone.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "ITEM_CONFIG_REASON")
public class ItemConfigReason {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "SMARTPHONE_ITEM_CONFIG_REASON_SEQ_GEN")
    @SequenceGenerator(
            name = "SMARTPHONE_ITEM_CONFIG_REASON_SEQ_GEN",
            sequenceName = "SMARTPHONE.ITEM_CONFIG_REASON_SEQ",
            allocationSize = 1
    )
    @Column(name = "ITEM_CONFIG_REASON_ID")
    private Long itemConfigReasonId;

    @Column(name = "ITEM_CONFIG_ID")
    private Long itemConfigId;

    @Column(name = "REASON_ID")
    private Long reasonId;

    @Column(name = "STATUS")
    private Long status;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "LAST_UPDATE")
    private Date lastUpdate;

    @Column(name = "GRAVEDAD")
    private String gravedad;
}
