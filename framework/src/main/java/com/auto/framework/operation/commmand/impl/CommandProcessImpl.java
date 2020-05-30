package com.auto.framework.operation.commmand.impl;

import com.auto.framework.utils.ThreadUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
abstract class CommandProcessImpl extends CommandProcess {
    protected static long SLEEP_AFTER_DESTROY_PROCESS = 500L;
    private transient static Logger log = Logger.getLogger(CommandProcess.class);
    private Process m_process = null;
    private boolean m_timedOut = false;
    private Thread m_runningThread;
    private Boolean m_isDestroyed = new Boolean(false);
    private Boolean m_isDestroyInProgress = new Boolean(false);

    protected CommandProcessImpl() {
        super();
    }

    void exec(String[] cmd, File cwd, String[] env, String user) throws IOException {
        //user yet to handle
        execInternal(cmd, cwd, env);
    }

    protected void execInternal(String[] cmd, File cwd, String[] env) throws IOException {
        setProcess(Runtime.getRuntime().exec(cmd, env, cwd));
    }

    @Override
    public int exitValue() {
        return getProcess().exitValue();
    }

    @Override
    public int waitFor() throws InterruptedException {
        m_runningThread = Thread.currentThread();
        return getProcess().waitFor();
    }

    protected boolean isRunning() {
        try {
            exitValue();
            return false;
        } catch (IllegalThreadStateException e) {
            return true;
        }
    }

    @Override
    public final void destroy() {
        destroy(true);
    }

    private final void destroyAsync() {
        destroy(false);
    }

    final private void destroy(boolean wait) {
        synchronized (m_isDestroyInProgress) {
            if (!wait) {
                if (m_isDestroyInProgress) {
                    log.info("destroy() - Command " + this + " was already destroyed");
                    return;
                }
                interuptThreadWaitingOnProcess();
                return;
            }
            m_isDestroyInProgress = true;
        }
        synchronized (m_isDestroyed) {
            if (m_isDestroyed) {
                return;
            }
            destroyInternal();
            m_isDestroyed = true;
        }
    }

    private void interuptThreadWaitingOnProcess() {
        if (m_runningThread != null && m_runningThread != Thread.currentThread() && isRunning()) {
            m_runningThread.interrupt();
        }
    }

    protected void destroyInternal() {
        Process process = getProcess();
        if (!isRunning()) {
            if (log.isDebugEnabled()) {
                log.debug("destroy() - Process " + process + " is already destroyed");
            }
        } else {
            logDestroy(process);
            process.destroy();
            ThreadUtils.sleep(SLEEP_AFTER_DESTROY_PROCESS);
            if (isRunning()) {
                log.warn("destroy() - process has not finished in normal sequnece, will try to kill it with JNI SIGKILL, this=" + this);
                sigKillProcess(process);
            }
        }
    }

    private void logDestroy(Process process) {
        Integer pid = getProcessID(process);
        if (log.isDebugEnabled()) {
            log.debug("About to destroy process " + process + " pid " + pid);
        }
        if (pid.intValue() > 0) {
            //report process
        }
    }

    protected Integer getProcessID(Process process) {
        try {
            Field pidField;
            pidField = process.getClass().getDeclaredField("pid");
            pidField.setAccessible(true);
            Integer pid = (Integer) pidField.get(process);
            return pid;
        } catch (Throwable e1) {
            log.warn("failed to get pid by reflection", e1);
            return -1;
        }
    }

    protected void sigKill(int pid) {
        if (pid > 0) {
            // kill -9
        }
    }

    private void sigKillProcess(Process process) {
        try {
            Integer pid = getProcessID(process);
            sigKill(pid.intValue());
        } catch (Throwable e1) {
            log.warn("failed to get pid by reflection, and kill it", e1);
        }
    }

    @Override
    public InputStream getErrorStream() {
        return getProcess().getErrorStream();
    }

    @Override
    public InputStream getInputStream() {
        return getProcess().getInputStream();
    }

    @Override
    public OutputStream getOutputStream() {
        return getProcess().getOutputStream();
    }

    @Override
    public void timeout() {
        // If not running, there is no meaning to timeout and the calling thread will be in charge of destroy.
        if (isRunning()) {
            setTimedOut(true);
            destroyAsync();
        }
    }

    @Override
    public boolean isTimedOut() {
        return m_timedOut;
    }

    private void setTimedOut(boolean timedOut) {
        m_timedOut = timedOut;
    }

    private Process getProcess() {
        return m_process;
    }

    private void setProcess(Process process) {
        m_process = process;
    }

    @Override
    public int getPID() throws IOException {
        Field pidField;
        Process process = getProcess();
        try {
            pidField = process.getClass().getDeclaredField("pid");
            pidField.setAccessible(true);
            Integer pid = (Integer) pidField.get(process);
            if (pid.intValue() > 0) {
                return pid;
            }
            throw new RuntimeException("Pid is " + pid);
        } catch (Exception e) {
            throw new IOException("Pid is not available", e);
        }
    }

    @Override
    public String toString() {
        return "CommandProcess [m_process=" + m_process + ", m_timedOut=" + m_timedOut + "]";
    }
}
