package com.auto.framework.reporter.appender;

import com.auto.framework.reporter.iface.IOutputFileStrategy;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.WriterAppender;

import java.io.IOException;
import java.io.OutputStream;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public class TestOutputStreamAppender extends TestAppender {
    private OutputStream oOut = System.out;

    public TestOutputStreamAppender(IOutputFileStrategy strategy) {
        super(strategy);
    }

    public void redirectOutput(OutputStream oOut) {
        this.oOut = oOut;
    }

    @Override
    public void logMessage(String sMessage, boolean bError) {
        super.logMessage(sMessage, bError);
        try {
            oOut.flush();
        } catch (IOException ex) {
        }
    }

    @Override
    public void logMessage(String sMessage, Throwable t) {
        super.logMessage(sMessage, t);
        try {
            oOut.flush();
        } catch (IOException e) {
        }
    }

    @Override
    public void init() {
        super.init();
        log.addAppender(new WriterAppender(new PatternLayout("%p %d{HH:mm:ss,SSS} %c %x %m%n"), oOut));
    }

}
