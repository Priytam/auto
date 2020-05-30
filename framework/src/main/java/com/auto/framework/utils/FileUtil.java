package com.auto.framework.utils;

import com.auto.framework.operation.commmand.CommandResult;
import com.auto.framework.operation.commmand.TestCommandExecution;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public class FileUtil {
    private static Logger log = Logger.getLogger(FileUtil.class);

    public static boolean delete(String path) {
        return delete(path, true);
    }

    public static boolean delete(String path, boolean bFollowLinks) {
        return delete(new File(path), bFollowLinks);

    }

    public static boolean delete(File file) {
        return delete(file, true);
    }

    private static boolean delete(File file, boolean bFollowLinks) {
        if (!file.exists()) {
            log.warn("delete() - file " + file.getAbsolutePath() + " doesn't exist");
            return false;
        }
        if (!file.getParentFile().canWrite()) {
            log.warn("delete() - no write permission on file " + file.getAbsolutePath());
            return false;
        }
        try {
            if (isFollowedLink(file, bFollowLinks)) {
                File[] fileList = file.listFiles();
                if (null != fileList && fileList.length > 0) {
                    for (int i = 0; i < fileList.length; i++) {
                        if (!fileList[i].exists()) {
                            fileList[i].delete();
                            continue;
                        }
                        if (false == delete(fileList[i], bFollowLinks)) {
                            log.warn("delete() - failed on " + fileList[i].getAbsolutePath());
                            return false;
                        }
                    }
                }
            }
        } catch (IOException e) {
        }
        return file.delete();
    }


    static boolean isFollowedLink(File file, boolean followLinks) throws IOException {
        return !isSymlink(file) || (followLinks && !isLinkToSelf(file));
    }

    public static boolean isSymlink(File file) throws IOException {
        if (file == null) {
            throw new NullPointerException("File must not be null");
        }
        File canon;
        if (file.getParent() == null) {
            canon = file;
        } else {
            File canonDir = file.getParentFile().getCanonicalFile();
            canon = new File(canonDir, file.getName());
        }
        return !canon.getCanonicalFile().equals(canon.getAbsoluteFile());
    }

    private static boolean isLinkToSelf(File renamedFile) throws IOException {
        return Files.readSymbolicLink(renamedFile.toPath()).toString().equals(".");
    }

    public static boolean createLinkSilent(String linkName, String target) {
        return TestCommandExecution.runCommandWithoutTrace("/bin/rm", "-rf", linkName).getExitStatus() == 0
                && TestCommandExecution.runCommandWithoutTrace("/bin/ln", "-s", target, linkName).getExitStatus() == 0;
    }

    public static boolean createLink(String linkName, String target) {
        return runCommand(new String[]{"/bin/rm", "-rf", linkName}) &&
                runCommand(new String[]{"/bin/ln", "-s", target, linkName});
    }

    public static void copyFile(File in, File out) {
        try {
            FileInputStream fis = new FileInputStream(in);
            FileOutputStream fos = new FileOutputStream(out);
            byte[] buf = new byte[1024];
            int i = 0;
            while ((i = fis.read(buf)) != -1) {
                fos.write(buf, 0, i);
            }
            fis.close();
            fos.close();
            out.setLastModified(in.lastModified());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void copyFile(String in, String out) {
        copyFile(new File(in), new File(out));
    }

    public static void renameFile(File in, File out) throws IOException {
        if (false == in.canWrite()) {
            throw new IOException("delete() - no write permission on file " + in.getAbsolutePath());
        }
        File[] fileList = in.listFiles();
        if (null != fileList && fileList.length > 0) {
            for (int i = 0; i < fileList.length; i++) {
                renameFile(fileList[i], new File(out, in.getName()));
            }
        }
        in.renameTo(out);
    }


    public static String readLink(String sLink) {
        try {
            return new File(sLink).getCanonicalPath();
        } catch (IOException e) {
            return sLink;
        }
    }

    public static String resolveLinks(String destination) {
        try {
            File file = new File(destination + "/");
            if (!file.exists()) {
                return null;
            }
            return file.getCanonicalPath();
        } catch (IOException e) {
            return destination;
        }
    }

    public static boolean chmod(String path, String mode, boolean recursive) {
        String chmod = "/bin/chmod";
        String[] cmd = recursive ? new String[]{chmod, "-R", mode, path} : new String[]{chmod, mode, path};
        return runCommand(cmd);
    }


    private static boolean runCommand(String[] cmd) {
        CommandResult commandResult = TestCommandExecution.runCommand(cmd);
        try {
            if (log.isDebugEnabled()) {
                log.debug("runCommand() - commnad " + Arrays.toString(cmd));
                log.debug("runCommand() - exitstatus " + commandResult.getExitStatus());
                log.debug("runCommand() - output " + commandResult.getStdOut());
            }
            return (commandResult.getExitStatus() == 0);
        } catch (Exception e) {
            log.warn("runCommand() - cannot execute ln for " + Arrays.toString(cmd), e);
        }
        return false;
    }

    public static String getParent(String sFile) {
        return new File(sFile).getParent();
    }


    public static void createDir(String sDirnameFull) throws IOException {
        boolean fileCreated, fileCanWrite;
        if (!sDirnameFull.contains(File.separator)) {
            throw new IOException("Given file " + sDirnameFull + " is invalid");
        }
        (new File(sDirnameFull)).mkdirs();
        File file = new File(sDirnameFull + File.separator + System.currentTimeMillis() + "_test.txt");
        fileCreated = file.createNewFile();
        fileCanWrite = file.canWrite();
        if (fileCreated) {
            file.delete();
        }
        if (!fileCanWrite) {
            throw new IOException("Fail write to dir " + sDirnameFull);
        }
    }


    public static void setContents(String aFile, String aContents) {
        setContents(new File(aFile), aContents, false);
    }

    public static void setContents(File aFile, String aContents) {
        setContents(aFile, aContents, false);
    }

    public static void setContents(File aFile, String aContents, boolean append) {
        if (aFile == null) {
            throw new IllegalArgumentException("File should not be null.");
        }
        if (!aFile.exists()) {
            throw new RuntimeException(new FileNotFoundException("File does not exist: " + aFile));
        }
        if (!aFile.isFile()) {
            throw new IllegalArgumentException("Should not be a directory: " + aFile);
        }
        if (!aFile.canWrite()) {
            throw new IllegalArgumentException("File cannot be written: " + aFile);
        }
        try {
            // use buffering
            Writer output = getWriter(aFile, append);
            try {
                // FileWriter always assumes default encoding is OK!
                output.write(aContents);
            } finally {
                output.close();
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Writer getWriter(File aFile, boolean append) {
        try {
            Writer output = new BufferedWriter(new FileWriter(aFile, append));
            return output;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String getContents(File aFile) {
        StringBuilder contents = new StringBuilder();
        try {
            doGetContentsEx(aFile, contents);
        } catch (IOException ex) {
            log.error("Error reading file " + aFile.getPath(), ex);
        }
        return contents.toString();
    }

    private static void doGetContentsEx(File aFile, StringBuilder contents) throws IOException {
        FileReader input = new FileReader(aFile);
        try {
            int line;
            char[] buf = new char[1024];
            while ((line = input.read(buf)) != -1) {
                contents.append(buf, 0, line);
            }
        } finally {
            input.close();
        }
    }

    public static boolean exist(String path) {
        return Files.exists(Paths.get(path));
    }

}
