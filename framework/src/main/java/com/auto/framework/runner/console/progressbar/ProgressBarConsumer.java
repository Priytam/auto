package com.auto.framework.runner.console.progressbar;

import java.util.function.Consumer;


public interface ProgressBarConsumer extends Consumer<String>, Appendable, AutoCloseable {

    int getMaxProgressLength();

    void accept(String rendered);

    default ProgressBarConsumer append(CharSequence csq) {
        accept(csq.toString());
        return this;
    }

    default ProgressBarConsumer append(CharSequence csq, int start, int end) {
        accept(csq.subSequence(start, end).toString());
        return this;
    }

    default ProgressBarConsumer append(char c) {
        accept(String.valueOf(c));
        return this;
    }

    void close();

}
