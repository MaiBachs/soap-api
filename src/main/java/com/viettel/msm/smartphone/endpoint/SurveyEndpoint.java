package com.viettel.msm.smartphone.endpoint;

import com.viettel.msm.smartphone.Constants;
import com.viettel.msm.smartphone.MessageKey;
import com.viettel.msm.smartphone.repository.sm.entity.MapChannelSurveyStaff;
import com.viettel.msm.smartphone.repository.smartphone.entity.Token;
import com.viettel.msm.smartphone.service.SurveyService;
import com.viettel.msm.smartphone.service.smartphone.TokenService;
import com.viettel.msm.smartphone.ws.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Slf4j
@Endpoint
public class SurveyEndpoint extends BaseEndpoint {
    @Autowired
    private SurveyService surveyService;
    @Autowired
    private MessageSource messageSource;

    @Autowired
    public SurveyEndpoint(TokenService tokenService) {
        super(tokenService);
    }

    @PayloadRoot(namespace = Constants.URI_BASE, localPart = "getListSurveysRequest")
    @ResponsePayload
    public GetListSurveysResponse getListSurveys(@RequestPayload GetListSurveysRequest request) {
        GetListSurveysResponse response = new GetListSurveysResponse();
        SurveyReturn returnType = new SurveyReturn();

        Locale locale = new Locale(request.getLocation());
        Token validateResult = authenticateValidate(request);
        if (validateResult == null) {
            returnType.setErrorCode(Constants.TOKEN_INVALID);
            returnType.setDescription(messageSource.getMessage(MessageKey.COMMON_TOKEN_INVALID, null, locale));
            response.setReturn(returnType);
            return response;
        }
        List<MapChannelSurveyStaff> list = surveyService.getListSurveys(validateResult.getStaffId());
        List<SurveyDto> surveys = list.stream().map(s -> {
            SurveyDto surveyType = new SurveyDto();
            BeanUtils.copyProperties(s, surveyType);
            return surveyType;
        }).collect(Collectors.toList());
        returnType.getSurveys().addAll(surveys);
        response.setReturn(returnType);
        return response;
    }

    @PayloadRoot(namespace = Constants.URI_BASE, localPart = "updateSurveysRequest")
    @ResponsePayload
    public UpdateSurveysResponse updateSurveys(@RequestPayload UpdateSurveysRequest request) {
        UpdateSurveysResponse response = new UpdateSurveysResponse();
        SurveyUpdateReturn returnType = new SurveyUpdateReturn();

        Locale locale = new Locale(request.getLocation());
        Token validateResult = authenticateValidate(request);
        if (validateResult == null) {
            returnType.setErrorCode(Constants.TOKEN_INVALID);
            returnType.setDescription(messageSource.getMessage(MessageKey.COMMON_TOKEN_INVALID, null, locale));
            response.setReturn(returnType);
            return response;
        }
        returnType = surveyService.updateSurvey(request);
        response.setReturn(returnType);
        return response;
    }
}