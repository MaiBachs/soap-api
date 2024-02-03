package com.viettel.msm.smartphone.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ActionPlanChannelBean {
    private Long branchId;
    private Long pdvId;
    private String pdvCode;
    private Long channelTypeId;
}
