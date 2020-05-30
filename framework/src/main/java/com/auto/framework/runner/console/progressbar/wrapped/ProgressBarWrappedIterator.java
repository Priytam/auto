package com.auto.framework.runner.console.progressbar.wrapped;

import com.auto.framework.runner.console.progressbar.ProgressBar;

import java.util.Iterator;

public class ProgressBarWrappedIterator<T> implements Iterator<T>, AutoCloseable {

    private Iterator<T> underlying;
    private ProgressBar pb;

    public ProgressBarWrappedIterator(Iterator<T> underlying, ProgressBar pb) {
        this.underlying = underlying;
        this.pb = pb;
    }

    public ProgressBar getProgressBar() {
        return pb;
    }

    @Override
    public boolean hasNext() {
        boolean r = underlying.hasNext();
        if (!r) pb.close();
        return r;
    }

    @Override
    public T next() {
        T r = underlying.next();
        pb.step();
        return r;
    }

    @Override
    public void remove() {
        underlying.remove();
    }

    @Override
    public void close() {
        pb.close();
    }
}
