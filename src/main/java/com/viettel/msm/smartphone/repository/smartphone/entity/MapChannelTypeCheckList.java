package com.viettel.msm.smartphone.repository.smartphone.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "MAP_CHANNEL_TYPE_CHECK_LIST")
public class MapChannelTypeCheckList implements java.io.Serializable{
    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "BR_ID")
    private Long brId;

    @Column(name = "BR_CODE")
    private String brCode;

    @Column(name = "CHANNEL_TYPE_ID")
    private Long channelTypeId;

    @Column(name = "CHANNEL_TYPE_NAME")
    private String channelTypeName;

    @Column(name = "JOB_ID")
    private Long jobId;

    @Column(name = "JOB_CODE")
    private String jobCode;

    @Column(name = "QUANTITY_PER_MONTH")
    private Long quantityPerMonth;

    @Column(name = "APPROVAL_SCORE")
    private Float approvalScore;

    @Column(name = "DATE_EVALUATION_1")
    private java.sql.Date dateEvaluation1;

    @Column(name = "DATE_EVALUATION_2")
    private java.sql.Date dateEvaluation2;

    @Column(name = "DATE_EVALUATION_3")
    private java.sql.Date dateEvaluation3;

    @Column(name = "DATE_EVALUATION_4")
    private java.sql.Date dateEvaluation4;

    @CreatedBy
    @Column(name = "CREATED_BY")
    private String createdBy;

    @CreatedDate
    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @LastModifiedBy
    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @LastModifiedDate
    @Column(name = "UPDATED_DATE")
    private Date updatedDate;

}
