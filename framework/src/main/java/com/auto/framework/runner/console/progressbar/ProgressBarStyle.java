package com.auto.framework.runner.console.progressbar;

import com.auto.framework.runner.console.ConsoleStyle;

public enum ProgressBarStyle {

    COLORFUL_UNICODE_BLOCK("\r", ConsoleStyle.YELLOW + "│", "│" + ConsoleStyle.RESET, '█', ' ', " ▏▎▍▌▋▊▉"),

    UNICODE_BLOCK("\r", "│", "│", '█', ' ', " ▏▎▍▌▋▊▉"),

    ASCII("\r", "[", "]", '=', ' ', ">");

    String refreshPrompt;
    String leftBracket;
    String rightBracket;
    char block;
    char space;
    String fractionSymbols;

    ProgressBarStyle(String refreshPrompt, String leftBracket, String rightBracket, char block, char space, String fractionSymbols) {
        this.refreshPrompt = refreshPrompt;
        this.leftBracket = leftBracket;
        this.rightBracket = rightBracket;
        this.block = block;
        this.space = space;
        this.fractionSymbols = fractionSymbols;
    }

}
