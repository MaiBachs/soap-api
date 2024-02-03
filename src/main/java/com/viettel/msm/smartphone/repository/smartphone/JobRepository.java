package com.viettel.msm.smartphone.repository.smartphone;

import com.slyak.spring.jpa.GenericJpaRepository;
import com.viettel.msm.smartphone.repository.smartphone.entity.Job;

import java.util.List;

public interface JobRepository extends GenericJpaRepository<Job, Long> {
    List<Job> findByCcAudit(Integer ccAudit);
}
