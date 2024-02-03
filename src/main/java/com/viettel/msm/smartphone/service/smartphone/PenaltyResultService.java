package com.viettel.msm.smartphone.service.smartphone;

import com.viettel.msm.smartphone.repository.smartphone.PenaltyResultDetailRepository;
import com.viettel.msm.smartphone.repository.smartphone.PenaltyResultRepository;
import com.viettel.msm.smartphone.repository.smartphone.entity.PenaltyResult;
import com.viettel.msm.smartphone.repository.smartphone.entity.PenaltyResultDetail;
import com.viettel.msm.smartphone.ws.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PenaltyResultService {
    @Autowired
    private PenaltyResultDetailRepository penaltyResultDetailRepository;
    @Autowired
    private PenaltyResultRepository penaltyResultRepository;

    public TotalPenaltyDto getPenalty(PenaltyRequest request){
        List<PenaltyResult> penaltyResults = penaltyResultRepository.findByAuditorIdAndDatePlan(request.getAuditorId(), request.getDate());
        List<PenaltyResultDetail> penaltyResultDetails = penaltyResultDetailRepository.findAllByAuditorIdInMonth(request.getAuditorId(), request.getDate());
        TotalPenaltyDto totalPenaltyDto = new TotalPenaltyDto();
        totalPenaltyDto.setAuditorId(request.getAuditorId());
        totalPenaltyDto.setAuditorCode(request.getAuditorCode());
        // map branch
        List<BranchPenaltyDto> branchPenaltyDtos = new ArrayList<>();
        for(PenaltyResult pr1 :penaltyResults){
            boolean branchExist = false;
            for(BranchPenaltyDto b: branchPenaltyDtos){
                if(Objects.equals(b.getBranchId(),pr1.getBranchId())){
                    branchExist = true;
                }
            }
            if(branchExist == false){
                BranchPenaltyDto br = new BranchPenaltyDto();
                br.setBranchId(pr1.getBranchId());
                br.setBranchCode(pr1.getBranchCode());
                //map ChannelType
                List<PenaltyResult> prOfBr = penaltyResults.stream().filter(pr->Objects.equals(pr.getBranchId(), br.getBranchId())).collect(Collectors.toList());
                List<ChannelTypePenaltyDto> channelTypePenaltyDtos = new ArrayList<>();
                for(PenaltyResult prBr: prOfBr){
                    boolean channelTypeExist = false;
                    for(ChannelTypePenaltyDto ct: channelTypePenaltyDtos){
                        if(Objects.equals(ct.getChanelTypeId(),prBr.getChannelTypeId())){
                            channelTypeExist = true;
                            break;
                        }
                    }
                    if(channelTypeExist == false){
                        ChannelTypePenaltyDto ct = new ChannelTypePenaltyDto();
                        ct.setChanelTypeId(prBr.getChannelTypeId());
                        ct.setChannelTypeName(prBr.getChannelTypeName());
                        //map shop
                        List<PenaltyResult> prOfCT = penaltyResults.stream().filter(pr->Objects.equals(pr.getChannelTypeId(), prBr.getChannelTypeId())).collect(Collectors.toList());
                        List<ShopPenaltyDto> shopPenaltyDtos = new ArrayList<>();
                        for(PenaltyResult prCT: prOfCT){
                            boolean shopExist = false;
                            for(ShopPenaltyDto sp: shopPenaltyDtos){
                                if(Objects.equals(prCT.getShopId(), sp.getShopId())){
                                    shopExist = true;
                                    break;
                                }
                            }
                            if(shopExist == false){
                                ShopPenaltyDto shopPenaltyDto = new ShopPenaltyDto();
                                shopPenaltyDto.setPenaltyResultId(prCT.getPenaltyResultId());
                                shopPenaltyDto.setShopCode(prCT.getShopCode());
                                shopPenaltyDto.setShopId(prCT.getShopId());
                                shopPenaltyDto.setChannelTypeName(ct.getChannelTypeName());
                                // map evaluation
                                List<PenaltyResult> prOfShop = penaltyResults.stream().filter(pr->Objects.equals(pr.getShopId(), prCT.getShopId())).collect(Collectors.toList());
                                List<EvaluationPenaltyDto> evaluationPenaltyDtos = new ArrayList<>();
                                for(PenaltyResult prS: prOfShop){
                                    for(PenaltyResultDetail prd: penaltyResultDetails){
                                        if(Objects.equals(prd.getPenaltyResultId(), prS.getPenaltyResultId())){
                                            boolean evaluationExist = false;
                                            for(EvaluationPenaltyDto ep: evaluationPenaltyDtos){
                                                if(Objects.equals(ep.getEvaluationId(), prd.getEvaluationId())
                                                        && !Objects.equals(prS.getEvaluationId(), prd.getEvaluationId())){
                                                    evaluationExist = true;
                                                    break;
                                                }
                                            }
                                            if(evaluationExist == false){
                                                EvaluationPenaltyDto evaluationPenaltyDto = new EvaluationPenaltyDto();
                                                evaluationPenaltyDto.setEvaluationId(prd.getEvaluationId());
                                                evaluationPenaltyDto.setEvaluationName(prd.getEvaluationName());

                                                // map item
                                                List<PenaltyResultDetail> prdOfEv = penaltyResultDetails.stream().filter(pd->Objects.equals(pd.getEvaluationId(), prd.getEvaluationId())).collect(Collectors.toList());
                                                List<ItemPenaltyDto> itemPenaltyDtos = new ArrayList<>();
                                                for(PenaltyResultDetail prEv: prdOfEv){
                                                    boolean ItExist = false;
                                                    for(ItemPenaltyDto it: itemPenaltyDtos){
                                                        if(Objects.equals(it.getItemId(), prEv.getItemId())){
                                                            ItExist = true;
                                                            break;
                                                        }
                                                    }
                                                    if(ItExist == false){
                                                        ItemPenaltyDto itemPenaltyDto = new ItemPenaltyDto();
                                                        itemPenaltyDto.setPenaltyResultDetailId(prEv.getPenaltyResultDetailId());
                                                        itemPenaltyDto.setItemId(prEv.getItemId());
                                                        itemPenaltyDto.setItemName(prEv.getItemName());
                                                        itemPenaltyDto.setPenalidad(prEv.getPenalidad());
                                                        itemPenaltyDtos.add(itemPenaltyDto);
                                                    }
                                                }
                                                evaluationPenaltyDto.getItemPenaltyDtos().addAll(itemPenaltyDtos);
                                                evaluationPenaltyDtos.add(evaluationPenaltyDto);
                                            }
                                        }
                                    }
                                }
                                shopPenaltyDto.getEvaluationPenaltyDtos().addAll(evaluationPenaltyDtos);
                                shopPenaltyDtos.add(shopPenaltyDto);
                            }
                        }
                        ct.getShopPenaltyDtos().addAll(shopPenaltyDtos);
                        channelTypePenaltyDtos.add(ct);
                    }
                }
                br.getChannelTypePenaltyDtos().addAll(channelTypePenaltyDtos);
                branchPenaltyDtos.add(br);
            }
        }

        // identify number non conformities and penalty
        Long totalPenalty = 0l;
        for(BranchPenaltyDto b: branchPenaltyDtos){
            Long branchPenalty = 0l;
            for(ChannelTypePenaltyDto ct: b.getChannelTypePenaltyDtos()){
                Long channelTypePenalty = 0l;
                for(ShopPenaltyDto s: ct.getShopPenaltyDtos()){
                    Long shopPenalty = 0l;
                    Long numberComfor = 0l;
                    for(EvaluationPenaltyDto e: s.getEvaluationPenaltyDtos()){
                        numberComfor += e.getItemPenaltyDtos().size();
                        for(ItemPenaltyDto i: e.getItemPenaltyDtos()){
                            if(StringUtils.isNumeric(i.getPenalidad())){
                                shopPenalty += Long.parseLong(i.getPenalidad());
                            }
                        }
                    }
                    s.setNumberNonComFor(numberComfor);
                    s.setShopPenalty(shopPenalty);
                    channelTypePenalty += shopPenalty;
                }
                ct.setChannelPenalty(channelTypePenalty);
                branchPenalty += channelTypePenalty;
            }
            b.setBrPenalty(branchPenalty);
            totalPenalty += branchPenalty;
        }
        totalPenaltyDto.setTotalPenalty(totalPenalty);
        totalPenaltyDto.getBranchPenaltyDtos().addAll(branchPenaltyDtos);
        return totalPenaltyDto;
    }
}
