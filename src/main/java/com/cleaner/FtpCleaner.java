package com.cleaner;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class FtpCleaner
{
    private final static Logger logger = Logger.getLogger(FtpCleaner.class);
    private FTPClient client;
    private FtpInfo info;

    public static void main(String[] args) {
        FtpCleaner ftpCleaner = new FtpCleaner();
        ftpCleaner.info = buildInfo(args);
        if(ftpCleaner.info == null){
            logger.warn("Necessary arguments not specified (host, user, password, cleaningDir)");
            System.exit(2);
        }

        if(StringUtil.contains(args, "clean") || StringUtil.contains(args, "-c"))
            ftpCleaner.cleanup();
        if(StringUtil.contains(args,"sort") || StringUtil.contains(args, "-s"))
            ftpCleaner.sortFiles();
        try {
            if(ftpCleaner.client != null)
            {
                logger.info("Disconnecting client");
                ftpCleaner.client.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("error while disconnecting", e);
        }
    }

    private void sortFiles(){
        logger.info("Sorting files triggered.");
        if(client == null || !client.isConnected())
            connect();

        logger.info("Ftp client conntected.");
        try {
            client.changeWorkingDirectory(info.getCleaningDir());
            FTPFile[] ftpFiles = client.listDirectories();
            for(FTPFile file : ftpFiles)
            {
                logger.info("Changing directory to: " + file.getName() + "");
                client.changeWorkingDirectory(file.getName());

                FTPFile[] dirFiles = client.listFiles();

                if(dirFiles.length > 0)
                {
                    String newDir = new DateTime().toString("HH dd.MM.yyyy");
                    logger.info("Creating new dir:" + newDir);
                    client.makeDirectory(newDir);

                    for(FTPFile ftpFile : dirFiles)
                    {
                        logger.info("Moving File: " + ftpFile.getName() + " into subfolder");
                        if(ftpFile.isFile())
                        {
                            client.rename(ftpFile.getName(), newDir+"/"+ftpFile.getName());
                        }
                    }
                }

                client.changeToParentDirectory();
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("error while sorting", e);
        }
    }

    private void cleanup(){
        logger.info("Cleaning files triggered.");
        if(client == null || !client.isConnected())
            connect();

        logger.info("Ftp client conntected.");
        try {
            client.changeWorkingDirectory(info.getCleaningDir());
            FTPFile[] ftpFiles = client.listDirectories();
            for(FTPFile file : ftpFiles)
            {
                logger.info("Changing directory to: " + file.getName() + "");
                client.changeWorkingDirectory(file.getName());
                FTPFile[] dirFiles = client.listFiles();
                Calendar calendarInstance = Calendar.getInstance();
                calendarInstance.setTime(new Date());
                calendarInstance.add(Calendar.DATE, -14);
                for(FTPFile ftpFile : dirFiles)
                {
                    if(ftpFile.isDirectory() && ftpFile.getTimestamp().before(calendarInstance))
                    {
                        logger.info("Removing dir: "+ftpFile.getName());
                        FtpUtil.removeDirectory(client, ftpFile.getName(), "");
                    }
                }
                client.changeToParentDirectory();
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("error while cleaning", e);
        }
    }

    private static FtpInfo buildInfo(String[] args){

        if(StringUtil.contains(args, "host") && StringUtil.contains(args, "user") && StringUtil.contains(args, "pw") && StringUtil.contains(args, "dir")){
           try{
               String host = StringUtil.extract(args, "host");
               host = host.substring(host.indexOf("=") + 1);
               String[] split = host.split(":");

               String user = StringUtil.extract(args, "user");
               user = user.substring(user.indexOf("=") + 1);

               String pw = StringUtil.extract(args, "pw");
               pw = pw.substring(pw.indexOf("=") + 1);

               String dir = StringUtil.extract(args, "dir");
               dir = dir.substring(dir.indexOf("=")+1);

               if(split.length == 1)
                   return new FtpInfo(user, pw, split[0], dir);
               else if(split.length == 2)
                   return new FtpInfo(user, pw, split[0], Integer.valueOf(split[1]), dir);
           }catch(NullPointerException e){
               logger.warn("Could not extract necessary info from args.", e);
           }
        }else{
            System.exit(1);
        }
        return null;
    }

    private Boolean connect() {
        client = new FTPClient();
        try {
            client.connect(info.getFtpHost(), info.getFtpPort());
            return client.login(info.getUsername(), info.getPassword());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}