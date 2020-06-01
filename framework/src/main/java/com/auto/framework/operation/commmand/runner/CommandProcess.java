package com.auto.framework.operation.commmand.runner;

import com.auto.framework.operation.commmand.CommandRunningException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Callable;

/**
 * User: Priytam Jee Pandey
 * Date: 01/06/20
 * Time: 7:41 pm
 * email: priytam.pandey@cleartrip.com
 */
public class CommandProcess implements Callable<Integer> {

    private final String[] cmd;
    private final String[] environment;
    private final File wd;
    private final OutputStream out;
    private final OutputStream err;

    public CommandProcess(String[] cmd, String[] environment, File wd, OutputStream out, OutputStream err) {
        this.cmd = cmd;
        this.environment = environment;
        this.wd = wd;
        this.out = out;
        this.err = err;
    }

    public static void pipe(boolean fromProcess, InputStream from, OutputStream to) throws IOException {
        try {
            final int SIZE = 4096;
            byte[] buffer = new byte[SIZE];
            for (; ; ) {
                int n;
                if (!fromProcess) {
                    n = from.read(buffer, 0, SIZE);
                } else {
                    try {
                        n = from.read(buffer, 0, SIZE);
                    } catch (IOException ex) {
                        // Ignore exception as it can be cause by closed pipe
                        break;
                    }
                }
                if (n < 0) {
                    break;
                }
                if (fromProcess) {
                    to.write(buffer, 0, n);
                    to.flush();
                } else {
                    try {
                        to.write(buffer, 0, n);
                        to.flush();
                    } catch (IOException ex) {
                        // Ignore exception as it can be cause by closed pipe
                        break;
                    }
                }
            }
        } finally {
            try {
                if (fromProcess) {
                    from.close();
                } else {
                    to.close();
                }
            } catch (IOException ex) {
                // Ignore errors on close. On Windows JVM may throw invalid
                // refrence exception if process terminates too fast.
            }
        }
    }

    @Override
    public Integer call() throws Exception {
        Process p = Runtime.getRuntime().exec(cmd, environment, wd);
        try {
            PipeThread outThread = null;
            if (out != null) {
                outThread = new PipeThread(true, p.getInputStream(), out);
                outThread.start();
            } else {
                p.getInputStream().close();
            }

            PipeThread errThread = null;
            if (err != null) {
                errThread = new PipeThread(true, p.getErrorStream(), err);
                errThread.start();
            } else {
                p.getErrorStream().close();
            }

            // wait for process completion
            for (; ; ) {
                try {
                    p.waitFor();
                    if (outThread != null) {
                        outThread.join();
                    }
                    if (errThread != null) {
                        errThread.join();
                    }
                    break;
                } catch (InterruptedException ignore) {
                }
            }

            return p.exitValue();
        } finally {
            p.destroy();
        }
    }

    static class PipeThread extends Thread {

        private boolean fromProcess;
        private InputStream from;
        private OutputStream to;

        PipeThread(boolean fromProcess, InputStream from, OutputStream to) {
            setDaemon(true);
            this.fromProcess = fromProcess;
            this.from = from;
            this.to = to;
        }

        @Override
        public void run() {
            try {
                CommandProcess.pipe(fromProcess, from, to);
            } catch (IOException ex) {
                throw new CommandRunningException(ex);
            }
        }
    }
}
