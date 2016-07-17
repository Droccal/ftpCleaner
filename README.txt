*** FtpCleaner ***

This is a small java tool that helps to cleanup your local or remote ftp.

I use it for my cameras that used to record alot of images and caused my NAS to have a lot of files in one directory (> 400k files). Therefore I wrote this program to cleanup files into folders. I run it with a raspberry that executes the program every hour to sort the files into the folders and there is another arg that deletes folders that are older than 14 days.

To properly start the program there need to be the following args:
-user=<username> [ Username for FTP login ]
-pw=<password> [ Password for FTP login ]
-host=<host:port> [ Host for FTP, specify with port or without, default port is 21 ]
-dir=</dir1/dir2> [ Directory in which the tool should cleanup ]

There are two commands which the tool can execute, you can also start the tool with both, then it will first execute the clean command and then the sort command
-clean [ Deletes directories that are older than 14 Days (creation date of dir) in the defined cleaning dir above]
-sort [ Sorts all files in the directories in a new folder which is named hh dd.mm.yyyy ]