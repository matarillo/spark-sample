import java.time.LocalDateTime;
import java.time.OffsetDateTime;
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
    private OffsetDateTime lastUpdated;
    private OffsetDateTime published;

    public String formatLastUpdated() {
        LocalDateTime ldt = lastUpdated.toLocalDateTime();
        return ldt.format(FORMATTER);
    }

    public String formatPublished() {
        LocalDateTime ldt = published.toLocalDateTime();
        return ldt.format(FORMATTER);
    }
}