package com.viettel.msm.smartphone.repository.smartphone.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "JOB")
public class Job {
    @Id
    @Column(name = "JOB_ID")
    private Long jobId;

    @Column(name = "PARENT_ID")
    private Long parentId;

    @Column(name = "CODE")
    private String code;

    @Column(name = "NAME")
    private String name;

    @Column(name = "RESULT_DATA_TYPE")
    private Long resultDataType;

    @Column(name = "CATEGORY")
    private Long category;

    @Column(name = "STATUS")
    private Long status;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "LAST_UPDATE")
    private Date lastUpdate;

    @Column(name = "CC_AUDIT")
    private Integer ccAudit;
}
