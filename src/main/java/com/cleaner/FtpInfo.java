package com.cleaner;

public class FtpInfo {
    private String username;
    private String password;
    private String ftpHost;
    private int ftpPort =21;
    private String cleaningDir;

    public FtpInfo(String usr, String pw, String host, String dir){
        username = usr;
        password = pw;
        ftpHost = host;
        cleaningDir = dir;
    }

    public FtpInfo(String usr, String pw, String host, int port, String dir){
        username = usr;
        password = pw;
        ftpHost = host;
        ftpPort = port;
        cleaningDir = dir;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFtpHost() {
        return ftpHost;
    }

    public int getFtpPort() {
        return ftpPort;
    }

    public String getCleaningDir() {
        return cleaningDir;
    }
}
