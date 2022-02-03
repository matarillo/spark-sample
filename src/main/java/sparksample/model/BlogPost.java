package sparksample.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import lombok.Data;

@Data
public class BlogPost {

    private static final DateTimeFormatter FORMATTER;

    static {
        FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    }

    private UUID id;
    private String title;
    private String body;
    private LocalDateTime lastUpdated;
    private LocalDateTime published;

    public String formatLastUpdated() {
        return lastUpdated.format(FORMATTER);
    }

    public String formatPublished() {
        return published.format(FORMATTER);
    }
}
