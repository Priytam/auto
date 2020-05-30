package com.auto.framework.runner.console.progressbar;

@FunctionalInterface
public interface ProgressBarRenderer {

    String render(ProgressState progress, int maxLength);

}
