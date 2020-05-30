package com.auto.framework.runner.console.progressbar;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;

class Util {

    private static int defaultTerminalWidth = 160;

    static String repeat(char c, int n) {
        if (n <= 0) return "";
        char[] s = new char[n];
        for (int i = 0; i < n; i++) s[i] = c;
        return new String(s);
    }

    static String formatDuration(Duration d) {
        long s = d.getSeconds();
        return String.format("%02d:%02d", (s % 3600) / 60, s % 60);
    }

    static long getInputStreamSize(InputStream is) {
        try {
            if (is instanceof FileInputStream)
                return ((FileInputStream) is).getChannel().size();
        }
        catch (IOException e) {
            return -1;
        }
        return -1;
    }

    static Terminal getTerminal() {
        Terminal terminal = null;
        try {
            terminal = TerminalBuilder.builder().dumb(true).build();
        }
        catch (IOException ignored) { }
        return terminal;
    }

    static int getTerminalWidth(Terminal terminal) {
        if (terminal != null && terminal.getWidth() >= 10) // Workaround for issue #23 under IntelliJ
            return terminal.getWidth();
        else return defaultTerminalWidth;
    }

    static int getTerminalWidth() {
        Terminal terminal = getTerminal();
        int width = getTerminalWidth(terminal);
        try {
            if (terminal != null)
                terminal.close();
        }
        catch (IOException ignored) { /* noop */ }
        return width;
    }

}
