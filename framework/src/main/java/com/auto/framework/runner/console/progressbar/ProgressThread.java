package com.auto.framework.runner.console.progressbar;

class ProgressThread implements Runnable {

    private ProgressState progress;
    private ProgressBarRenderer renderer;
    private long updateInterval;
    private ProgressBarConsumer consumer;

    ProgressThread(
            ProgressState progress,
            ProgressBarRenderer renderer,
            long updateInterval,
            ProgressBarConsumer consumer
    ) {
        this.progress = progress;
        this.renderer = renderer;
        this.updateInterval = updateInterval;
        this.consumer = consumer;
    }

    private void refresh() {
        String rendered = renderer.render(progress, consumer.getMaxProgressLength());
        consumer.accept(rendered);
    }

    void closeConsumer() {
        consumer.close();
    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                refresh();
                Thread.sleep(updateInterval);
            }
        } catch (InterruptedException ignored) {
            refresh();
            // force refreshing after being interrupted
        }
    }

}
