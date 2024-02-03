package com.viettel.msm.smartphone.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public final class FTPHelper {
    private static final Logger LOG = LoggerFactory.getLogger(FTPHelper.class);
    private static final String TEMPORATOR_POST_FIX_FILE_NAME = "_TEMP";

    private FTPHelper() {
    }

    public static void upload(final FTPClient ftpClient, final String source, final String dest) throws IOException {
        File file = null;
//        connectAndLogin(ftpClient, host, username, password);

        try {
            LOG.debug("Storing file to FTP server.");
            file = new File(source);
            ftpClient.storeFile(dest.concat(TEMPORATOR_POST_FIX_FILE_NAME), new FileInputStream(file));
            ftpClient.rename(dest.concat(TEMPORATOR_POST_FIX_FILE_NAME), dest);
//            boolean existed = checkFileExists(ftpClient, dest);
//            System.out.println(existed);
        } catch (IOException var9) {
            LOG.error("Exception occurred while store file to FTP server: {}, {}", source, var9);
            throw var9;
        }
//        disconnect(ftpClient);
    }

    public static void connectAndLogin(final FTPClient ftpClient, final String host, final String username,
                                        final String password) throws IOException {
        try {
            LOG.debug("Checking ftp connecting status.");
            if (!ftpClient.isConnected()) {
                ftpClient.connect(host);
                int reply = ftpClient.getReplyCode();
                if (!FTPReply.isPositiveCompletion(reply)) {
                    disconnect(ftpClient);
                    throw new IOException("Exception in connecting to FTP Server");
                }

                if (!ftpClient.login(username, password)) {
                    throw new IOException("Incorrect username or password");
                }

                ftpClient.setFileType(2);
                ftpClient.enterLocalPassiveMode();
            }
        } catch (IOException var5) {
            LOG.error("Exception occurred while sign in FTP server: ", var5);
            throw var5;
        }
    }

    public static void disconnect(final FTPClient ftpClient) throws IOException {
        if (ftpClient.isConnected()) {
            try {
                ftpClient.disconnect();
            } catch (IOException var2) {
                LOG.error("Exception occurred while disconnect to FTP server: ", var2);
                throw var2;
            }
        }
    }

    public static boolean checkFileExists(FTPClient ftpClient, String filePath) throws IOException {
        if (StringUtils.isEmpty(filePath)){
            return false;
        }
//        ftpClient.changeWorkingDirectory(filePath);
        FTPFile[] files = ftpClient.listFiles(filePath);
        if (files == null || files.length < 1) {
            return false;
        }
        return true;
    }

    public static boolean loadFile(FTPClient ftpClient, String remotePath, OutputStream outputStream) {
        try {
            LOG.debug("Trying to retrieve a file from remote path " + remotePath);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            return ftpClient.retrieveFile(remotePath, outputStream);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
            return false;
        }
    }
}
