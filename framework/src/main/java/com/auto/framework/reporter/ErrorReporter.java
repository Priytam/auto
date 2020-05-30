package com.auto.framework.reporter;

import com.auto.framework.iface.ITestCase;
import com.auto.framework.iface.ITestComponent;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public class ErrorReporter {
    private ITestCase testCase = null;
    private int iErrorNum = 0;
    private String sOutputDir = null;
    private int iPass = 0;
    private int iFail = 0;
    private int iFatal = 0;
    private Throwable fatalThrowable;
    private boolean errorLogging = false;
    private String sFirstFailure = null;
    private static boolean saveConfigurationOnError = true;
    private static boolean bSaveInformation = true;
    private static boolean captureSnapshotOnError = true;

    public ErrorReporter(ITestCase tTest, String sOutputDir) {
        testCase = tTest;
        this.sOutputDir = sOutputDir;
    }

    private String getCurrentErrorDir() {
        return getOutputDir() + File.separator + "error" + iErrorNum;
    }

    private String getOutputDir() {
        return sOutputDir;
    }

    public void pass() {
        iPass++;
    }

    public void fail(String sMessage) {
        if (errorLogging) {
            return;
        }
        saveFailure(sMessage);
        iFail++;
        reportError();
    }

    public void fatal(Throwable t) {
        if (errorLogging) {
            return;
        }
        saveFailure(t.getMessage());
        iFatal++;
        fatalThrowable = t;
        reportError();
    }

	/*private void captureSnapshot() {
		for(ITestComponent testComponent : getTestCase().getTestComponents()) {
			testComponent.captureSnapshotOnFailure();
		}
		
	}*/

    private ITestCase getTestCase() {
        return testCase;
    }

    private void reportError() {
        if (!bSaveInformation) {
            return;
        }
        try {
            errorLogging = true;
            iErrorNum++;
            String sErrorDir = getCurrentErrorDir();
            new File(sErrorDir).mkdir();

            getTestCase().getTestComponents().forEach(cComponent -> {
                if (null != cComponent) {
                    TestReporter.TRACE("saving information on error for " + cComponent);
                    String sTargetDir = getCurrentErrorDir() + File.separator + cComponent.getComponentName() + "_" + cComponent.getHost();
                    new File(sTargetDir).mkdirs();

                    if (saveConfigurationOnError) {
                        saveFileOnError(getTestCase().getSandBoxDir(), sTargetDir);
                    }

                    if (captureSnapshotOnError) {
                        String sSnapshotDir = sTargetDir + "/snapshotOnError/";
                        new File(sSnapshotDir).mkdir();
                        captureSnapshotOnError(cComponent, sSnapshotDir);
                    }
                }

            });
        } finally {
            errorLogging = false;
        }
    }

    private void saveFileOnError(String sSourcePath, String sDestination) {

    }

    private void captureSnapshotOnError(ITestComponent cComponent, String sTargetDir) {
    }

    public int getPass() {
        if (iFail + iFatal + iPass <= 0) {
            return 1;
        }
        return iPass;
    }

    public int getFail() {
        return iFail;
    }

    public int getFatal() {
        return iFatal;
    }

    public void dump() {
        saveFileOnError(getOutputDir(), getOutputDir());
    }

    public String getFirstFailure() {
        return sFirstFailure;
    }

    private void saveFailure(String sFailure) {
        if (StringUtils.isEmpty(sFirstFailure)) {
            sFirstFailure = sFailure;
        }
    }

    public Throwable getFatalThrowable() {
        return fatalThrowable;
    }

    public static void setSaveInformation(boolean saveInformation) {
        bSaveInformation = saveInformation;
    }

    public static void setSaveConfigurationOnError(boolean m_bSaveConfigurationOnError) {
        saveConfigurationOnError = m_bSaveConfigurationOnError;
    }

    public static void setCaptureSnapshotOnError(boolean m_bCaptureSnapshotOnError) {
        captureSnapshotOnError = m_bCaptureSnapshotOnError;
    }
}
