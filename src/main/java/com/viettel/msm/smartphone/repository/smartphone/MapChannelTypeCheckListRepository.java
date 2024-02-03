package com.viettel.msm.smartphone.repository.smartphone;


import com.slyak.spring.jpa.GenericJpaRepository;
import com.viettel.msm.smartphone.bean.CheckListBean;
import com.viettel.msm.smartphone.repository.smartphone.entity.MapChannelTypeCheckList;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MapChannelTypeCheckListRepository extends GenericJpaRepository<MapChannelTypeCheckList, Long> {
    @Query(value = "SELECT new com.viettel.msm.smartphone.bean.CheckListBean(mctcl.id,mctcl.brId, mctcl.brCode, mctcl.channelTypeId, mctcl.jobId, mctcl.quantityPerMonth,mctcl.approvalScore, mctcl.dateEvaluation1, mctcl.dateEvaluation2, mctcl.dateEvaluation3, mctcl.dateEvaluation4 , j.name, j.code) " +
            "FROM MapChannelTypeCheckList mctcl INNER JOIN Job j ON mctcl.jobId = j.jobId WHERE mctcl.brId = :brId AND mctcl.channelTypeId = :channelTypeId AND TRUNC(mctcl.dateEvaluation1, 'MM') = TRUNC(SYSDATE, 'MM')")
    List<CheckListBean> getCheckListByBrIdAndChannelAndMonth(@Param("brId") Long brId, @Param("channelTypeId") Long channelTypeId);

    @Query(value = "SELECT new com.viettel.msm.smartphone.bean.CheckListBean(mctcl.id,mctcl.brId, mctcl.brCode, mctcl.channelTypeId, mctcl.jobId,mctcl.approvalScore, mctcl.dateEvaluation1, mctcl.dateEvaluation2, mctcl.dateEvaluation3, mctcl.dateEvaluation4 , j.name, j.code) " +
            "FROM MapChannelTypeCheckList mctcl INNER JOIN Job j ON mctcl.jobId = j.jobId WHERE mctcl.brId = :brId AND mctcl.channelTypeId = :channelTypeId AND TRUNC(mctcl.dateEvaluation1, 'MM') = TRUNC(SYSDATE, 'MM')")
    List<CheckListBean> getCheckListAPByBrIdAndChannelAndMonth(@Param("brId") Long brId, @Param("channelTypeId") Long channelTypeId);
}
