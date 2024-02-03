package com.viettel.msm.smartphone.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalendarBean {
    private Long channelTypeId;
    private Date dateEvaluation1;
    private Date dateEvaluation2;
    private Date dateEvaluation3;
    private Date dateEvaluation4;
    private Long visitDate1;
    private Long visitDate2;
    private Long visitDate3;
    private Long visitDate4;
    private Long shopId;
    private String shopCode;
    private String shopName;
    private String jobName;
    private String jobCode;

    public CalendarBean(Long channelTypeId, Date dateEvaluation1, Date dateEvaluation2, Date dateEvaluation3, Date dateEvaluation4, Long shopId, String shopCode, String shopName, String jobName, String jobCode) {
        this.channelTypeId = channelTypeId;
        this.dateEvaluation1 = dateEvaluation1;
        this.dateEvaluation2 = dateEvaluation2;
        this.dateEvaluation3 = dateEvaluation3;
        this.dateEvaluation4 = dateEvaluation4;
        this.shopId = shopId;
        this.shopCode = shopCode;
        this.shopName = shopName;
        this.jobName = jobName;
        this.jobCode = jobCode;
    }
}
