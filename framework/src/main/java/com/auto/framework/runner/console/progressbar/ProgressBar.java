package com.auto.framework.runner.console.progressbar;

import com.auto.framework.runner.console.progressbar.wrapped.ProgressBarWrappedIterable;
import com.auto.framework.runner.console.progressbar.wrapped.ProgressBarWrappedIterator;
import com.auto.framework.runner.console.progressbar.wrapped.ProgressBarWrappedSpliterator;
import com.auto.framework.runner.console.progressbar.wrapped.ProgressBarWrappedInputStream;

import java.io.InputStream;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.time.Instant;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.stream.BaseStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ProgressBar implements AutoCloseable {

    private ProgressState progress;
    private ProgressThread target;
    private Thread thread;

    public ProgressBar(String task, long initialMax) {
        this(task, initialMax, 1000, System.err, ProgressBarStyle.COLORFUL_UNICODE_BLOCK, "", 1, false, null);
    }

    public ProgressBar(String task, long initialMax, ProgressBarStyle style) {
        this(task, initialMax, 1000, System.err, style, "", 1, false, null);
    }

    public ProgressBar(String task, long initialMax, int updateIntervalMillis) {
        this(task, initialMax, updateIntervalMillis, System.err, ProgressBarStyle.COLORFUL_UNICODE_BLOCK, "", 1, false, null);
    }

    public ProgressBar(String task,
                       long initialMax,
                       int updateIntervalMillis,
                       PrintStream os,
                       ProgressBarStyle style,
                       String unitName,
                       long unitSize) {
        this(task, initialMax, updateIntervalMillis, os, style, unitName, unitSize, false, null);
    }

    public ProgressBar(
            String task,
            long initialMax,
            int updateIntervalMillis,
            PrintStream os,
            ProgressBarStyle style,
            String unitName,
            long unitSize,
            boolean showSpeed) {
        this(task, initialMax, updateIntervalMillis, os, style, unitName, unitSize, showSpeed, null);
    }

    public ProgressBar(
            String task,
            long initialMax,
            int updateIntervalMillis,
            PrintStream os,
            ProgressBarStyle style,
            String unitName,
            long unitSize,
            boolean showSpeed,
            DecimalFormat speedFormat
    ) {
        this(task, initialMax, updateIntervalMillis,
                new DefaultProgressBarRenderer(style, unitName, unitSize, showSpeed, speedFormat),
                new ConsoleProgressBarConsumer(os)
        );
    }

    public ProgressBar(
            String task,
            long initialMax,
            int updateIntervalMillis,
            ProgressBarRenderer renderer,
            ProgressBarConsumer consumer
    ) {
        this.progress = new ProgressState(task, initialMax);
        this.target = new ProgressThread(progress, renderer, updateIntervalMillis, consumer);
        this.thread = new Thread(target, this.getClass().getName());

        // starts the progress bar upon construction
        progress.startTime = Instant.now();
        thread.start();
    }

    public ProgressBar stepBy(long n) {
        progress.stepBy(n);
        return this;
    }

    public ProgressBar stepTo(long n) {
        progress.stepTo(n);
        return this;
    }

    public ProgressBar step() {
        progress.stepBy(1);
        return this;
    }

    public ProgressBar maxHint(long n) {
        if (n < 0)
            progress.setAsIndefinite();
        else {
            progress.setAsDefinite();
            progress.maxHint(n);
        }
        return this;
    }


    @Override
    public void close() {
        thread.interrupt();
        try {
            thread.join();
            target.closeConsumer();
        }
        catch (InterruptedException ignored) { }
    }


    public ProgressBar setExtraMessage(String msg) {
        progress.setExtraMessage(msg);
        return this;
    }

    public long getCurrent() {
        return progress.getCurrent();
    }


    public long getMax() {
        return progress.getMax();
    }


    public String getTask() {
        return progress.getTask();
    }

    public String getExtraMessage() {
        return progress.getExtraMessage();
    }

    public static <T> Iterator<T> wrap(Iterator<T> it, String task) {
        return wrap(it,
                new ProgressBarBuilder().setTaskName(task).setInitialMax(-1)
        ); // indefinite progress bar
    }

    public static <T> Iterator<T> wrap(Iterator<T> it, ProgressBarBuilder pbb) {
        return new ProgressBarWrappedIterator<>(it, pbb.build());
    }

    public static <T> Iterable<T> wrap(Iterable<T> ts, String task) {
        return wrap(ts, new ProgressBarBuilder().setTaskName(task));
    }

    public static <T> Iterable<T> wrap(Iterable<T> ts, ProgressBarBuilder pbb) {
        long size = ts.spliterator().getExactSizeIfKnown();
        if (size != -1)
            pbb.setInitialMax(size);
        return new ProgressBarWrappedIterable<>(ts, pbb);
    }

    public static InputStream wrap(InputStream is, String task) {
        ProgressBarBuilder pbb = new ProgressBarBuilder().setTaskName(task).setInitialMax(Util.getInputStreamSize(is));
        return wrap(is, pbb);
    }

    public static InputStream wrap(InputStream is, ProgressBarBuilder pbb) {
        long size = Util.getInputStreamSize(is);
        if (size != -1)
            pbb.setInitialMax(size);
        return new ProgressBarWrappedInputStream(is, pbb.build());
    }

    public static <T> Spliterator<T> wrap(Spliterator<T> sp, String task) {
        ProgressBarBuilder pbb = new ProgressBarBuilder().setTaskName(task);
        return wrap(sp, pbb);
    }

    public static <T> Spliterator<T> wrap(Spliterator<T> sp, ProgressBarBuilder pbb) {
        long size = sp.getExactSizeIfKnown();
        if (size != -1)
            pbb.setInitialMax(size);
        return new ProgressBarWrappedSpliterator<>(sp, pbb.build());
    }

    public static <T, S extends BaseStream<T, S>> Stream<T> wrap(S stream, String task) {
        ProgressBarBuilder pbb = new ProgressBarBuilder().setTaskName(task);
        return wrap(stream, pbb);
    }

    public static <T, S extends BaseStream<T, S>> Stream<T> wrap(S stream, ProgressBarBuilder pbb) {
        Spliterator<T> sp = wrap(stream.spliterator(), pbb);
        return StreamSupport.stream(sp, stream.isParallel());
    }

}
