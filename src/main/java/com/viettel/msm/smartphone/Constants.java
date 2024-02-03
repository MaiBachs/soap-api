
package com.viettel.msm.smartphone;

public interface Constants {
    String URI_BASE = "http://www.viettel.com/msm/smartphone/ws";
    String URI_TUTORIAL_SERVICE = URI_BASE + "/TutorialService";
    String URI_COUNTRY_SERVICE = URI_BASE + "/country";
    String URI_SURVEY_SERVICE = URI_BASE + "/survey";

    String SUCCESS_CODE = "0";
    String ERROR_CODE = "1";
    String CHANNEL_CHECK_LIST = "CHANNEL_CHECK_LIST";
    String TOKEN_INVALID = "TOKEN_INVALID";
    String COMMON_TOKEN_INVALID = "token.invalid";               //   Constants.COMMON_TOKEN_INVALID

    String OBJECT_TYPE_SHOP = "1";
    String OBJECT_TYPE_STAFF = "2";
    String LOCATION_VALID = "Ubicacion correcta";
    String LOCATION_INVALID = "Ubicacion incorrecta";

    String MINIMOS_CODE = "REQUERIMENTOS_MINIMOS";
    String IMAGEN_CODE = "IMAGEN";
    String CALIDAD_CODE = "CALIDAD";

    String ACTION_PLAN_STATUS = "ACTION_PLAN_STATUS";
    int STATUS_ACTIVE = 1;
    int STATUS_INACTIVE = 0;
    int STATUS_DONE = 2;
    Long STATUS_CCAUDIT_IN_PLAN = 1l;
    Long ACTION_PLAN_STATUS_NEW = 10l;
    Long ACTION_PLAN_STATUS_IN_PROCESS = 11l;
    Long ACTION_PLAN_STATUS_REQUEST_UPDATE = 14l;
    Long ACTION_PLAN_STATUS_DONE = 12l;

    String FTP_CONNECT_PROBLEM = "ftp connection problem";
    String TRUE = "true";
    Long EVALUATION_STATUS_NEED_ATION_PLAN = 2l;
    Long EVALUATION_STATUS_DONE = 1l;
    Long EVALUATION_STATUS_INPROCESS = 3l;
}
