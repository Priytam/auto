package com.auto.framework.utils;

import com.auto.framework.runner.console.ConsoleStyle;
import com.auto.framework.env.RegressionEnvironment;
import com.auto.framework.reporter.TestReporter;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileWriter;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 9:25 pm
 * email: mrpjpandey@gmail.com
 */
public class TestContentFileDumper {
    protected static final Logger logger = Logger.getLogger(TestContentFileDumper.class);
    public static final TestContentFileDumper instance = new TestContentFileDumper();

    private TestContentFileDumper() {
    }

    public void dumpContent(String content, String extension, String name) {
        if (!RegressionEnvironment.isRegression()) {
            String sFileName = TestReporter.getOutputDir() + File.separator + TestReporter.CONTENT_DIR + File.separator + name + "." + extension;
            writeToFile(content, sFileName);
            TestReporter.TRACE(formatMessage(name, sFileName));
        } else {
            TestReporter.TRACE(formatHeading(name) + content);
        }
    }

    private String formatMessage(String name, String sResultPath) {
        return formatHeading(name) + "file://" + sResultPath;
    }

    private String formatHeading(String name) {
        return ConsoleStyle.YELLOW + ConsoleStyle.BOLD + ConsoleStyle.OPEN_BRACKET + ConsoleStyle.STAR + ConsoleStyle.STAR + ConsoleStyle.STAR
                + " content " + name + " dump " + ConsoleStyle.STAR + ConsoleStyle.STAR + ConsoleStyle.STAR + ConsoleStyle.CLOSE_BRACKET
                + ConsoleStyle.RESET + " ";
    }

    private void writeToFile(String sContent, String sFileName) {
        try {
            FileWriter fFileWriter = new FileWriter(new File(sFileName), true);
            fFileWriter.append(sContent);
            fFileWriter.close();
        } catch (Exception e) {
            logger.info("failed write to file", e);
        }
    }

    public static TestContentFileDumper getInstance() {
        return instance;
    }
}
