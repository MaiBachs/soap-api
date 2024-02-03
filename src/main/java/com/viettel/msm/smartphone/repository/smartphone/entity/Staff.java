package com.viettel.msm.smartphone.repository.smartphone.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "STAFF")
public class Staff {
    @Id
    @Column(name = "STAFF_ID")
    private Long staffId;

    @Column(name = "SHOP_ID")
    private Long shopId;

    @Column(name = "STAFF_CODE")
    private String staffCode;

    @Column(name = "NAME")
    private String name;

    @Column(name = "STATUS")
    private Long status;

    @Column(name = "BIRTHDAY")
    private java.sql.Date birthday;

    @Column(name = "ID_NO")
    private String idNo;

    @Column(name = "ID_ISSUE_PLACE")
    private String idIssuePlace;

    @Column(name = "ID_ISSUE_DATE")
    private java.sql.Date idIssueDate;

    @Column(name = "TEL")
    private String tel;

    @Column(name = "TYPE")
    private Long type;

    @Column(name = "SERIAL")
    private String serial;

    @Column(name = "ISDN")
    private String isdn;

    @Column(name = "PIN")
    private String pin;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "PROVINCE")
    private String province;

    @Column(name = "STAFF_OWN_TYPE")
    private String staffOwnType;

    @Column(name = "STAFF_OWNER_ID")
    private Long staffOwnerId;

    @Column(name = "CHANNEL_TYPE_ID")
    private Long channelTypeId;

    @Column(name = "PRICE_POLICY")
    private String pricePolicy;

    @Column(name = "DISCOUNT_POLICY")
    private String discountPolicy;

    @Column(name = "LOCK_STATUS")
    private Long lockStatus;

    @Column(name = "LAST_LOCK_TIME")
    private java.sql.Date lastLockTime;

    @Column(name = "MAP_STATUS")
    private String mapStatus;

    @Column(name = "STAFF_LEVEL")
    private Long staffLevel;

    @Column(name = "STAFF_TYPE")
    private Long staffType;

    @Column(name = "ISDN_AGENT")
    private String isdnAgent;

    @Column(name = "TRACKING_STATUS")
    private Long trackingStatus;

    @Column(name = "CHANGE_DATETIME")
    private java.sql.Date changeDatetime;

    @Column(name = "LAST_UPDATED_USER")
    private String lastUpdatedUser;

    @Column(name = "X")
    private Double x;

    @Column(name = "Y")
    private Double y;

    @Column(name = "BOARD_TYPE")
    private String boardType;

    @Column(name = "LENGTHS")
    private Long lengths;

    @Column(name = "WIDTH")
    private Long width;

    @Column(name = "COST")
    private Long cost;

    @Column(name = "INSTALL_DATE")
    private java.sql.Date installDate;

    @Column(name = "BOARD_STATUS")
    private Long boardStatus;

    @Column(name = "IMG_PATH")
    private String imgPath;

    @Column(name = "BUSINESS_METHOD")
    private Long businessMethod;

    @Column(name = "CONTRACT_METHOD")
    private Long contractMethod;

    @Column(name = "NUM_OF_VISIT")
    private Long numOfVisit;

    @Column(name = "IMG_URL")
    private String imgUrl;

    @Column(name = "REGISTER_INFO")
    private String registerInfo;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "TTNS_CODE")
    private String ttnsCode;

    @Column(name = "POINT_OF_SALE_TYPE")
    private Long pointOfSaleType;

    @Column(name = "DISTRICT")
    private String district;

    @Column(name = "PRECINCT")
    private String precinct;

    @Column(name = "SUB_OWNER_ID")
    private Long subOwnerId;

    @Column(name = "SUB_OWNER_TYPE")
    private String subOwnerType;

    @Column(name = "BANNER_WIDTH")
    private Long bannerWidth;

    @Column(name = "BANNER_LENGTH")
    private Long bannerLength;

    @Column(name = "BTS_ID")
    private Long btsId;

    @Column(name = "BTS_UPDATE_TIME")
    private java.sql.Date btsUpdateTime;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "STREET_NAME")
    private String streetName;

    @Column(name = "STREET_BLOCK_NAME")
    private String streetBlockName;

    @Column(name = "AGENT_TYPE")
    private String agentType;

    @Column(name = "CHECK_VAT")
    private String checkVat;

    @Column(name = "SYNC_STATUS")
    private String syncStatus;

    @Column(name = "IS_WARNING_LOGIN")
    private String isWarningLogin;

    @Column(name = "LAST_UPDATE_KEY")
    private java.sql.Date lastUpdateKey;

    @Column(name = "LAST_UPDATE_TIME")
    private java.sql.Date lastUpdateTime;

    @Column(name = "LAST_UPDATE_IP_ADDRESS")
    private String lastUpdateIpAddress;

    @Column(name = "LAST_UPDATE_USER")
    private String lastUpdateUser;

    @Column(name = "REGISTRY_DATE")
    private java.sql.Date registryDate;

    @Column(name = "PROFILE_STATE")
    private String profileState;

    @Column(name = "BOARD_STATE")
    private String boardState;

    @Column(name = "CHILD_CHANNEL")
    private String childChannel;
}
