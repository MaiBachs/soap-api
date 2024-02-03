package com.viettel.msm.smartphone.repository.smartphone.entity;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "shop",  schema = "smartphone")
public class Shop {
    @Id
    @Column(name = "SHOP_ID")
    private Long shopId;
    @Column(name = "NAME")
    private String name;
    @Column(name = "SHOP_CODE")
    private String shopCode;
    @Column(name = "CHANNEL_TYPE_ID")
    private Long channelTypeId;
    @Column(name = "PARENT_SHOP_ID")
    private Long parentShopId;
    @Column(name = "X")
    private Double x;
    @Column(name = "Y")
    private Double y;
}
