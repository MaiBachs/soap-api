package com.viettel.msm.smartphone.repository.smartphone;

import com.slyak.spring.jpa.GenericJpaRepository;
import com.viettel.msm.smartphone.repository.smartphone.entity.Plan;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlanRepository extends GenericJpaRepository<Plan, Long>{
    @Query(value = "SELECT DISTINCT p.channelTypeId FROM Plan p WHERE p.ccAudit = :ccAudit")
    List<Long> findByCcAudit(Long ccAudit);
}
