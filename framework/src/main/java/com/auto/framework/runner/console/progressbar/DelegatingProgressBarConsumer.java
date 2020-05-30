package com.auto.framework.runner.console.progressbar;

import java.util.function.Consumer;

public class DelegatingProgressBarConsumer implements ProgressBarConsumer {

    private final int maxProgressLength;
    private final Consumer<String> consumer;

    public DelegatingProgressBarConsumer(Consumer<String> consumer) {
        this(consumer, Util.getTerminalWidth());
    }

    public DelegatingProgressBarConsumer(Consumer<String> consumer, int maxProgressLength) {
        this.maxProgressLength = maxProgressLength;
        this.consumer = consumer;
    }

    @Override
    public int getMaxProgressLength() {
        return maxProgressLength;
    }

    @Override
    public void accept(String str) {
        this.consumer.accept(str);
    }

    @Override
    public void close() {
        //NOOP
    }
}
