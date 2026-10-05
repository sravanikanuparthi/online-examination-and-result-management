package util;

public final class InputValidator {
    private InputValidator() {}

    public static boolean isOption(String input) {
        return input != null && input.trim().matches("[ABCDabcd]");
    }

    public static char option(String input) {
        return Character.toUpperCase(input.trim().charAt(0));
    }
}
