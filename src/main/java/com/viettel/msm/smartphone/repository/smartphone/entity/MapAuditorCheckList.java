package com.viettel.msm.smartphone.repository.smartphone.entity;

import lombok.Data;
import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "MAP_AUDITOR_CHECK_LIST")
public class MapAuditorCheckList implements java.io.Serializable{
    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "BR_ID")
    private Long brId;

    @Column(name = "BR_CODE")
    private String brCode;

    @Column(name = "AUDITOR_ID")
    private Long auditorId;

    @Column(name = "CHANNEL_TYPE_ID")
    private Long channelTypeId;

    @Column(name = "SHOP_ID")
    private Long shopId;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "UPDATED_DATE")
    private Date updatedDate;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @Transient
    private String auditorName;

    @Transient
    private String channelTypeName;

    @Transient
    private String shopCode;

    @Transient
    private String shopName;
}
