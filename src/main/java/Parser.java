import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 *Parser class, for date and time parsing and formatting.
 */
public class Parser {
    //Accepted input date formats
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

    //Accepted output date formats (what the user sees)
    public static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm");

    //Parses date/time from string input.
    public static LocalDateTime parseDateTime(String dateTimeStr) throws DateTimeParseException {
        String trimmed = dateTimeStr.trim();
        for (DateTimeFormatter f : FORMATS) {
            try {
                if (f.toString().contains("H")) {
                    return LocalDateTime.parse(trimmed, f);
                } else {
                    //default time to 00:00 if not given
                    LocalDate date = LocalDate.parse(trimmed, f);
                    return date.atStartOfDay();
                }
            } catch (DateTimeParseException ignored) {}
        }
        throw new DateTimeParseException("Date does not match any accepted format", trimmed, 0);
    }

    //Formats date/time for output.
    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(OUTPUT_FORMAT);
    }

    //Splitting the Deadline input & parsing it
    public static String[] parseDeadlineInput(String input) throws IllegalArgumentException {
        String[] parts = input.split("/by");
        if (parts.length != 2)
            throw new IllegalArgumentException("Use: deadline <task> /by <date> [HHmm]");
        //Validating date for deadlines
        parseDateTime(parts[1].trim());
        return new String[]{parts[0].trim(), parts[1].trim()};
    }

    //Splitting the Event input & parsing it
    public static String[] parseEventInput(String input) throws IllegalArgumentException {
        String[] parts = input.split("/from");
        if (parts.length != 2)
            throw new IllegalArgumentException("Use: event <task> /from <date> [HHmm] /to <date> [HHmm]");

        String name = parts[0].trim();
        String[] fromTo = parts[1].split("/to");
        if (fromTo.length != 2)
            throw new IllegalArgumentException("Use: event <task> /from <date> [HHmm] /to <date> [HHmm]");

        //Validating date for events
        parseDateTime(fromTo[0].trim());
        parseDateTime(fromTo[1].trim());

        return new String[]{name, fromTo[0].trim(), fromTo[1].trim()};
    }
}
