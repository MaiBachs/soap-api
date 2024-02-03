package com.viettel.msm.smartphone.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ItemConfigBean {
    private Long id;
    private String itemName;
    private Long jobId;
    private Long percent;
    private Long ok;
    private Long nok;
    private Long na;
    private String validation;
    private String url;
    private Long status;
    private Long evaluationId;
    private String evaluationName;
    private Long groupId;
    private String groupName;
    private Long visitPlanId;
    private Long planResultId;

    public ItemConfigBean(Long id, String itemName, Long jobId, Long percent, Long ok, Long nok, Long na, String validation, String url, Long status, Long evaluationId, String evaluationName, Long groupId, String groupName) {
        this.id = id;
        this.itemName = itemName;
        this.jobId = jobId;
        this.percent = percent;
        this.ok = ok;
        this.nok = nok;
        this.na = na;
        this.validation = validation;
        this.url = url;
        this.status = status;
        this.evaluationId = evaluationId;
        this.evaluationName = evaluationName;
        this.groupId = groupId;
        this.groupName = groupName;
    }

    public ItemConfigBean(Long id, String itemName, Long jobId, Long percent, Long ok, Long nok, Long na, String validation, String url, Long status, Long evaluationId, String evaluationName, Long groupId, String groupName, Long visitPlanId, Long planResultId) {
        this.id = id;
        this.itemName = itemName;
        this.jobId = jobId;
        this.percent = percent;
        this.ok = ok;
        this.nok = nok;
        this.na = na;
        this.validation = validation;
        this.url = url;
        this.status = status;
        this.evaluationId = evaluationId;
        this.evaluationName = evaluationName;
        this.groupId = groupId;
        this.groupName = groupName;
        this.visitPlanId = visitPlanId;
        this.planResultId = planResultId;
    }
}
