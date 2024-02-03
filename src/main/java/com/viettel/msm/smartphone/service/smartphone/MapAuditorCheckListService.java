package com.viettel.msm.smartphone.service.smartphone;

import com.viettel.msm.smartphone.Constants;
import com.viettel.msm.smartphone.bean.CalendarBean;
import com.viettel.msm.smartphone.repository.sm.VisitPlanMapRepository;
import com.viettel.msm.smartphone.repository.sm.entity.VisitPlanMap;
import com.viettel.msm.smartphone.repository.smartphone.JobRepository;
import com.viettel.msm.smartphone.repository.smartphone.MapAuditorCheckListRepository;
import com.viettel.msm.smartphone.repository.smartphone.ShopRepository;
import com.viettel.msm.smartphone.repository.smartphone.entity.ChannelType;
import com.viettel.msm.smartphone.repository.smartphone.entity.Job;
import com.viettel.msm.smartphone.repository.smartphone.entity.MapAuditorCheckList;
import com.viettel.msm.smartphone.repository.smartphone.entity.Shop;
import com.viettel.msm.smartphone.ws.CalendarDto;
import com.viettel.msm.smartphone.ws.MapAuditorCheckListDto;
import com.viettel.msm.smartphone.ws.MapAuditorCheckListDtos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MapAuditorCheckListService {
    public static final Long In_process = 11L;
    @Autowired
    private MapAuditorCheckListRepository mapAuditorCheckListRepository;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private ChannelTypeService channelTypeService;
    @Autowired
    private PlanService planService;
    @Autowired
    private VisitPlanMapRepository visitPlanMapRepository;
    @Autowired
    private JobRepository jobRepository;

    public List<MapAuditorCheckList> findByAuditorId(Long auditorId){
        return mapAuditorCheckListRepository.findByAuditorId(auditorId);
    }

    public List<MapAuditorCheckListDtos> findChannelCheckList(Long auditorId){
        List<MapAuditorCheckList> mapAuditorCheckLists = mapAuditorCheckListRepository.findByAuditorId(auditorId);

        List<MapAuditorCheckListDto> mapAuditorCheckListDtos = mapAuditorCheckLists.stream().map(macl->{
            MapAuditorCheckListDto mapAuditorCheckListDto = new MapAuditorCheckListDto();
            Shop shop = shopRepository.findById(macl.getShopId()).orElse(null);
            mapAuditorCheckListDto.setId(macl.getId());
            mapAuditorCheckListDto.setShopId(macl.getShopId());
            mapAuditorCheckListDto.setShopCode(shop.getShopCode());
            mapAuditorCheckListDto.setShopName(shop.getName());
            mapAuditorCheckListDto.setChannelTypeId(macl.getChannelTypeId());
            mapAuditorCheckListDto.setBrId(macl.getBrId());
            mapAuditorCheckListDto.setBranchCode(macl.getBrCode());
            return mapAuditorCheckListDto;
        }).collect(Collectors.toList());

        List<ChannelType> channelTypes = channelTypeService.getChannelTypeIdIn(planService.getChannelTypeIdByType(Constants.STATUS_CCAUDIT_IN_PLAN));

        List<MapAuditorCheckListDtos> channelWithTypes = new ArrayList<>();
        channelWithTypes = channelTypes.stream().map(ct->{
            MapAuditorCheckListDtos mapAuditorCheckListDtos1 = new MapAuditorCheckListDtos();
            mapAuditorCheckListDtos1.setChannelTypeId(ct.getChannelTypeId());
            mapAuditorCheckListDtos1.setChannelTypeName(ct.getName());
            List<MapAuditorCheckListDto> mapAuditorCheckListDtos2 = mapAuditorCheckListDtos.stream().filter(macl->ct.getChannelTypeId().equals(macl.getChannelTypeId())).collect(Collectors.toList());
            mapAuditorCheckListDtos1.getMapAuditorCheckListDto().addAll(mapAuditorCheckListDtos2);
            return mapAuditorCheckListDtos1;
        }).collect(Collectors.toList());

        return channelWithTypes;
    }

    public List<CalendarDto> getCalendar(Long auditorId, String date){
        List<Job> jobListEvaluation =  jobRepository.findByCcAudit(1);
        List<CalendarBean> calendarBeans = mapAuditorCheckListRepository.calendar(auditorId, date);
        List<CalendarDto> calendarDtos  = calendarBeans.stream().map(cb->{
            List<VisitPlanMap> visitPlanMaps = visitPlanMapRepository.findByZonalIdAndPdvIdAndDatePlan(auditorId, cb.getShopId(), date);
            CalendarDto calendarDto = new CalendarDto();
            calendarDto.setChannelTypeId(cb.getChannelTypeId());
            calendarDto.setDateEvaluation1(cb.getDateEvaluation1().toString());
            calendarDto.setDateEvaluation2(cb.getDateEvaluation2() != null ? cb.getDateEvaluation2().toString() : null);
            calendarDto.setDateEvaluation3(cb.getDateEvaluation3() != null ? cb.getDateEvaluation3().toString() : null);
            calendarDto.setDateEvaluation4(cb.getDateEvaluation4() != null ? cb.getDateEvaluation4().toString() : null);
//            Date visit1 = null;
//            Date visit2 = null;
//            Date visit3 = null;
//            Date visit4 = null;
//
//            for(VisitPlanMap vpm: visitPlanMaps){
//                if(Objects.equals(vpm.getDatePlan(), cb.getDateEvaluation1())){
//                    visit1 = cb.getDateEvaluation1();
//                }
//                if(Objects.equals(vpm.getDatePlan(), cb.getDateEvaluation2())){
//                    visit2 = cb.getDateEvaluation2();
//                }
//                if(Objects.equals(vpm.getDatePlan(), cb.getDateEvaluation3())){
//                    visit3 = cb.getDateEvaluation3();
//                }
//                if(Objects.equals(vpm.getDatePlan(), cb.getDateEvaluation4())){
//                    visit4 = cb.getDateEvaluation4();
//                }
//            }
            //visit1 == null &&
            if(cb.getDateEvaluation1() != null && cb.getDateEvaluation1().before(new Date())){
                calendarDto.setDateEvaluation1(null);
                calendarDto.setMissEvaluation1(cb.getDateEvaluation1().toString());
            }
            if(cb.getDateEvaluation2() != null && cb.getDateEvaluation2().before(new Date())){
                calendarDto.setDateEvaluation2(null);
                calendarDto.setMissEvaluation2(cb.getDateEvaluation2().toString());
            }
            if(cb.getDateEvaluation3() != null && cb.getDateEvaluation3().before(new Date())){
                calendarDto.setDateEvaluation3(null);
                calendarDto.setMissEvaluation3(cb.getDateEvaluation3().toString());
            }
            if(cb.getDateEvaluation4() != null && cb.getDateEvaluation4().before(new Date())){
                calendarDto.setDateEvaluation4(null);
                calendarDto.setMissEvaluation4(cb.getDateEvaluation1().toString());
            }
            calendarDto.setShopId(cb.getShopId());
            calendarDto.setShopCode(cb.getShopCode());
            calendarDto.setShopName(cb.getShopName());
            calendarDto.setJobName(cb.getJobName());
            calendarDto.setJobCode(cb.getJobCode());
            return calendarDto;
        }).collect(Collectors.toList());
        List<VisitPlanMap> actionPlan = visitPlanMapRepository.findActionPlanCalendar(auditorId, date);
        for(VisitPlanMap vpm: actionPlan){
            CalendarDto calendarDto = new CalendarDto();
            calendarDto.setChannelTypeId(vpm.getChannelTypeId());
            calendarDto.setIsActionPlan(true);
            if(vpm.getDatePlan().after(new Date())){
                calendarDto.setActionPlanDate(String.valueOf(vpm.getDatePlan()));
            }else {
                calendarDto.setMissActionPlanDate(String.valueOf(vpm.getDatePlan()));
            }
            calendarDto.setShopId(vpm.getPdvId());
            Shop shop = shopRepository.findById(calendarDto.getShopId()).orElse(null);
            calendarDto.setShopName(shop.getName());
            calendarDto.setShopCode(shop.getShopCode());
            for(Job j: jobListEvaluation){
                if(j.getJobId().equals(vpm.getJobId())){
                    calendarDto.setJobName(j.getName());
                    calendarDto.setJobCode(j.getCode());
                    break;
                }
            }
            calendarDtos.add(calendarDto);
        }
        return calendarDtos;
    }

    public List<MapAuditorCheckListDtos> findActionPlanCheckList(Long auditorId){
        List<Long> mapAuditorActionPlanCheckLists = visitPlanMapRepository.findShopIdOfActionPlanCheckList(auditorId);
        List<MapAuditorCheckList> mapAuditorCheckLists = mapAuditorCheckListRepository.findByAuditorId(auditorId);
        List<MapAuditorCheckList> mapAuditorCheckListOfActionPlans = new ArrayList<>();
        for(Long shopId: mapAuditorActionPlanCheckLists){
            mapAuditorCheckListOfActionPlans.addAll(mapAuditorCheckLists.stream().filter(m -> m.getShopId().equals(shopId)).collect(Collectors.toList()));
        }
        List<MapAuditorCheckListDto> mapAuditorCheckListDtos = mapAuditorCheckListOfActionPlans.stream().map(v->{
            MapAuditorCheckListDto mapAuditorCheckListDto = new MapAuditorCheckListDto();
            Shop shop = shopRepository.findById((v.getShopId())).orElse(null);
            mapAuditorCheckListDto.setId(v.getId());
            mapAuditorCheckListDto.setShopId(v.getShopId());
            mapAuditorCheckListDto.setShopCode(shop.getShopCode());
            mapAuditorCheckListDto.setShopName(shop.getName());
            mapAuditorCheckListDto.setChannelTypeId(v.getChannelTypeId());
            mapAuditorCheckListDto.setBrId(v.getBrId());
            mapAuditorCheckListDto.setBranchCode(v.getBrCode());
            return mapAuditorCheckListDto;
        }).collect(Collectors.toList());

        List<ChannelType> channelTypes = channelTypeService
                .getChannelTypeIdIn(planService.getChannelTypeIdByType(Constants.STATUS_CCAUDIT_IN_PLAN));


        List<MapAuditorCheckListDtos> channelWithTypes = new ArrayList<>();
        channelWithTypes = channelTypes.stream().map(ct->{
            List<MapAuditorCheckListDto> mapAuditorCheckListDtos2 = mapAuditorCheckListDtos.stream()
                    .filter(macl->ct.getChannelTypeId().equals(macl.getChannelTypeId())).collect(Collectors.toList());
            MapAuditorCheckListDtos mapAuditorCheckListDtos1 = new MapAuditorCheckListDtos();
            mapAuditorCheckListDtos1.setChannelTypeId(ct.getChannelTypeId());
            mapAuditorCheckListDtos1.setChannelTypeName(ct.getName());

            mapAuditorCheckListDtos1.getMapAuditorCheckListDto().addAll(mapAuditorCheckListDtos2);
            return mapAuditorCheckListDtos1;
        }).collect(Collectors.toList());

        return channelWithTypes;
    }
}
