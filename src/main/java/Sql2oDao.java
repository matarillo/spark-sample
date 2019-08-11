import java.util.List;

import org.sql2o.Connection;
import org.sql2o.Sql2o;

public class Sql2oDao {
    private final Sql2o sql2o;

    public Sql2oDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    public int getCount() {
        try (Connection conn = sql2o.open()) {
            Object count = conn.createQuery("SELECT count(*) FROM blogpost WHERE published <= current_timestamp")
                .executeScalar();
            return (int)(long)count;
        }
    }

    public List<BlogPost> getPosts(int limit, int offset) {
        try (Connection conn = sql2o.open()) {
            List<BlogPost> posts = conn.createQuery("SELECT id, title, body, last_updated AS lastUpdated, published FROM blogpost WHERE published <= current_timestamp ORDER BY published DESC LIMIT :limit OFFSET :offset")
                .addParameter("limit", limit)
                .addParameter("offset", offset)
                .executeAndFetch(BlogPost.class);
            return posts;
        }
    }

    public int getCountByYearMonth(int year, int month) {
        try (Connection conn = sql2o.open()) {
            Object count = conn.createQuery("SELECT count(*) FROM blogpost WHERE published <= current_timestamp AND EXTRACT(YEAR FROM published) = :year AND EXTRACT(MONTH FROM published) = :month")
                .addParameter("year", year)
                .addParameter("month", month)
                .executeScalar();
            return (int)(long)count;
        }
    }

    public List<BlogPost> getPostsByYearMonth(int year, int month, int limit, int offset) {
        try (Connection conn = sql2o.open()) {
            List<BlogPost> posts = conn.createQuery("SELECT id, title, body, last_updated AS lastUpdated, published FROM blogpost WHERE published <= current_timestamp AND EXTRACT(YEAR FROM published) = :year AND EXTRACT(MONTH FROM published) = :month ORDER BY published DESC LIMIT :limit OFFSET :offset")
                .addParameter("year", year)
                .addParameter("month", month)
                .addParameter("limit", limit)
                .addParameter("offset", offset)
                .executeAndFetch(BlogPost.class);
            return posts;
        }
    }

    public List<PostGroup> getGroupsByYearMonth() {
        try (Connection conn = sql2o.open()) {
            List<PostGroup> groups = conn.createQuery("SELECT EXTRACT(YEAR FROM published) AS year, EXTRACT(MONTH FROM published) AS month, count(*) AS count FROM blogpost WHERE published <= current_timestamp GROUP BY year, month ORDER BY year DESC, month DESC")
                .executeAndFetch(PostGroup.class);
            return groups;
        }
    }
}