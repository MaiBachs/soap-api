package com.viettel.msm.smartphone.repository.sm;

import com.viettel.msm.smartphone.bean.ActionPlanChannelBean;
import com.viettel.msm.smartphone.repository.sm.entity.VisitPlanMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ActionPlanRepository extends JpaRepository<VisitPlanMap, Long>, JpaSpecificationExecutor<VisitPlanMap> {

    @Query(value = "SELECT vpm FROM VisitPlanMap vpm WHERE vpm.branchId = :branchId AND vpm.channelTypeId = :channelTypeId AND vpm.datePlan IS NOT NULL AND vpm.parentId IS NOT NULL AND vpm.zonalId = :zonalId AND vpm.pdvId = :pdvId AND TRUNC(vpm.createdDate , 'MM') = TRUNC(SYSDATE, 'MM')")
    List<VisitPlanMap> findByBranchIdAndChannelTypeIdIAndAuditIdnMonth(@Param("branchId") Long branchId, @Param("channelTypeId") Long channelTypeId, @Param("zonalId") Long zonalId, @Param("pdvId") Long pdvId);

    @Query(value = "SELECT new com.viettel.msm.smartphone.bean.ActionPlanChannelBean(vpm.branchId, vpm.pdvId, vpm.pdvCode, vpm.channelTypeId) FROM VisitPlanMap vpm WHERE vpm.datePlan IS NOT NULL AND vpm.parentId IS NOT NULL AND vpm.isActionPlan = 1 AND vpm.zonalId = :zonalId AND TRUNC(vpm.createdDate , 'MM') = TRUNC(SYSDATE, 'MM')")
    List<ActionPlanChannelBean> findActionPlanChannelInMonth(@Param("zonalId") Long zonalId);
}
