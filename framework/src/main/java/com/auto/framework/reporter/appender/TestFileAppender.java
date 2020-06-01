package com.auto.framework.reporter.appender;

import com.auto.framework.operation.OpResult;
import com.auto.framework.operation.commmand.CommandRequest;
import com.auto.framework.reporter.iface.IOutputFileStrategy;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public class TestFileAppender extends TestAppender {

    public TestFileAppender(IOutputFileStrategy strategy) {
        super(strategy);
    }

    @Override
    protected void writeToFile(CommandRequest cRequest, OpResult rResult, String sFileName) {
        try {
            FileWriter fFileWriter = new FileWriter(new File(sFileName), true);
            fFileWriter.append("Command " + getCommandNum() + ": " + String.join(" ", cRequest.getCommand()) + "\n");
            fFileWriter.append("CurrentTime: " + new Date() + "\n");
            fFileWriter.append("Hostname: " + cRequest.getHost() + "\n");
            fFileWriter.append("ExitStatus: " + rResult.getExitStatus() + "\n");
            fFileWriter.append("Stdout: " + String.join("\n", rResult.getStdOut()) + "\n");
            fFileWriter.append("Stderr: " + String.join("\n", rResult.getStdErr()) + "\n");
            fFileWriter.close();
        } catch (Exception e) {
            logMessage("failed write to file", e);
        }
    }

    @Override
    protected void writeToFile(String sRequest, int iStatusCode, String bOutput, String sUrl, String sFileName) {
        try {
            FileWriter fFileWriter = new FileWriter(new File(sFileName), true);
            fFileWriter.append("Url: " + sUrl + "\n");
            fFileWriter.append("Request " + getCommandNum() + ": " + sRequest + " " + "\n");
            fFileWriter.append("Response: " + bOutput + "\n");
            fFileWriter.append("ResponseCode:" + iStatusCode + "\n");
            fFileWriter.append("CurrentTime: " + new Date() + "\n");
            fFileWriter.close();
        } catch (Exception e) {
            logMessage("failed write to file", e);
        }

    }

}
