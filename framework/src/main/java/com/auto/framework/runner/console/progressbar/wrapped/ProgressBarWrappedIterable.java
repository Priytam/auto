package com.auto.framework.runner.console.progressbar.wrapped;

import com.auto.framework.runner.console.progressbar.ProgressBarBuilder;

import java.util.Iterator;

public class ProgressBarWrappedIterable<T> implements Iterable<T> {

    private Iterable<T> underlying;
    private ProgressBarBuilder pbb;

    public ProgressBarWrappedIterable(Iterable<T> underlying, ProgressBarBuilder pbb) {
        this.underlying = underlying;
        this.pbb = pbb;
    }

    public ProgressBarBuilder getProgressBarBuilder() {
        return pbb;
    }

    @Override
    public Iterator<T> iterator() {
        Iterator<T> it = underlying.iterator();
        return new ProgressBarWrappedIterator<>(
                it,
                pbb.setInitialMax(underlying.spliterator().getExactSizeIfKnown()).build()
                // getExactSizeIfKnown return -1 if not known, then indefinite progress bar naturally
        );
    }
}
