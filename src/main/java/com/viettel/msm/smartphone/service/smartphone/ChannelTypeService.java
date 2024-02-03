package com.viettel.msm.smartphone.service.smartphone;

import com.viettel.msm.smartphone.repository.smartphone.ChannelTypeRepository;
import com.viettel.msm.smartphone.repository.smartphone.entity.ChannelType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ChannelTypeService {
    @Autowired
    private ChannelTypeRepository channelTypeRepository;

    public List<ChannelType> getChannelTypeIdIn(List<Long> ids) {
        return channelTypeRepository.findByChannelTypeIdIn(ids);
    }

}
