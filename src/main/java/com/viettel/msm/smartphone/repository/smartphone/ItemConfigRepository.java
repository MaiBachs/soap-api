package com.viettel.msm.smartphone.repository.smartphone;

import com.slyak.spring.jpa.GenericJpaRepository;
import com.viettel.msm.smartphone.bean.ItemConfigBean;
import com.viettel.msm.smartphone.repository.smartphone.entity.ItemConfig;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemConfigRepository extends GenericJpaRepository<ItemConfig, Long> {
    @Query("SELECT new com.viettel.msm.smartphone.bean.ItemConfigBean(ic.id, it.name,ic.jobId ,ic.percent, ic.ok, ic.nok, ic.na, ic.validation, ic.url, ic.status, ic.evaluationId, e.name, ic.groupId, g.name) FROM ItemConfig ic INNER JOIN Job e ON e.jobId = ic.evaluationId INNER JOIN Job g ON g.jobId = ic.groupId INNER JOIN Job it ON it.jobId = ic.jobId WHERE ic.status = 1 AND ic.evaluationId = :evaluationId AND ic.channelTypeId = :channelTypeId")
    List<ItemConfigBean> findItemConfigByEvaluationIdAndChannelTypeId(@Param("evaluationId") Long evaluationId, @Param("channelTypeId") Long channelTypeId);

    @Query("SELECT COUNT(ic) FROM ItemConfig ic INNER JOIN Job e ON e.jobId = ic.evaluationId INNER JOIN Job g ON g.jobId = ic.groupId INNER JOIN Job it ON it.jobId = ic.jobId WHERE ic.status = 1 AND ic.evaluationId = :evaluationId AND ic.channelTypeId = :channelTypeId")
    long countItemConfigByEvaluationIdAndChannelTypeId(@Param("evaluationId") Long evaluationId, @Param("channelTypeId") Long channelTypeId);

    @Query("SELECT new com.viettel.msm.smartphone.bean.ItemConfigBean(ic.id, it.name,ic.jobId ,ic.percent, " +
            "ic.ok, ic.nok, ic.na, ic.validation, ic.url, ic.status, ic.evaluationId, e.name, ic.groupId, g.name, pr.visitPlanId, pr.planResultId) " +
            "FROM ItemConfig ic " +
            "INNER JOIN Job e ON e.jobId = ic.evaluationId " +
            "INNER JOIN Job g ON g.jobId = ic.groupId " +
            "INNER JOIN Job it ON it.jobId = ic.jobId " +
            "INNER JOIN PlanResult pr ON pr.itemConfigId = ic.id " +
            "WHERE ic.status = 1 AND ic.evaluationId = :evaluationId AND ic.channelTypeId = :channelTypeId " +
            "AND pr.result = 'NOK' " +
            "AND pr.visitPlanId IN (:visitPlanIds) ")
    List<ItemConfigBean> findItemConfigActionPlanByEvaluationIdAndChannelTypeId(@Param("evaluationId") Long evaluationId,
                                                                                @Param("channelTypeId") Long channelTypeId,
                                                                                @Param("visitPlanIds") List<Long> visitPlanIds);
}
