import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import com.typesafe.config.ConfigFactory;

import org.sql2o.Sql2o;

import spark.ModelAndView;
import spark.Request;
import spark.servlet.SparkApplication;
import spark.template.mustache.MustacheTemplateEngine;
import static spark.Spark.*;

public class Program implements SparkApplication {
    public static void main(String[] args) {
        new Program().init();
    }

    private final Sql2o db;

    public Program() {
        this.db = new Sql2oBuilder(ConfigFactory.load()).build();
    }

    @Override
    public void init() {
        staticFiles.location("/public");

        get("/", (req, res) -> {
            String root = getContextPath(req);
            int page = getPage(req);
            int pageWidth = 10;
            int offset = page * pageWidth;
            Sql2oDao dao = new Sql2oDao(db);
            int count = dao.getCount();
            List<BlogPost> posts = dao.getPosts(pageWidth, offset);
            List<PostGroup> groups = dao.getGroupsByYearMonth();
            Map<String, Object> model = new HashMap<>();
            model.put("root", root);
            model.put("posts", posts);
            model.put("groups", groups);
            model.put("pagenation", new Pagenation(page, pageWidth, count));

            return new MustacheTemplateEngine().render(
                new ModelAndView(model, "index.mustache")
            );
        });

        get("/archive/:year/:month/", (req, res) -> {
            String root = getContextPath(req);
            int year;
            int month;
            try {
                year = Integer.parseInt(req.params(":year"));
                month = Integer.parseInt(req.params(":month"));
            } catch (NumberFormatException ex) {
                res.status(404);
                return "";
            }
            int page = getPage(req);
            int pageWidth = 10;
            int offset = page * pageWidth;
            Sql2oDao dao = new Sql2oDao(db);
            int count = dao.getCountByYearMonth(year, month);
            List<BlogPost> posts = dao.getPostsByYearMonth(year, month, pageWidth, offset);
            List<PostGroup> groups = dao.getGroupsByYearMonth();
            Map<String, Object> model = new HashMap<>();
            model.put("root", root);
            model.put("year", year);
            model.put("month", month);
            model.put("posts", posts);
            model.put("groups", groups);
            model.put("pagenation", new Pagenation(page, pageWidth, count));

            return new MustacheTemplateEngine().render(
                new ModelAndView(model, "index.mustache")
            );
        });
    }

    private int getPage(Request req) {
        String pageParam = req.queryParams("page");
        int page = 0;
        try {
            page = Integer.parseInt(pageParam);
        } catch (NumberFormatException ex) {
            // ignore
        }
        return page;
    }

    private String getContextPath(Request req) {
        ServletContext ctx = req.session().raw().getServletContext();
        return ctx.getContextPath();
    }
}
