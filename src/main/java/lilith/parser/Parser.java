package lilith.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Parser class, for date and time parsing and formatting.
 */
public class Parser {

    /**
     * Accepted output date format (what the user sees).
     */
    public static final DateTimeFormatter OUTPUT_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm");

    /**
     * Accepted input date formats (what the user can input).
     */
    private static final DateTimeFormatter[] FORMATS = new DateTimeFormatter[]{
        DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"),
        DateTimeFormatter.ofPattern("yyyy/MM/dd HHmm"),
        DateTimeFormatter.ofPattern("dd-MM-yyyy HHmm"),
        DateTimeFormatter.ofPattern("dd/MM/yyyy HHmm"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
        DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"),
        DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"),
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"),
        DateTimeFormatter.ofPattern("yyyy-M-dd HHmm"),
        DateTimeFormatter.ofPattern("yyyy/M/dd HHmm"),
        DateTimeFormatter.ofPattern("dd-M-yyyy HHmm"),
        DateTimeFormatter.ofPattern("dd/M/yyyy HHmm"),
        DateTimeFormatter.ofPattern("yyyy-M-dd HH:mm"),
        DateTimeFormatter.ofPattern("yyyy/M/dd HH:mm"),
        DateTimeFormatter.ofPattern("dd-M-yyyy HH:mm"),
        DateTimeFormatter.ofPattern("dd/M/yyyy HH:mm"),
        DateTimeFormatter.ofPattern("d/M/yyyy"),
        DateTimeFormatter.ofPattern("dd/MM/yyyy"),
        DateTimeFormatter.ofPattern("d-M-yyyy"),
        DateTimeFormatter.ofPattern("dd-MM-yyyy"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd"),
        DateTimeFormatter.ofPattern("yyyy/MM/dd"),
        DateTimeFormatter.ofPattern("yyyy-M-dd"),
        DateTimeFormatter.ofPattern("yyyy/M/dd")
    };

    /**
     * Parses date/time from string input.
     * Defaults time to 00:00 if not given.
     *
     * @param dateTimeStr Input string from user.
     * @return Parsed LocalDateTime object.
     * @throws DateTimeParseException If input does not match any format.
     */
    public static LocalDateTime parseDateTime(String dateTimeStr)
            throws DateTimeParseException {

        String trimmed = dateTimeStr.trim();

        for (DateTimeFormatter formatter : FORMATS) {
            try {
                if (formatter.toString().contains("H")) {
                    return LocalDateTime.parse(trimmed, formatter);
                } else {
                    LocalDate date = LocalDate.parse(trimmed, formatter);
                    return date.atStartOfDay();
                }

            } catch (DateTimeParseException e) {
                // Try next formatter
            }
        }

        throw new DateTimeParseException(
                "I can't understand your date format.",
                trimmed,
                0
        );
    }

    /**
     * Formats date/time for output.
     *
     * @param dateTime LocalDateTime to format.
     * @return Formatted date string.
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(OUTPUT_FORMAT);
    }

    /**
     * Splits and parses the Deadline input.
     *
     * @param input User deadline command input.
     * @return String array containing task name and deadline date.
     * @throws IllegalArgumentException If format is invalid.
     */
    public static String[] parseDeadlineInput(String input)
            throws IllegalArgumentException {

        String[] parts = input.split("/by");

        if (parts.length != 2) {
            throw new IllegalArgumentException(
                    "Use: deadline <task> /by <date> [HHmm]"
            );
        }

        parseDateTime(parts[1].trim());

        return new String[]{parts[0].trim(), parts[1].trim()};
    }

    /**
     * Splits and parses the Event input.
     *
     * @param input User event command input.
     * @return String array containing task name, start, and end date.
     * @throws IllegalArgumentException If format is invalid.
     */
    public static String[] parseEventInput(String input)
            throws IllegalArgumentException {

        String[] parts = input.split("/from");

        if (parts.length != 2) {
            throw new IllegalArgumentException(
                    "Use: event <task> /from <date> [HHmm] /to <date> [HHmm]"
            );
        }

        String name = parts[0].trim();
        String[] fromTo = parts[1].split("/to");

        if (fromTo.length != 2) {
            throw new IllegalArgumentException(
                    "Use: event <task> /from <date> [HHmm] /to <date> [HHmm]"
            );
        }

        parseDateTime(fromTo[0].trim());
        parseDateTime(fromTo[1].trim());

        return new String[]{name, fromTo[0].trim(), fromTo[1].trim()};
    }

    /**
     * Parses the update command input into a String array of field updates.
     * Returns a 4-element array: [name, by, from, to]
     * Any field not provided will be null.
     * parseDateTime(val) validates the date — throws if invalid
     *
     * @param input User update command input after "update (index)" is removed.
     * @return String array of [name, by, from, to].
     */
    public static String[] parseUpdateInput(String input) {
        String[] result = new String[4];

        if (input.contains("/name")) {
            String after = input.substring(input.indexOf("/name") + 5).trim();
            String[] split = after.split("/by|/from|/to", 2);
            result[0] = split[0].trim();
        }

        if (input.contains("/by")) {
            String after = input.substring(input.indexOf("/by") + 3).trim();
            String[] split = after.split("/name|/from|/to", 2);
            String val = split[0].trim();
            parseDateTime(val);
            result[1] = val;
        }

        if (input.contains("/from")) {
            String after = input.substring(input.indexOf("/from") + 5).trim();
            String[] split = after.split("/name|/by|/to", 2);
            String val = split[0].trim();
            parseDateTime(val);
            result[2] = val;
        }

        if (input.contains("/to")) {
            String after = input.substring(input.indexOf("/to") + 3).trim();
            String[] split = after.split("/name|/by|/from", 2);
            String val = split[0].trim();
            parseDateTime(val);
            result[3] = val;
        }

        return result;
    }
}

