package com.viettel.msm.smartphone.repository.smartphone;

import com.slyak.spring.jpa.GenericJpaRepository;
import com.viettel.msm.smartphone.bean.CalendarBean;
import com.viettel.msm.smartphone.repository.smartphone.entity.MapAuditorCheckList;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface MapAuditorCheckListRepository extends GenericJpaRepository<MapAuditorCheckList, Long> {
    @Query("SELECT DISTINCT macl FROM MapAuditorCheckList macl INNER JOIN MapChannelTypeCheckList mctcl ON macl.brId = mctcl.brId AND macl.channelTypeId = mctcl.channelTypeId AND TRUNC(mctcl.dateEvaluation1, 'MM') = TRUNC(SYSDATE, 'MM') WHERE macl.auditorId = :auditorId")
    List<MapAuditorCheckList> findByAuditorId(Long auditorId);

    @Query(value = "SELECT new com.viettel.msm.smartphone.bean.CalendarBean(macl.channelTypeId" +
            ", mctcl.dateEvaluation1" +
            ", mctcl.dateEvaluation2" +
            ", mctcl.dateEvaluation3" +
            ", mctcl.dateEvaluation4" +
            ", shop.shopId" +
            ", shop.shopCode, shop.name, job.name, job.code)" +
            "FROM MapAuditorCheckList  macl\n" +
            "    INNER JOIN MapChannelTypeCheckList mctcl\n" +
            "        ON macl.brId = mctcl.brId AND macl.channelTypeId = mctcl.channelTypeId\n" +
            "               AND TRUNC(mctcl.dateEvaluation1, 'MM') = TRUNC(TO_DATE(:date, 'dd-MM-yyyy'), 'MM')\n" +
            "    INNER JOIN Shop shop ON macl.shopId = shop.shopId\n" +
            "    INNER JOIN Job job ON mctcl.jobId = job.jobId\n" +
            "WHERE macl.auditorId = :auditorId")
    List<CalendarBean> calendar(Long auditorId, String date);
}
