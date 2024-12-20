package com.peoplestrong.timeoff.encashment.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppLogger {

    private static final String FILTER_PACKAGE = "com.peoplestrong.timeoff";
    private final Logger logger;

    private AppLogger(String className) {
        logger = Logger.getLogger(className);
    }

    // Return new instance of Logger
    public static <T> AppLogger get(Class<T> targetClass) {
        return new AppLogger(targetClass.getName());
    }

    private void log(Level level, String msg) {
        if (msg == null || msg.trim().isEmpty()) return;
        logger.log(level, msg);
    }

    public void error(Throwable e) {
        log(Level.SEVERE, getStackTrace(e));
    }

    public void error(String msg) {
        log(Level.SEVERE, msg);
    }

    public void error(String message, Throwable e) {
        log(Level.SEVERE, String.format("%s%n%s", message, getStackTrace(e)));
    }

    public void info(String msg) {
        log(Level.INFO, msg);
    }

    public void warn(String msg) {
        log(Level.WARNING, msg);
    }

    public String getShortLog(Throwable e) {
        StackTraceElement[] stackTrace = e.getStackTrace();
        if (stackTrace.length > 0) {
            // Print the caller class (the first element in the stack trace)
            StackTraceElement caller = stackTrace[0];
            return "Exception thrown at: "
                    + caller.getClassName()
                    + "."
                    + caller.getMethodName()
                    + " (Line: "
                    + caller.getLineNumber()
                    + ")";
        }
        return null;
    }

    /**
     * This method return only meaningful message related to filter package
     *
     * @param e of Type Throwable
     * @return String
     */
    private String getStackTrace(Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        boolean containFilterPackage = false;
        for (StackTraceElement element : e.getStackTrace()) {
            // Filter by package name
            if (element.getClassName().startsWith(FILTER_PACKAGE)) {
                containFilterPackage = true;
                pw.println(element);
            }
        }
        if (containFilterPackage) {
			pw.close();
            return String.format("Message: %s%n StackTrace: %s", e.getMessage(), sw);
        }
        // no package found, return complete stack trace
        sw = new StringWriter();
        pw = new PrintWriter(sw);
        for (StackTraceElement element : e.getStackTrace()) {
            pw.println(element);
        }
		pw.close();
        return String.format("Message: %s%n StackTrace: %s", e.getMessage(), sw);
    }

    /**
     * This method is not to be used on controllers/Services,
     * This is only for Global exceptions
     *
     * @param e Throwable
     */
    public static void log(Throwable e) {
        String className = getCallerClass(e);
        if (className == null || className.trim().isEmpty()) {
            className = AppLogger.class.getName();
        }
        new AppLogger(className).error(e);
    }

    private static String getCallerClass(Throwable e) {
        StackTraceElement[] stackTrace = e.getStackTrace();
        if (stackTrace.length > 0) {
            // Print the caller class (the first element in the stack trace)
            StackTraceElement caller = stackTrace[0];
            return caller.getClassName();
        }
        return null;
    }
}
