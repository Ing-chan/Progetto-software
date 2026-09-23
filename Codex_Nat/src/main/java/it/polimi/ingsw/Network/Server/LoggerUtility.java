package it.polimi.ingsw.Network.Server;

import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class LoggerUtility {
    private static final Logger logger = Logger.getLogger(LoggerUtility.class.getName());

    static {
        // Remove default handlers
        Logger rootLogger = Logger.getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        for (Handler handler : handlers) {
            rootLogger.removeHandler(handler);
        }

        // Create a new console handler
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);

        // Set the custom formatter
        consoleHandler.setFormatter(new CustomFormatter());

        // Add the handler to the logger
        logger.addHandler(consoleHandler);

        // Set the logger level
        logger.setLevel(Level.ALL);
    }

    // Custom formatter with color support
    private static class CustomFormatter extends Formatter {
        // ANSI escape codes for colors
        public static final String RESET = "\u001B[0m";
        public static final String BLACK = "\u001B[30m";
        public static final String RED = "\u001B[31m";
        public static final String GREEN = "\u001B[32m";
        public static final String YELLOW = "\u001B[33m";
        public static final String BLUE = "\u001B[34m";
        public static final String PURPLE = "\u001B[35m";
        public static final String CYAN = "\u001B[36m";
        public static final String WHITE = "\u001B[37m";

        @Override
        public String format(LogRecord record) {
            String color = switch (record.getLevel().getName()) {
                case "SEVERE" -> RED;
                case "WARNING" -> YELLOW;
                case "INFO" -> BLUE;
                case "CONFIG" -> CYAN;
                case "FINE" -> GREEN;
                case "FINER" -> PURPLE;
                case "FINEST" -> WHITE;
                default -> BLACK;
            };

            return String.format("%s%s: %s%s%n", color, record.getLevel(), record.getMessage(), RESET);
        }
    }

    public static Logger getLogger() {
        return logger;
    }
}
