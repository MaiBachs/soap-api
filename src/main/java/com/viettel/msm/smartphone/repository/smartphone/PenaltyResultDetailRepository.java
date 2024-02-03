package com.viettel.msm.smartphone.repository.smartphone;

import com.slyak.spring.jpa.GenericJpaRepository;
import com.viettel.msm.smartphone.repository.smartphone.entity.PenaltyResultDetail;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PenaltyResultDetailRepository extends GenericJpaRepository<PenaltyResultDetail, Long> {
    @Query("SELECT new com.viettel.msm.smartphone.repository.smartphone.entity.PenaltyResultDetail(prd.penaltyResultDetailId, prd.penaltyResultId, prd.evaluationId, prd.penalidad, prd.createDate, prd.itemId, je.name, ji.name) " +
            "FROM PenaltyResultDetail prd " +
            "INNER JOIN PenaltyResult pr ON prd.penaltyResultId = pr.penaltyResultId AND TRUNC(pr.datePlan, 'MM') = TRUNC(TO_DATE(:date, 'dd-MM-yyyy'), 'MM') AND pr.auditorId = :auditorId " +
            "INNER JOIN Job je ON je.jobId = prd.evaluationId " +
            "INNER JOIN Job ji ON ji.jobId = prd.itemId")
    List<PenaltyResultDetail> findAllByAuditorIdInMonth(Long auditorId, String date);
}
