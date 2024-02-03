package com.viettel.msm.smartphone.repository.smartphone.entity;

import lombok.Data;

@Data
public class AppParams {
    private String type;
    private String code;
    private String name;
    private String status;
    private String value;
    private Long telecomServiceId;
}
