package sparksample.infrastructure;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

import com.typesafe.config.Config;

import org.sql2o.Sql2o;
import org.sql2o.converters.Converter;
import org.sql2o.converters.ConverterException;
import org.sql2o.converters.UUIDConverter;
import org.sql2o.quirks.PostgresQuirks;

public class Sql2oBuilder {

    private final Config config;

    public Sql2oBuilder(Config config) {
        this.config = config;
    }

    public Sql2o build() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        PostgresQuirks quirks = new PostgresQuirks() {
            {
                converters.put(LocalDateTime.class, new LocalDateTimeConverter());
                converters.put(UUID.class, new UUIDConverter());
            }
        };
        String jdbcUrl = config.getString("jdbc.url");
        String jdbcUser = config.getString("jdbc.user");
        String jdbcPassword = config.getString("jdbc.password");
        Sql2o sql2o = new Sql2o(jdbcUrl, jdbcUser, jdbcPassword, quirks);
        return sql2o;
    }

    private static class LocalDateTimeConverter implements Converter<LocalDateTime> {

        @Override
        public LocalDateTime convert(Object val) throws ConverterException {
            Timestamp timestamp = (Timestamp)val;
            return timestamp.toLocalDateTime();
        }

        @Override
        public Object toDatabaseParam(LocalDateTime val) {
            return Timestamp.valueOf(val);
        }

    }
}
