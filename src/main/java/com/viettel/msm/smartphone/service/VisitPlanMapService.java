package com.viettel.msm.smartphone.service;

import com.viettel.msm.smartphone.Constants;
import com.viettel.msm.smartphone.bean.CalendarBean;
import com.viettel.msm.smartphone.bean.CheckListBean;
import com.viettel.msm.smartphone.bean.FTPProperties;
import com.viettel.msm.smartphone.bean.StaffBean;
import com.viettel.msm.smartphone.repository.sm.VisitPlanMapRepository;
import com.viettel.msm.smartphone.repository.sm.entity.VisitPlanMap;
import com.viettel.msm.smartphone.repository.smartphone.ChannelTypeRepository;
import com.viettel.msm.smartphone.repository.smartphone.JobRepository;
import com.viettel.msm.smartphone.repository.smartphone.PlanResultRepository;
import com.viettel.msm.smartphone.repository.smartphone.ShopRepository;
import com.viettel.msm.smartphone.repository.smartphone.StaffRepository;
import com.viettel.msm.smartphone.repository.smartphone.entity.*;
import com.viettel.msm.smartphone.service.smartphone.AppParamsService;
import com.viettel.msm.smartphone.utils.DateUtil;
import com.viettel.msm.smartphone.utils.FTPFileWriter;
import com.viettel.msm.smartphone.utils.FTPHelper;
import com.viettel.msm.smartphone.utils.FileUtils;
import com.viettel.msm.smartphone.ws.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPSClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.xml.datatype.DatatypeFactory;
import java.awt.geom.Point2D;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VisitPlanMapService {
    private static final Logger logger = LoggerFactory.getLogger(VisitPlanMapService.class);
    public static final String YYYY_M_MDD = "yyyyMMdd";
    public static final long STATUS_INPROCESS = 11L;
    @Autowired
    private VisitPlanMapRepository visitPlanMapRepository;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private ChannelTypeRepository channelTypeRepository;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private PlanResultRepository planResultRepository;
    @Autowired
    private AppParamsService appParamsService;
    @Value("${TEMPLATE_PATH}")
    private String templateFolder;
    @Value("${FILE_TEMP_UPLOAD_PATH}")
    private String fileNameFullFolder;
    private String ftpWorkingDir;
    @Value("${ftp.host}")
    private String ftpHost;
    @Value("${ftp.port}")
    private String ftpPort;
    @Value("${ftp.user}")
    private String ftpUser;
    @Value("${ftp.pass}")
    private String ftpPass;
    @Value("${ftp.is_ssl}")
    private String ftpIsSsl;

    public List<VisitPlanMap> findVisitPlanBybrIdAndChannelId(Long branchId, Long channelToId) {
        return visitPlanMapRepository.findVisitPlanBybrIdAndChannelId(branchId, channelToId);
    }

    public VisitPlanMap saveAuditCheckList(VisitPlanMap visitPlanMap, GetCheckListResultRequest request, ResultCheckListDto resultCheckListDto) {
        Shop bcShop = null;
        Long idCheckStaff = null;
        List<Staff> staffs = null;
        if (visitPlanMap.getPdvChannelObjectType().equals(Constants.OBJECT_TYPE_SHOP)) {
            Shop shop = shopRepository.findById(visitPlanMap.getPdvId()).orElse(null);
            bcShop = shopRepository.findById(shop.getParentShopId()).orElse(null);
        } else if (visitPlanMap.getPdvChannelObjectType().equals(Constants.OBJECT_TYPE_STAFF)) {
            Staff staff = staffRepository.findById(request.getStaffId()).orElse(null);
            bcShop = shopRepository.findById(staff.getShopId()).orElse(null);
            String datePlan = String.valueOf(request.getDatePlan());
            VisitPlanMap calidad = null;
            calidad = visitPlanMapRepository.finVisitPlanStaffOfCalidad(request.getBranchId(), request.getPdvId(), datePlan);
            staffs = staffRepository.getStaffByShopId(request.getPdvId());
            if (calidad == null) {
                VisitPlanMap visitPlanMapStaff = new VisitPlanMap();
                BeanUtils.copyProperties(visitPlanMap, visitPlanMapStaff);
                visitPlanMapStaff.setScore(100f / (float) staffs.size());
                visitPlanMapStaff.setPdvChannelObjectType(Constants.OBJECT_TYPE_SHOP);
                visitPlanMapStaff.setBcCode(bcShop.getShopCode());
                visitPlanMapStaff.setBcId(bcShop.getShopId());
                visitPlanMapStaff.setCreatedDate(new Date());
                visitPlanMapStaff.setUpdatedDate(new Date());
                if (visitPlanMapStaff.getScore() < request.getApprovalScore()) {
                    visitPlanMapStaff.setStatus(Constants.EVALUATION_STATUS_INPROCESS);
                } else {
                    visitPlanMapStaff.setStatus(Constants.EVALUATION_STATUS_DONE);
                }
                visitPlanMapStaff = visitPlanMapRepository.save(visitPlanMapStaff);
                idCheckStaff = visitPlanMapStaff.getId();
                visitPlanMap.setParentId(visitPlanMapStaff.getId());
            } else {
                idCheckStaff = calidad.getId();
                calidad.setScore(calidad.getScore() + 100f / (float) staffs.size());
                if (calidad.getScore() >= request.getApprovalScore()) {
                    calidad.setStatus(Constants.EVALUATION_STATUS_DONE);
                }
                visitPlanMapRepository.save(calidad);
                visitPlanMap.setParentId(calidad.getId());
            }
            visitPlanMap.setPdvId(request.getStaffId());
            visitPlanMap.setPdvCode(request.getStaffCode());
        }
        visitPlanMap.setBcCode(bcShop.getShopCode());
        visitPlanMap.setBcId(bcShop.getShopId());
        visitPlanMap.setCreatedDate(new Date());
        visitPlanMap.setUpdatedDate(new Date());
        visitPlanMap.setStatus(Constants.EVALUATION_STATUS_DONE);
        VisitPlanMap visitPlanMap1 = visitPlanMapRepository.save(visitPlanMap);
        if (idCheckStaff != null && staffs != null) {
            // check staff evaluated
            List<StaffBean> staffBeans = finVisitPlanOfCalidadByParentId(idCheckStaff);
            List<StaffDto> staffDtos = staffBeans.stream().map(s -> {
                StaffDto staffDto = new StaffDto();
                staffDto.setStaffId(s.getStaffId());
                staffDto.setStaffCode(s.getStaffCode());
                return staffDto;
            }).collect(Collectors.toList());
            resultCheckListDto.getStaffList().addAll(staffDtos);
            List<Staff> staff1 = staffRepository.getStaffByShopId(request.getPdvId());
            if (staffBeans.size() < staffs.size()) {
                resultCheckListDto.setCheckListFull(false);
            } else {
                resultCheckListDto.setCheckListFull(true);
            }
        }
        return visitPlanMap1;
    }

    public String findNameById(VisitPlanMap visitPlanMap) {
        String channelName = null;
        if (visitPlanMap.getPdvChannelObjectType().equals(Constants.OBJECT_TYPE_SHOP)) {
            Shop shop = shopRepository.findById(visitPlanMap.getPdvId()).orElse(null);
            if (shop != null) {
                channelName = shop.getName();
            }
        } else if (visitPlanMap.getPdvChannelObjectType().equals(Constants.OBJECT_TYPE_STAFF)) {
            Staff staff = staffRepository.findById(visitPlanMap.getPdvId()).orElse(null);
            if (staff != null) {
                channelName = staff.getName();
            }
        }
        return channelName;
    }

    public ResultCheckListDto rejectEvaluation(VisitPlanMap visitPlanMap, GetCheckListResultRequest request) {
        ResultCheckListDto resultCheckListDto = new ResultCheckListDto();
        resultCheckListDto.setChannelFromId(visitPlanMap.getZonalId());
        resultCheckListDto.setChannelFromCode(visitPlanMap.getZonalCode());
        try {
            resultCheckListDto.setEvaluationDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(DateUtil.dbUpdateDateTime2String(visitPlanMap.getVisitTime()).split(" ")[0]));
        } catch (Exception e) {
            e.printStackTrace();
        }
        resultCheckListDto.setChannelCode(visitPlanMap.getPdvCode());
        resultCheckListDto.setChannelName(findNameById(visitPlanMap));
        resultCheckListDto.setQuantityPerMonth(request.getQuantityPerMonth());
        resultCheckListDto.setCheckListResultCommnet(request.getCheckListResultCommnet());
        boolean check = true;
        if (!mapLocation(visitPlanMap.getPdvId(), request.getXReject(), request.getYReject(), request.getChannelTypeId())) {
            check = false;
        }
        resultCheckListDto.setValid(check ? true : false);
        return resultCheckListDto;
    }

    public ResultCheckListDto makeResultCheckListDto(VisitPlanMap visitPlanMap, GetCheckListResultRequest request) {
        ResultCheckListDto resultCheckListDto = new ResultCheckListDto();
        Shop br = shopRepository.findById(request.getBranchId()).orElse(null);
        resultCheckListDto.setBranchName(br != null ? br.getName() : null);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        resultCheckListDto.setVisitTime(simpleDateFormat.format(new Date()));
        resultCheckListDto.setBranchCode(request.getBranchCode());
        resultCheckListDto.setChannelFromId(visitPlanMap.getZonalId());
        resultCheckListDto.setChannelFromCode(visitPlanMap.getZonalCode());
        try {
            resultCheckListDto.setEvaluationDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(DateUtil.dbUpdateDateTime2String(visitPlanMap.getVisitTime()).split(" ")[0]));
        } catch (Exception e) {
            e.printStackTrace();
        }
        resultCheckListDto.setChannelCode(visitPlanMap.getPdvCode());
        resultCheckListDto.setChannelName(findNameById(visitPlanMap));
        resultCheckListDto.setQuantityPerMonth(request.getQuantityPerMonth());
        int itemOk = 0;
        int totalItem = 0;
        float score = 0;
        float totalScore = 100f;
        List<CheckListItemDto> checkListItem = request.getCheckListItem();
        boolean check = true;
        for (CheckListItemDto checkListItemDto : checkListItem) {
            // Tinh item
            if (checkListItemDto.getResult().equalsIgnoreCase("OK")) {
                itemOk += 1;
                totalItem += 1;
            } else if (checkListItemDto.getResult().equalsIgnoreCase("NOK")) {
                totalItem += 1;
            } else if (checkListItemDto.getResult().equalsIgnoreCase("NA")) {
                totalScore -= checkListItemDto.getPercent();
            }
            // check location
            if (!mapLocation(visitPlanMap.getPdvId(), checkListItemDto.getX(), checkListItemDto.getY(), request.getChannelTypeId())) {
                check = false;
            }
            // Tinh score
            if (checkListItemDto.getResult().equalsIgnoreCase("OK")) {
                score += checkListItemDto.getPercent();
            }
        }
        resultCheckListDto.setLocation(check ? Constants.LOCATION_VALID : Constants.LOCATION_INVALID);
        resultCheckListDto.setValid(check ? true : false);
        if (totalScore == 0f) {
            resultCheckListDto.setScore(100f);
        } else {
            resultCheckListDto.setScore(score * 100f / totalScore);
        }
        resultCheckListDto.setItemOk(itemOk);
        resultCheckListDto.setTotalItem(totalItem);
        resultCheckListDto.setActionPlan(false);
        // set quantity
        if (resultCheckListDto.isValid()) {
            if (request.getJobCode().equalsIgnoreCase(Constants.CALIDAD_CODE)) {
                long staffInShop = staffRepository.getCountStaffOfShop(request.getPdvId());
                VisitPlanMap calidad = visitPlanMapRepository.finVisitPlanStaffOfCalidad(request.getBranchId(), request.getPdvId(), request.getDatePlan().toString());
                List<StaffBean> staffChecklisted = visitPlanMapRepository.finVisitPlanOfCalidadByParentId(calidad.getParentId());
                if (staffChecklisted.size() == staffInShop - 1) {
                    resultCheckListDto.setQuantity(request.getQuantity());
                } else {
                    resultCheckListDto.setQuantity(request.getQuantity() - 1);
                }
            } else {
                if (resultCheckListDto.getScore() < request.getApprovalScore() && request.getJobCode().equalsIgnoreCase(Constants.MINIMOS_CODE)) {
                    resultCheckListDto.setActionPlan(true);
                }
                if (resultCheckListDto.getScore() < request.getApprovalScore()
                        && request.getJobCode().equalsIgnoreCase(Constants.IMAGEN_CODE)
                        && request.getQuantity() == request.getQuantityPerMonth()) {
                    resultCheckListDto.setActionPlan(true);
                }
                resultCheckListDto.setQuantity(request.getQuantity());
            }
        } else {
            resultCheckListDto.setQuantity(request.getQuantity() - 1);
        }

        return resultCheckListDto;
    }

    public boolean mapLocation(Long shopId, Double xc, Double yc, Long channelTypeId) {
        Shop shop = shopRepository.findById(shopId).orElse(null);
        Double x = shop.getX();
        Double y = shop.getY();
        // bỏ qua trường hợp null với database test
        if (x == null || y == null) {
            return false;
        }
        Point2D point = new Point2D.Double(xc, yc);
        Point2D pointTarget = new Point2D.Double(x, y);
        AppParams maxDistance = channelTypeRepository.findAppParamByTypeAndCode("LOCATION_DISTANCE", channelTypeId.toString());
        double distance = 1000000;
        try {
            distance = point.distance(pointTarget);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return distance <= Double.parseDouble(maxDistance.getValue());
    }

    public VisitPlanMap createActionPlan(VisitPlanMap visitPlanResult) {
        VisitPlanMap actionPlan = new VisitPlanMap();
        actionPlan.setBranchId(visitPlanResult.getBranchId());
        actionPlan.setBranchCode(visitPlanResult.getBranchCode());
        actionPlan.setBcId(visitPlanResult.getBcId());
        actionPlan.setBcCode(visitPlanResult.getBcCode());
        actionPlan.setPdvChannelObjectType(visitPlanResult.getPdvChannelObjectType());
        actionPlan.setZonalId(visitPlanResult.getZonalId());
        actionPlan.setZonalCode(visitPlanResult.getZonalCode());
        actionPlan.setPdvId(visitPlanResult.getPdvId());
        actionPlan.setPdvCode(visitPlanResult.getPdvCode());
        actionPlan.setCreatedDate(new Date());
        actionPlan.setUpdatedDate(new Date());
        actionPlan.setStatus(Constants.ACTION_PLAN_STATUS_NEW);
        actionPlan.setIsDetail(1l);
        actionPlan.setParentId(visitPlanResult.getId());
        actionPlan.setChannelTypeId(visitPlanResult.getChannelTypeId());
        actionPlan.setJobId(visitPlanResult.getJobId());
        actionPlan.setIsActionPlan(1);
        try {
            visitPlanResult.setStatus(Constants.EVALUATION_STATUS_NEED_ATION_PLAN);
            visitPlanMapRepository.save(visitPlanResult);
            return visitPlanMapRepository.save(actionPlan);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<VisitPlanMap> findActionPlan(SearchActionPlanRequest request) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String fromDateFm = null;
        String toDateFm = null;
        if (request.getFromDate() != null) {
            fromDateFm = sdf.format(request.getFromDate().toGregorianCalendar().getTime());
        }
        if (request.getToDate() != null) {
            toDateFm = sdf.format(request.getToDate().toGregorianCalendar().getTime());
        }
        List<VisitPlanMap> result = visitPlanMapRepository.findActionPlan(request.getAuditorId()
                , request.getChannelTypeId() == 0l ? null : request.getChannelTypeId()
                , request.getChannelId() == 0l ? null : request.getChannelId()
                , request.getStatusCode() == 0l ? null : new Long(request.getStatusCode())
                , fromDateFm
                , toDateFm);

        List<Job> jobListEvaluation = jobRepository.findByCcAudit(1);
        List<AppParams> appParams = appParamsService.findByType(Constants.ACTION_PLAN_STATUS);
        List<VisitPlanMap> result1 = result.stream().map(vpm -> {
            Optional<Job> job = jobListEvaluation.stream().filter(j -> j.getJobId().equals(vpm.getJobId())).findFirst();
            Optional<AppParams> appParams1 = appParams.stream().filter(s -> s.getCode().equals(vpm.getStatus().toString())).findFirst();
            if (job.isPresent()) {
                vpm.setJobName(job.get().getName());
            }
            if (appParams1.isPresent()) {
                vpm.setStatusName(appParams1.get().getValue());
            }
            return vpm;
        }).collect(Collectors.toList());

        return result1;
    }

    public VisitPlanMap save(VisitPlanMap visitPlanMap) {
        return visitPlanMapRepository.save(visitPlanMap);
    }

    public List<PlanResult> setPlanResults(VisitPlanMap visitPlanResult, GetCheckListResultRequest request) {
        List<PlanResult> planResults = request.getCheckListItem().stream().map(it -> {
            PlanResult planResult = new PlanResult();
            BeanUtils.copyProperties(it, planResult);
            if (planResult.getReasonId() == 0l) {
                planResult.setReasonId(null);
            } else {
                planResult.setReasonId(it.getReasonId());
            }
            planResult.setItemConfigId(it.getId());
            planResult.setStatus(1l);
            planResult.setCreatedDate(new Date());
            planResult.setLastUpdate(new Date());
            planResult.setLatitude(it.getX());
            planResult.setLongitude(it.getY());
            planResult.setVisitPlanId(visitPlanResult.getId());
            planResult.setFilePath(it.getFilePath());
            return planResult;
        }).collect(Collectors.toList());
        return planResults;
    }

    public List<PlanResult> setActionPlanResults(VisitPlanMap visitPlanResult, GetCheckListACResultRequest request) {
        List<PlanResult> planResults = request.getCheckListItem().stream().map(it -> {
            PlanResult planResult = new PlanResult();
            BeanUtils.copyProperties(it, planResult);
            if (planResult.getReasonId() == 0l) {
                planResult.setReasonId(null);
            } else {
                planResult.setReasonId(it.getReasonId());
            }
            planResult.setItemConfigId(it.getId());
            planResult.setStatus(1l);
            planResult.setCreatedDate(new Date());
            planResult.setLastUpdate(new Date());
            planResult.setLatitude(it.getX());
            planResult.setLongitude(it.getY());
            planResult.setVisitPlanId(visitPlanResult.getId());
            planResult.setFilePath(it.getFilePath());
            return planResult;
        }).collect(Collectors.toList());
        return planResults;
    }

    public void delete(VisitPlanMap visitPlanMap) {
        visitPlanMapRepository.delete(visitPlanMap);
    }

    public VisitPlanMap findById(Long id) {
        return visitPlanMapRepository.findById(id).orElse(null);
    }

    public List<StaffBean> finVisitPlanOfCalidadByParentId(Long parentId) {
        return visitPlanMapRepository.finVisitPlanOfCalidadByParentId(parentId);
    }

    public void checkEvaluationCalidad(List<VisitPlanMap> visitPlanMaps1, CheckListDto checkListDto, CheckListBean cl, GetCheckListOfAuditorRequest request) {
        if (visitPlanMaps1.size() > 0 && cl.getJobCode().equals(Constants.CALIDAD_CODE)) {
            VisitPlanMap visitPlanMap = visitPlanMaps1.stream()
                    .max(Comparator.comparing(VisitPlanMap::getId))
                    .orElse(null);
            DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate visitTime = LocalDate.parse(visitPlanMap.getVisitTime().toString().split(" ")[0], f);
            LocalDate nowDay = LocalDate.parse(LocalDate.now().toString(), f);
            Boolean compare = visitTime.equals(nowDay);
            if (compare == true) {
                List<StaffBean> staffBeans = finVisitPlanOfCalidadByParentId(visitPlanMap.getId());
                List<StaffDto> staffDtos = staffBeans.stream().map(s -> {
                    StaffDto staffDto = new StaffDto();
                    staffDto.setStaffId(s.getStaffId());
                    staffDto.setStaffCode(s.getStaffCode());
                    return staffDto;
                }).collect(Collectors.toList());
                checkListDto.getStaffList().addAll(staffDtos);
                List<Staff> staffs = staffRepository.getStaffByShopId(request.getChannelId());
                if (staffBeans.size() < staffs.size()) {
                    checkListDto.setQuantity(checkListDto.getQuantity() - 1);
                    checkListDto.setCheckListFull(false);
                } else {
                    checkListDto.setCheckListFull(true);
                }
            }
        }
    }

    public List<String> ConnectToUpLoadFile(SaveFileFtpRequest request) {
        List<String> filePaths = new ArrayList<>();
        FTPClient ftpClient = Constants.TRUE.equalsIgnoreCase(ftpIsSsl) ? new FTPSClient() : new FTPClient();
        try {
            FTPHelper.connectAndLogin(ftpClient, ftpHost, ftpUser, ftpPass);
            for (FileDto f : request.getFileDtos()) {
                String filePath = uploadFileToFtpServer(ftpClient, f.getFileName(), f.getFileContent());
                filePaths.add(filePath);
            }
            return filePaths;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                FTPHelper.disconnect(ftpClient);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String uploadFileToFtpServer(FTPClient ftpClient, String fileNameUpload, String fileContent) {
        try {
            String ext = "";
            String[] names = fileNameUpload.split("\\.");
            if (names.length > 1) {
                ext = "." + names[names.length - 1];
            }
            String fileName = Calendar.getInstance().getTimeInMillis() + "_msm" + ext;
            String fileNameFull = fileNameFullFolder + File.separator + fileName;
            byte[] decodedImg = Base64.getDecoder().decode(fileContent.getBytes(StandardCharsets.UTF_8));
            Path destinationFile = Paths.get(fileNameFullFolder, fileName);
            Files.write(destinationFile, decodedImg);
            // upload to ftp server
            final String remotePath = FileUtils.concatPath(ftpWorkingDir, fileName);
            System.out.println("=======Start upload to pdf file:" + fileName);
            System.out.println("=======Start upload to pdf remote file:" + remotePath);
            FTPHelper.upload(ftpClient, fileNameFull, remotePath);
            org.apache.commons.io.FileUtils.deleteQuietly(new File(fileNameFull));
            System.out.println("=======End upload to pdf file:" + fileName);
            System.out.println("=======End upload to pdf remote file:" + remotePath);
            return remotePath;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public FileDto downloadFileToFtpServer(String filePath) {
        FTPFileWriter ftpFileWriter = new FTPFileWriter();
        FileDto fileDto = new FileDto();
        String finalFilePath = null;
        OutputStream outputstream = null;
        String pdfData = "data:application/octet-stream;base64,";
        String pngData = "data:image/png;base64,";
        String jpegData = "data:image/jpeg;base64,";
        int lastIndexOfFileSeparator = filePath.lastIndexOf("/");
        String pathFile = filePath.substring(0, lastIndexOfFileSeparator);
        String fileName = filePath.substring(lastIndexOfFileSeparator + 1);
        try {
            ftpFileWriter.open(FTPProperties.builder().server(ftpHost)
                    .port(Integer.parseInt(ftpPort)).username(ftpUser).password(ftpPass)
                    .autoStart(false).keepAliveTimeout(10).build());
            if (ftpFileWriter.isConnected()) {
                String tmpdir = fileNameFullFolder;
                File folder = new File(tmpdir);
                if (!folder.exists()) {
                    folder.mkdirs();
                }

                if (ftpFileWriter.checkFileExists(pathFile, fileName)) {
                    finalFilePath = tmpdir + File.separator + Calendar.getInstance().getTimeInMillis() + "_" + fileName;
                    logger.debug("===========FTPFilePath file path {}", filePath);
                    outputstream = new FileOutputStream(finalFilePath);
                    ftpFileWriter.loadFile(fileName, outputstream);
                    outputstream.flush();
                    logger.debug("===========FinalFilePath file path {}", finalFilePath);
                } else {
                    logger.error("File " + fileName + " not found on FTP server " + ftpHost);
                    logger.error("===========File not found on FTP server", filePath);
                }
            }
        } catch (Exception ftpException) {
            logger.error("FTP server " + ftpHost + ": " + ftpException.getMessage());
        } finally {
            try {
                if (outputstream != null) {
                    outputstream.close();
                }
            } catch (IOException ioe) {
                logger.error("Error in closing the Stream {}", ioe);
            }
            ftpFileWriter.close();
            if (StringUtils.isNotEmpty(finalFilePath)) {
                fileDto.setFileName(fileName);
                StringBuilder data = new StringBuilder();
                if (fileName.toLowerCase().endsWith("pdf")) {
                    data.append(pdfData);
                } else if (fileName.toLowerCase().endsWith("png")) {
                    data.append(pngData);
                } else if (fileName.toLowerCase().endsWith("jpg") || fileName.toLowerCase().endsWith("jpeg")) {
                    data.append(jpegData);
                }
                String base64 = FileUtils.base64encoder(finalFilePath);
                if (StringUtils.isNotEmpty(base64)) {
                    data.append(base64);
                    fileDto.setFileContent(data.toString());
                }
                org.apache.commons.io.FileUtils.deleteQuietly(new File(finalFilePath));
            }
        }
        return fileDto;
    }

    //actionPlan
    public List<VisitPlanMap> findVisitPlanActionPlanBybrIdAndChannelId(Long branchId, Long channelToId) {
        return visitPlanMapRepository.findVisitPlanActionPlanBybrIdAndChannelId(branchId, channelToId);
    }

    public List<VisitPlanMap> findActionPlanBybrIdAndChannelId(Long branchId, Long channelToId) {
        return visitPlanMapRepository.findActionPlanBybrIdAndChannelId(branchId, channelToId);
    }

    public Long countActionPlanBybrIdAndChannelId(Long branchId, Long channelToId) {
        Integer count = visitPlanMapRepository.countActionPlanBybrIdAndChannelId(branchId, channelToId);
        Long countLong = Long.parseLong(String.valueOf(count));
        return countLong;
    }

    public void checkActionPlanOfEvaluationCalidad(List<VisitPlanMap> visitPlanMaps1, CheckListDto checkListDto, CheckListBean cl, GetActionPlanCheckListOfAuditorRequest request) {
        if (visitPlanMaps1.size() > 0 && cl.getJobCode().equals(Constants.CALIDAD_CODE)) {
            VisitPlanMap visitPlanMap = visitPlanMaps1.stream()
                    .max(Comparator.comparing(VisitPlanMap::getId))
                    .orElse(null);
            DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate visitTime = LocalDate.parse(visitPlanMap.getVisitTime().toString().split(" ")[0], f);
            LocalDate nowDay = LocalDate.parse(LocalDate.now().toString(), f);
            Boolean compare = visitTime.equals(nowDay);
            if (compare == true) {
                List<StaffBean> staffBeans = finVisitPlanOfCalidadByParentId(visitPlanMap.getId());
                List<StaffDto> staffDtos = staffBeans.stream().map(s -> {
                    StaffDto staffDto = new StaffDto();
                    staffDto.setStaffId(s.getStaffId());
                    staffDto.setStaffCode(s.getStaffCode());
                    return staffDto;
                }).collect(Collectors.toList());
                checkListDto.getStaffList().addAll(staffDtos);
                List<Staff> staffs = staffRepository.getStaffByShopId(request.getChannelId());
                if (staffBeans.size() < staffs.size()) {
                    checkListDto.setQuantity(checkListDto.getQuantity() - 1);
                    checkListDto.setCheckListFull(false);
                } else {
                    checkListDto.setCheckListFull(true);
                }
            }
        }
    }

    public VisitPlanMap findACByParentId(Long parentId) {
        VisitPlanMap visitPlanMap = visitPlanMapRepository.findACByParentId(parentId);
        return visitPlanMap;
    }

    public VisitPlanMap saveAuditACCheckList(VisitPlanMap visitPlanMap, GetCheckListACResultRequest request, ResultCheckListDto resultCheckListDto) {
        Shop bcShop = null;
        if (visitPlanMap.getPdvChannelObjectType().equals(Constants.OBJECT_TYPE_SHOP)) {
            Shop shop = shopRepository.findById(visitPlanMap.getPdvId()).orElse(null);
            bcShop = shopRepository.findById(shop.getParentShopId()).orElse(null);
        }
        visitPlanMap.setBcCode(bcShop.getShopCode());
        visitPlanMap.setBcId(bcShop.getShopId());
        visitPlanMap.setCreatedDate(new Date());
        visitPlanMap.setUpdatedDate(new Date());
        visitPlanMap.setStatus(Constants.ACTION_PLAN_STATUS_DONE);
        VisitPlanMap visitPlanMap1 = visitPlanMapRepository.save(visitPlanMap);
        return visitPlanMap1;
    }

    public ResultCheckListDto makeResultACCheckListDto(VisitPlanMap visitPlanMap, GetCheckListACResultRequest request) {
        ResultCheckListDto resultCheckListDto = new ResultCheckListDto();
        Shop br = shopRepository.findById(request.getBranchId()).orElse(null);
        resultCheckListDto.setBranchName(br != null ? br.getName() : null);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        resultCheckListDto.setVisitTime(simpleDateFormat.format(new Date()));
        resultCheckListDto.setBranchCode(request.getBranchCode());
        resultCheckListDto.setChannelFromId(visitPlanMap.getZonalId());
        resultCheckListDto.setChannelFromCode(visitPlanMap.getZonalCode());
        try {
            resultCheckListDto.setEvaluationDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(DateUtil.dbUpdateDateTime2String(visitPlanMap.getVisitTime()).split(" ")[0]));
        } catch (Exception e) {
            e.printStackTrace();
        }
        resultCheckListDto.setChannelCode(visitPlanMap.getPdvCode());
        resultCheckListDto.setChannelName(findNameById(visitPlanMap));
        resultCheckListDto.setQuantityPerMonth(request.getQuantityPerMonth());
        int itemOk = 0;
        int totalItem = 0;
        float score = 0;
        float totalScore = 100f;
        List<CheckListACItemDto> checkListItem = request.getCheckListItem();
        boolean check = true;
        for (CheckListACItemDto checkListItemDto : checkListItem) {
            // Tinh item
            if (checkListItemDto.getResult().equalsIgnoreCase("OK")) {
                itemOk += 1;
                totalItem += 1;
            } else if (checkListItemDto.getResult().equalsIgnoreCase("NOK")) {
                totalItem += 1;
            } else if (checkListItemDto.getResult().equalsIgnoreCase("NA")) {
                totalScore -= checkListItemDto.getPercent();
            }
            // check location
            if (!mapLocation(visitPlanMap.getPdvId(), checkListItemDto.getX(), checkListItemDto.getY(), request.getChannelTypeId())) {
                check = false;
            }
            // Tinh score
            if (checkListItemDto.getResult().equalsIgnoreCase("OK")) {
                score += checkListItemDto.getPercent();
            }
        }
        resultCheckListDto.setLocation(check ? Constants.LOCATION_VALID : Constants.LOCATION_INVALID);
        resultCheckListDto.setValid(check ? true : false);
        if (totalScore == 0f) {
            resultCheckListDto.setScore(100f);
        } else {
            resultCheckListDto.setScore(score * 100f / totalScore);
        }
        resultCheckListDto.setItemOk(itemOk);
        resultCheckListDto.setTotalItem(totalItem);
        resultCheckListDto.setActionPlan(false);
        // set quantity
        if (resultCheckListDto.isValid()) {
            resultCheckListDto.setQuantity(request.getQuantity());
        } else {
            resultCheckListDto.setQuantity(request.getQuantity() - 1);
        }

        return resultCheckListDto;
    }

    public List<PlanResult> setACPlanResults(VisitPlanMap visitPlanResult, GetCheckListACResultRequest request) {
        List<PlanResult> planResults = request.getCheckListItem().stream().map(it -> {
            PlanResult planResult = new PlanResult();
            BeanUtils.copyProperties(it, planResult);
            if (planResult.getReasonId() == 0l) {
                planResult.setReasonId(null);
            } else {
                planResult.setReasonId(it.getReasonId());
            }
            planResult.setItemConfigId(it.getId());
            planResult.setStatus(1l);
            planResult.setCreatedDate(new Date());
            planResult.setLastUpdate(new Date());
            planResult.setLatitude(it.getX());
            planResult.setLongitude(it.getY());
            planResult.setVisitPlanId(visitPlanResult.getId());
            planResult.setFilePath(it.getFilePath());
            return planResult;
        }).collect(Collectors.toList());
        return planResults;
    }

    public void makeUpdateResultACCheckListDto(VisitPlanMap visitPlanMap, GetCheckListACResultRequest request, List<CheckListACItemDto> checkListACItemDtoList) {
        ResultCheckListDto resultCheckListDto = new ResultCheckListDto();
        float score = 0;
        float totalScore = 100f;
        List<CheckListACItemDto> checkListItem = request.getCheckListItem();
        checkListItem.addAll(checkListACItemDtoList);
        boolean check = true;
        for (CheckListACItemDto checkListItemDto : checkListItem) {
            if (checkListItemDto.getResult().equalsIgnoreCase("NA")) {
                totalScore -= checkListItemDto.getPercent();
            }
            // Tinh score
            if (checkListItemDto.getResult().equalsIgnoreCase("OK")) {
                score += checkListItemDto.getPercent();
            }
        }
        if (totalScore == 0f) {
            visitPlanMap.setScore(100f);
        } else {
            visitPlanMap.setScore(score * 100f / totalScore);
        }
        visitPlanMap.setUpdatedDate(new Date());
        visitPlanMapRepository.save(visitPlanMap);
    }
}
