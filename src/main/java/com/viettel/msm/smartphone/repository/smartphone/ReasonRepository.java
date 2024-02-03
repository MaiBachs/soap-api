package com.viettel.msm.smartphone.repository.smartphone;

import com.slyak.spring.jpa.GenericJpaRepository;
import com.viettel.msm.smartphone.repository.smartphone.entity.Reason;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ReasonRepository extends GenericJpaRepository<Reason, Long> {
    Reason findByName(String name);

    @Query(value = "SELECT r FROM ItemConfigReason icr INNER JOIN Reason r ON icr.reasonId = r.reasonId WHERE icr.itemConfigId = :itemConfigId AND icr.status = 1")
    List<Reason> getReasonDtoByItemConfig(@Param("itemConfigId") Long itemConfigId);
}
