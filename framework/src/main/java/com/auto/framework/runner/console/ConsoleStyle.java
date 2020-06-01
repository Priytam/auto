package com.auto.framework.runner.console;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public class ConsoleStyle {
    public static final String RESET = "\u001b[0m";  // Text Reset
    public static final String BLACK = "\u001b[30m";   // BLACK
    public static final String RED = "\u001b[31m";     // RED
    public static final String GREEN = "\u001b[32m";   // GREEN
    public static final String YELLOW = "\u001b[33m";  // YELLOW
    public static final String BLUE = "\u001b[34m";    // BLUE
    public static final String PURPLE = "\u001b[35m";  // PURPLE
    public static final String CYAN = "\u001b[36m";    // CYAN
    public static final String WHITE = "\u001b[37m";   // WHITE


    public static final String BOLD = "\u001B[1m"; //BOLD
    public static final String TICK = "\u2714"; //TICK
    public static final String CROSS = "\u2718"; //CROSS
    public static final String OPEN_BRACKET = "\uFE5D"; //OPEN_BRACKET
    public static final String CLOSE_BRACKET = "\uFE5E"; //CLOSE_BRACKET
    public static final String STAR = "\uFE61";

    public static String decorate(String style, String message) {
        return style + message + RESET;
    }
}