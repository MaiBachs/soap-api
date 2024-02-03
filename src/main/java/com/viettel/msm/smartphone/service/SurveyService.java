package com.viettel.msm.smartphone.service;

import com.viettel.msm.smartphone.Constants;
import com.viettel.msm.smartphone.MessageKey;
import com.viettel.msm.smartphone.repository.sm.MapChannelSurveyStaffRepository;
import com.viettel.msm.smartphone.repository.sm.entity.MapChannelSurveyStaff;
import com.viettel.msm.smartphone.repository.smartphone.TokenRepository;
import com.viettel.msm.smartphone.ws.SurveyUpdateReturn;
import com.viettel.msm.smartphone.ws.UpdateSurveysRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static com.viettel.msm.smartphone.Constants.STATUS_DONE;

@Service
public class SurveyService {

    @Autowired
    private MapChannelSurveyStaffRepository surveyStaffRepository;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private MessageSource messageSource;

    public List<MapChannelSurveyStaff> getListSurveys(Long staffId) {
        try {
            Date sysDate = tokenRepository.getSysdate();
            return surveyStaffRepository.findByStatusAndChannelIdAndStartVoteDateIsLessThanEqualOrderByCreatedDateDesc(Constants.STATUS_ACTIVE, staffId, sysDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public SurveyUpdateReturn updateSurvey(UpdateSurveysRequest request) {
        SurveyUpdateReturn updateReturn = new SurveyUpdateReturn();
        Locale locale = new Locale(request.getLocation());
        if (StringUtils.isEmpty(request.getResultSurvey())) {
            updateReturn.setErrorCode(Constants.ERROR_CODE);
            updateReturn.setDescription(messageSource.getMessage(MessageKey.SURVEY_RESULT_IS_REQUIRED, null, locale));
        } else {
            Optional<MapChannelSurveyStaff> surveyOpt = surveyStaffRepository.findById(request.getSurveyId());
            if (!surveyOpt.isPresent()) {
                updateReturn.setErrorCode(Constants.ERROR_CODE);
                updateReturn.setDescription(messageSource.getMessage(MessageKey.SURVEY_NOT_FOUND, new Object[]{request.getSurveyId()}, locale));
            } else {
                MapChannelSurveyStaff surveyStaff = surveyOpt.get();
                Date sysDate = tokenRepository.getSysdate();
                surveyStaff.setSurveyDate(sysDate);
                surveyStaff.setResultSurvey(request.getResultSurvey());
                surveyStaff.setSurveyComment(request.getSurveyComment());
                surveyStaff.setStatus(STATUS_DONE);
                surveyStaffRepository.save(surveyStaff);
                updateReturn.setErrorCode(Constants.SUCCESS_CODE);
                updateReturn.setDescription(messageSource.getMessage(MessageKey.SURVEY_SAVE_SUCCESS, new Object[]{request.getSurveyId()}, locale));
            }
        }
        return updateReturn;
    }
}
