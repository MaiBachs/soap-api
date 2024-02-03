package com.viettel.msm.smartphone.service.smartphone;

import com.viettel.msm.smartphone.repository.smartphone.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanService {
    @Autowired
    private PlanRepository planRepository;

    public List<Long> getChannelTypeIdByType(Long ccAudit) {
        return planRepository.findByCcAudit(ccAudit);
    }
}
