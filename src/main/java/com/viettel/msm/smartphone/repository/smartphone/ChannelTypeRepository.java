package com.viettel.msm.smartphone.repository.smartphone;

import com.slyak.spring.jpa.GenericJpaRepository;
import com.slyak.spring.jpa.TemplateQuery;
import com.viettel.msm.smartphone.repository.smartphone.entity.AppParams;
import com.viettel.msm.smartphone.repository.smartphone.entity.ChannelType;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChannelTypeRepository extends GenericJpaRepository<ChannelType, Long> {
    List<ChannelType> findByChannelTypeIdIn(List<Long> ids);
    @TemplateQuery()
    AppParams findAppParamByTypeAndCode(@Param("type") String type,@Param("code") String code);
    @TemplateQuery()
    List<AppParams> findAppParamsByType(@Param("type") String type);
}
