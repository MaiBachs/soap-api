package com.viettel.msm.smartphone.repository.sm;

import com.viettel.msm.smartphone.bean.StaffBean;
import com.viettel.msm.smartphone.repository.sm.entity.VisitPlanMap;
import com.viettel.msm.smartphone.repository.smartphone.entity.MapAuditorCheckList;
import com.viettel.msm.smartphone.ws.MapAuditorCheckListDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface VisitPlanMapRepository extends JpaRepository<VisitPlanMap, Long> {
    @Query(value = "SELECT vpm FROM VisitPlanMap vpm WHERE vpm.branchId = :branchId AND vpm.isActionPlan IS NULL AND vpm.pdvId = :channelToId AND TRUNC(vpm.datePlan, 'MM') = TRUNC(SYSDATE, 'MM') ORDER BY vpm.id DESC")
    List<VisitPlanMap> findVisitPlanBybrIdAndChannelId(@Param("branchId") Long branchId, @Param("channelToId") Long channelToId);

    @Query("SELECT vpm FROM VisitPlanMap vpm WHERE " +
            "vpm.zonalId = :zonalId " +
            "AND ( :channelTypeId IS NULL OR vpm.channelTypeId = :channelTypeId )" +
            "AND ( :pdvId IS NULL OR vpm.pdvId = :pdvId )" +
            "AND ( :status IS NULL OR vpm.status = :status )" +
            "AND vpm.parentId IS NOT NULL " +
            "AND vpm.isActionPlan = 1 " +
            "AND ( vpm.datePlan IS NULL OR (vpm.datePlan >= TO_DATE(:fromDate, 'DD-MM-YYYY') AND vpm.datePlan <= TO_DATE(:toDate, 'DD-MM-YYYY') + 1 ) )" +
            "ORDER BY CASE WHEN vpm.datePlan IS NULL THEN 0 ELSE 1 END, vpm.datePlan ASC ")
    List<VisitPlanMap> findActionPlan(@Param("zonalId") Long zonalId , @Param("channelTypeId") Long channelTypeId, @Param("pdvId") Long pdvId,@Param("status" ) Long status, @Param("fromDate") String fromDate, @Param("toDate") String toDate);

    @Query("SELECT new com.viettel.msm.smartphone.bean.StaffBean(vpm.pdvId, vpm.pdvCode) FROM VisitPlanMap vpm WHERE vpm.parentId = :parentId AND vpm.isActionPlan IS NULL AND TRUNC(vpm.datePlan, 'DD') >= TRUNC(vpm.visitTime, 'DD') AND TRUNC(vpm.datePlan, 'MM') = TRUNC(SYSDATE, 'MM')")
    List<StaffBean> finVisitPlanOfCalidadByParentId(@Param("parentId") Long parentId);

    @Query(value = "SELECT vpm.* FROM VISIT_PLAN_MAP vpm WHERE vpm.BRANCH_ID = :branchId AND vpm.CHANNEL_TO_ID = :channelToId AND vpm.OBJECT_TYPE = '1' AND vpm.DATE_PLAN = TO_DATE(:datePlan, 'YYYY-MM-DD')", nativeQuery = true)
    VisitPlanMap finVisitPlanStaffOfCalidad(@Param("branchId") Long branchId, @Param("channelToId") Long channelToId, @Param("datePlan") String datePlan);

    @Query("SELECT vpm FROM VisitPlanMap vpm WHERE vpm.zonalId = :zonalId AND vpm.pdvId = :pdvId AND TRUNC(vpm.datePlan, 'MM') = TRUNC(TO_DATE(:date, 'dd-MM-yyyy'), 'MM')")
    List<VisitPlanMap> findByZonalIdAndPdvIdAndDatePlan(Long zonalId, Long pdvId, String date);

    @Query("SELECT v.pdvId FROM VisitPlanMap v WHERE v.isActionPlan = 1 AND v.status = 11 AND v.zonalId = :auditorId " +
            "AND TRUNC(v.datePlan, 'MM') = TRUNC(SYSDATE, 'MM')")
    List<Long> findShopIdOfActionPlanCheckList(@Param("auditorId") Long auditorId);

    @Query(value = "SELECT vpm FROM VisitPlanMap vpm WHERE vpm.branchId = :branchId AND vpm.isActionPlan IS NULL " +
            "AND vpm.pdvId = :channelToId " +
            "AND vpm.id IN (SELECT v.parentId FROM VisitPlanMap v WHERE v.branchId = :branchId " +
            "AND v.pdvId = :channelToId AND v.isActionPlan = 1 AND v.status = 11 AND TRUNC(v.datePlan, 'MM') = TRUNC(SYSDATE, 'MM'))" +
            "AND TRUNC(vpm.datePlan, 'MM') = TRUNC(SYSDATE, 'MM') " +
            "ORDER BY vpm.id DESC")
    List<VisitPlanMap> findVisitPlanActionPlanBybrIdAndChannelId(@Param("branchId") Long branchId, @Param("channelToId") Long channelToId);

    @Query("SELECT v.parentId FROM VisitPlanMap v WHERE v.isActionPlan = 1 AND v.status = 11 AND v.zonalId = :auditorId " +
            "AND TRUNC(v.datePlan, 'MM') = TRUNC(SYSDATE, 'MM')")
    List<Long> findVPMOfActionPlanCheckList(@Param("auditorId") Long auditorId);

    @Query(value = "SELECT vpm FROM VisitPlanMap vpm WHERE vpm.branchId = :branchId AND vpm.isActionPlan IS NULL " +
            "AND vpm.channelTypeId = :channelTypeId " +
            "AND vpm.id IN (SELECT v.parentId FROM VisitPlanMap v WHERE v.branchId = :branchId " +
            "AND v.isActionPlan = 1) " +
            "ORDER BY vpm.id DESC")
    List<VisitPlanMap> findVisitPlanActionPlanBybrIdAndChannelTypeId(@Param("branchId") Long branchId, @Param("channelTypeId") Long channelTypeId);

    @Query(value = "SELECT v FROM VisitPlanMap v WHERE v.branchId = :branchId " +
            "AND v.pdvId = :channelToId AND v.isActionPlan = 1 AND v.score IS NOT NULL AND TRUNC(v.datePlan, 'MM') = TRUNC(SYSDATE, 'MM') " +
            "ORDER BY v.id DESC")
    List<VisitPlanMap> findActionPlanBybrIdAndChannelId(@Param("branchId") Long branchId, @Param("channelToId") Long channelToId);

    @Query(value = "SELECT count(v) FROM VisitPlanMap v WHERE v.branchId = :branchId " +
            "AND v.pdvId = :channelToId AND v.isActionPlan = 1  AND TRUNC(v.datePlan, 'MM') = TRUNC(SYSDATE, 'MM') " +
            "ORDER BY v.id DESC")
    Integer countActionPlanBybrIdAndChannelId(@Param("branchId") Long branchId, @Param("channelToId") Long channelToId);

    @Query(value = "SELECT v FROM VisitPlanMap v WHERE v.parentId = :parentId AND v.isActionPlan = 1" )
    VisitPlanMap findACByParentId(@Param("parentId") Long parentId);

    @Query("SELECT vpm FROM VisitPlanMap vpm " +
            "WHERE vpm.zonalId = :auditorId AND TRUNC(vpm.datePlan, 'MM') = TRUNC(TO_DATE(:date, 'dd-MM-yyyy'), 'MM') AND vpm.isActionPlan IS NOT NULL AND vpm.status = 11")
    List<VisitPlanMap> findActionPlanCalendar(Long auditorId, String date);
}
