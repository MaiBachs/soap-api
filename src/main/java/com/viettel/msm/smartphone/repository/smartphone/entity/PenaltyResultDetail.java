package com.viettel.msm.smartphone.repository.smartphone.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(name = "PENALTY_RESULT_DETAIL")
public class PenaltyResultDetail {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "SMARTPHONE_PENALTY_RESULT_DETAIL_GEN")
    @SequenceGenerator(
            name = "SMARTPHONE_PENALTY_RESULT_DETAIL_SEQ_GEN",
            sequenceName = "SMARTPHONE.PENALTY_RESULT_DETAIL_SEQ",
            allocationSize = 1
    )
    @Column(name = "PENALTY_RESULT_DETAIL_ID")
    private Long penaltyResultDetailId;

    @Column(name = "PENALTY_RESULT_ID")
    private Long penaltyResultId;

    @Column(name = "EVALUATION_ID")
    private Long evaluationId;

    @Column(name = "PENALIDAD")
    private String penalidad;

    @Column(name = "CREATE_DATE")
    private Date createDate;

    @Column(name = "ITEM_ID")
    private Long itemId;

    @Transient
    private String evaluationName;
    @Transient
    private String itemName;

    public PenaltyResultDetail(Long penaltyResultDetailId, Long penaltyResultId, Long evaluationId, String penalidad, Date createDate, Long itemId, String evaluationName, String itemName) {
        this.penaltyResultDetailId = penaltyResultDetailId;
        this.penaltyResultId = penaltyResultId;
        this.evaluationId = evaluationId;
        this.penalidad = penalidad;
        this.createDate = createDate;
        this.itemId = itemId;
        this.evaluationName = evaluationName;
        this.itemName = itemName;
    }
}
