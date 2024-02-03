package com.viettel.msm.smartphone.repository.smartphone.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "CHANNEL_TYPE")
public class ChannelType {

    @Id
    @NonNull
    @Column(name = "CHANNEL_TYPE_ID")
    private Long channelTypeId;

    @NonNull
    @Column(name = "NAME", length = 50)
    private String name;

    @NonNull
    @Column(name = "STATUS", length = 1)
    private Integer status;

    @Column(name = "OBJECT_TYPE", length = 1)
    private String objectType;

    @Column(name = "IS_VT_UNIT", length = 1)
    private String isVtUnit;

    @Column(name = "CHECK_COMM", length = 1)
    private String checkCOMM;

    @Column(name = "STOCK_TYPE")
    private Integer STOCK_TYPE;

    @Column(name = "STOCK_REPORT_TEMPLATE", length = 50)
    private String stockReportTemplate;

    @Column(name = "TOTAL_DEBIT")
    private Long totalDebit;

    @Column(name = "MAP_STATUS", length = 100)
    private String mapStatus;

    @Column(name = "GROUP_CHANNEL", length = 20)
    private String groupChannel;

}
