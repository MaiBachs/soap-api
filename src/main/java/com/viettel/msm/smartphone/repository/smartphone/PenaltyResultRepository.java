package com.viettel.msm.smartphone.repository.smartphone;

import com.slyak.spring.jpa.GenericJpaRepository;
import com.viettel.msm.smartphone.repository.smartphone.entity.PenaltyResult;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface PenaltyResultRepository extends GenericJpaRepository<PenaltyResult, Long> {
    @Query("SELECT new com.viettel.msm.smartphone.repository.smartphone.entity.PenaltyResult(pr.penaltyResultId, pr.branchId, pr.branchCode, pr.channelTypeId, pr.shopId, pr.shopCode, pr.createdDate, pr.auditorId, pr.auditorCode, pr.datePlan, ct.name, pr.evaluationId) " +
            "FROM PenaltyResult pr INNER JOIN ChannelType ct ON ct.channelTypeId = pr.channelTypeId " +
            "WHERE pr.auditorId = :auditorId AND TRUNC(pr.datePlan, 'MM') = TRUNC(TO_DATE(:date, 'dd-MM-yyyy'), 'MM')")
    List<PenaltyResult> findByAuditorIdAndDatePlan(Long auditorId, String date);
}
