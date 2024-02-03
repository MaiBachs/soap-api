package com.viettel.msm.smartphone.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CheckListBean{
    private Long id;
    private Long brId;
    private String brName;
    private String brCode;
    private Long channelTypeId;
    private Long jobId;
    private Long quantityPerMonth;
    private Float approvalScore;
    private Date dateEvaluation1;
    private Date dateEvaluation2;
    private Date dateEvaluation3;
    private Date dateEvaluation4;
    private String jobName;
    private String jobCode;

    public CheckListBean(Long id, Long brId, String brCode,Long channelTypeId, Long jobId, Long quantityPerMonth, Float approvalScore, Date dateEvaluation1, Date dateEvaluation2, Date dateEvaluation3, Date dateEvaluation4, String jobName, String jobCode) {
        this.id = id;
        this.brId = brId;
        this.brCode = brCode;
        this.channelTypeId = channelTypeId;
        this.jobId = jobId;
        this.quantityPerMonth = quantityPerMonth;
        this.approvalScore = approvalScore;
        this.dateEvaluation1 = dateEvaluation1;
        this.dateEvaluation2 = dateEvaluation2;
        this.dateEvaluation3 = dateEvaluation3;
        this.dateEvaluation4 = dateEvaluation4;
        this.jobName = jobName;
        this.jobCode = jobCode;
    }

    public CheckListBean(Long id, Long brId, String brCode,Long channelTypeId, Long jobId, Float approvalScore, Date dateEvaluation1, Date dateEvaluation2, Date dateEvaluation3, Date dateEvaluation4, String jobName, String jobCode) {
        this.id = id;
        this.brId = brId;
        this.brCode = brCode;
        this.channelTypeId = channelTypeId;
        this.jobId = jobId;
        this.approvalScore = approvalScore;
        this.dateEvaluation1 = dateEvaluation1;
        this.dateEvaluation2 = dateEvaluation2;
        this.dateEvaluation3 = dateEvaluation3;
        this.dateEvaluation4 = dateEvaluation4;
        this.jobName = jobName;
        this.jobCode = jobCode;
    }
}
