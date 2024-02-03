package com.viettel.msm.smartphone.repository.sm.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

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

    @Column(name = "ID_TYPE")
    private Long idType;

    @Column(name = "ID_ISSUE_PLACE")
    private String idIssuePlace;

    @Column(name = "ID_ISSUE_DATE")
    private Date idIssueDate;

    @Column(name = "ID_EXPIRE_DATE")
    private Date idExpireDate;

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

    @Column(name = "POINT_OF_SALE")
    private String pointOfSale;

    @Column(name = "LAST_LOCK_TIME")
    private Date lastLockTime;

    @Column(name = "IMSI")
    private String imsi;

    @Column(name = "PAYMENT_LIMIT")
    private Long paymentLimit;

    @Column(name = "PAYMENT_USAGE")
    private Long paymentUsage;

    @Column(name = "DISTRICT")
    private String district;

    @Column(name = "PRECINCT")
    private String precinct;

    @Column(name = "PROFILE_STATE")
    private Long profileState;

    @Column(name = "BOARD_STATE")
    private Long boardState;

    @Column(name = "REGISTRY_DATE")
    private Date registryDate;

    @Column(name = "LAST_UPDATE_USER")
    private String lastUpdateUser;

    @Column(name = "LAST_UPDATE_IP_ADDRESS")
    private String lastUpdateIpAddress;

    @Column(name = "LAST_UPDATE_TIME")
    private Date lastUpdateTime;

    @Column(name = "LAST_UPDATE_KEY")
    private String lastUpdateKey;

    @Column(name = "IS_WARNING_LOGIN")
    private Long isWarningLogin;

    @Column(name = "TRADE_NAME")
    private String tradeName;

    @Column(name = "CONTACT_NAME")
    private String contactName;

    @Column(name = "USEFUL_WIDTH")
    private String usefulWidth;

    @Column(name = "SURFACE_AREA")
    private String surfaceArea;

    @Column(name = "SYNC_STATUS")
    private Long syncStatus;

    @Column(name = "CHECK_VAT")
    private Long checkVat;

    @Column(name = "AGENT_TYPE")
    private Long agentType;

    @Column(name = "NOTE")
    private String note;

    @Column(name = "STREET_BLOCK_NAME")
    private String streetBlockName;

    @Column(name = "STREET_NAME")
    private String streetName;

    @Column(name = "HOME")
    private String home;

    @Column(name = "CONTRACT_METHOD")
    private Long contractMethod;

    @Column(name = "HAS_EQUIPMENT")
    private Long hasEquipment;

    @Column(name = "BUSINESS_METHOD")
    private Long businessMethod;

    @Column(name = "HAS_TIN")
    private Long hasTin;

    @Column(name = "TIN")
    private String tin;

    @Column(name = "AREA_CODE")
    private String areaCode;

    @Column(name = "STOCK_NUM_IMP")
    private Long stockNumImp;

    @Column(name = "STOCK_NUM")
    private Long stockNum;

    @Column(name = "POINT_OF_SALE_TYPE")
    private Long pointOfSaleType;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "BTS_ID")
    private Long btsId;

    @Column(name = "BTS_UPDATE_TIME")
    private Date btsUpdateTime;

    @Column(name = "CHILD_CHANNEL")
    private String childChannel;

    @Column(name = "BANK_NAME")
    private String bankName;

    @Column(name = "DNI_CUST")
    private String dniCust;

    @Column(name = "EMAIL_CUST")
    private String emailCust;

    @Column(name = "TEL_CUST")
    private String telCust;

    @Column(name = "REGISTRY_CUST")
    private Date registryCust;

    @Column(name = "PLACE_CUST")
    private String placeCust;

    @Column(name = "EXPIRE_CUST")
    private Date expireCust;

    @Column(name = "CONTRACT_NO")
    private String contractNo;

    @Column(name = "SIGN_CONTRACT")
    private Date signContract;

    @Column(name = "EXPIRE_CONTRACT")
    private Date expireContract;

    @Column(name = "ACCOUNT")
    private String account;

    @Column(name = "VECTOR")
    private String vector;

    @Column(name = "CHANNEL_NAME")
    private String channelName;

    @Column(name = "NAME_CONTACT")
    private String nameContact;

    @Column(name = "ID_TYPE_CUST")
    private Long idTypeCust;

    @Column(name = "CREATE_DATE")
    private java.sql.Timestamp createDate;

    @Column(name = "X")
    private Double x;

    @Column(name = "Y")
    private Double y;

    @Column(name = "POS_CODE")
    private String posCode;

    @Column(name = "POS_NAME")
    private String posName;

    @Column(name = "POS_ID")
    private Long posId;

    @Column(name = "IP")
    private String ip;

    @Column(name = "GENDER")
    private String gender;

    @Column(name = "CANCEL_TIME")
    private Date cancelTime;

    @Column(name = "LONGITUDE")
    private Long longitude;

    @Column(name = "LATITUDE")
    private Long latitude;

    @Column(name = "SUBSCRIBER_ACCOUNT")
    private String subscriberAccount;

    @Column(name = "IS_PAY_BY_MONEY")
    private Long isPayByMoney;

    @Column(name = "IS_PAY_BY_ANYPAY")
    private Long isPayByAnypay;

    @Column(name = "CONNECT_MNP")
    private Long connectMnp;

    @Column(name = "CONNECT_POSTPAID")
    private Long connectPostpaid;

    @Column(name = "ACTIVE_PREPAID")
    private Long activePrepaid;

    @Column(name = "LAST_LOGIN")
    private Date lastLogin;

    @Column(name = "IS_BUY_ANYPAY_ORDER")
    private Long isBuyAnypayOrder;

    @Column(name = "LOCK_STATUS")
    private Long lockStatus;

    @Column(name = "SIM_MULTIFUNCTION")
    private Long simMultifunction;

    @Column(name = "APPROVE_STATUS")
    private Long approveStatus;

    @Column(name = "APPROVE_USER")
    private String approveUser;

    @Column(name = "APPROVE_DATE")
    private Date approveDate;

    @Column(name = "APPROVE_NOTE")
    private String approveNote;

    @Column(name = "IS_MOTOKAR")
    private Long isMotokar;

    @Column(name = "LOCATION_REF")
    private String locationRef;

    @Column(name = "SIGN_DOCUMENT")
    private Long signDocument;

    @Column(name = "AGENT_OWNER_ID")
    private Long agentOwnerId;

    @Column(name = "APP")
    private String app;
}
