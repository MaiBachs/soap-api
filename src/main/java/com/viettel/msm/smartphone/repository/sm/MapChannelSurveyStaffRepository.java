package com.viettel.msm.smartphone.repository.sm;

import com.viettel.msm.smartphone.repository.sm.entity.MapChannelSurveyStaff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Date;
import java.util.List;

/**
 * @author edward
 */
public interface MapChannelSurveyStaffRepository extends JpaRepository<MapChannelSurveyStaff, Long>, JpaSpecificationExecutor<MapChannelSurveyStaff> {
    List<MapChannelSurveyStaff> findByStatusAndChannelIdAndStartVoteDateIsLessThanEqualOrderByCreatedDateDesc(Integer status, Long channelId, Date startVoteDate);
}
