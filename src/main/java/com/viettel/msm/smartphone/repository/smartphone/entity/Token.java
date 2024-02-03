package com.viettel.msm.smartphone.repository.smartphone.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "TOKEN")
public class Token implements java.io.Serializable{
    @Id
    @Column(name = "USER_NAME")
    private String userName;
    private String serial;
    private String tokenValue;
    private Date createTime;
    private Date lastRequest;
    private Long status;
    private Long staffId;
    private String permission;
    private String staffName;
    private long connectStatus;
    @Transient
    private String ip;
    @Transient
    private String location;
    @Transient
    private String traceCode;

    @Column(name = "APP_CODE")
    private String appCode;

    public Token(String userName, String serial, String tokenValue, Date createTime, Date lastRequest, Long status) {
        this.userName = userName;
        this.serial = serial;
        this.tokenValue = tokenValue;
        this.createTime = createTime;
        this.lastRequest = lastRequest;
        this.status = status;
    }
}

