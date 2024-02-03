package com.viettel.msm.smartphone.utils;

import com.viettel.msm.smartphone.bean.FTPProperties;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FTPFileWriter {

    private static final Logger logger = LoggerFactory.getLogger(FTPFileWriter.class);
    protected FTPClient ftpClient;
    private int returnCode;

    public boolean open(FTPProperties FTPProperties) throws Exception {
        close();
        logger.debug("Connecting and logging in to FTP server.");
        ftpClient = new FTPClient();
        boolean loggedIn = false;
        try {
            ftpClient.connect(FTPProperties.getServer(), FTPProperties.getPort());
            loggedIn = ftpClient.login(FTPProperties.getUsername(), FTPProperties.getPassword());
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
            if (FTPProperties.getKeepAliveTimeout() > 0)
                ftpClient.setControlKeepAliveTimeout(FTPProperties.getKeepAliveTimeout());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
        return loggedIn;
    }

    public void close() {
        if (ftpClient != null && isConnected()) {
            try {
                ftpClient.logout();
                ftpClient.disconnect();
                logger.error("FTP close");
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    public boolean loadFile(String remotePath, OutputStream outputStream) {
        try {
            logger.debug("Trying to retrieve a file from remote path " + remotePath);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            return ftpClient.retrieveFile(remotePath, outputStream);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    public boolean saveFile(InputStream inputStream, String destPath, boolean append) {
        try {
            logger.debug("Trying to store a file to destination path " + destPath);
            if (append)
                return ftpClient.appendFile(destPath, inputStream);
            else
                return ftpClient.storeFile(destPath, inputStream);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    public boolean saveFile(String sourcePath, String destPath, boolean append) {
        InputStream inputStream = null;
        try {
            inputStream = new ClassPathResource(sourcePath).getInputStream();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return false;
        }
        return this.saveFile(inputStream, destPath, append);
    }

    public boolean isConnected() {
        boolean connected = false;
        if (ftpClient != null) {
            try {
                connected = ftpClient.sendNoOp();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        logger.debug("Checking for connection to FTP server. Is connected: " + connected);
        return connected;
    }

    /**
     * Determines whether a file exists or not
     * @param filePath
     * @return true if exists, false otherwise
     * @throws IOException thrown if any I/O error occurred.
     */
    public boolean checkFileExists(String filePath, String fileName) throws IOException {
        if (StringUtils.isEmpty(fileName)){
            return false;
        }
        ftpClient.changeWorkingDirectory(filePath);
        FTPFile[] files = ftpClient.listFiles(fileName);
        if (files == null || files.length < 1) {
            return false;
        }
        return true;
    }
}
