package com.viettel.msm.smartphone.bean;

import lombok.Data;

@Data
public class StaffBean {
    private Long staffId;
    private String staffCode;

    public StaffBean(Long staffId, String staffCode) {
        this.staffId = staffId;
        this.staffCode = staffCode;
    }
}
