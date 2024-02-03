package com.viettel.msm.smartphone.repository.smartphone;

import com.slyak.spring.jpa.GenericJpaRepository;
import com.viettel.msm.smartphone.repository.smartphone.entity.PlanResult;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface PlanResultRepository extends GenericJpaRepository<PlanResult, Long> {
    @Query("SELECT pr FROM PlanResult pr WHERE pr.visitPlanId = :visitPlanId ")
    List<PlanResult> findByVisitPlanId(@RequestParam("visitPlanId") Long visitPlanId);

    @Query("SELECT new com.viettel.msm.smartphone.repository.smartphone.entity.PlanResult(pr.planResultId, pr.visitPlanId, pr.result, pr.itemConfigId, ic.percent) " +
            "FROM PlanResult pr INNER JOIN ItemConfig ic ON ic.id = pr.itemConfigId " +
            "WHERE pr.visitPlanId = :visitPlanId AND pr.result = 'OK' ")
    List<PlanResult> findPLIsOkByVisitPlanId(@RequestParam("visitPlanId") Long visitPlanId);
}
