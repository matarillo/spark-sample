package sparksample;

import static spark.Spark.get;
import static spark.Spark.staticFiles;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.typesafe.config.ConfigFactory;

import org.sql2o.Sql2o;
import org.thymeleaf.context.Context;

import spark.ModelAndView;
import spark.Request;
import spark.TemplateEngine;
import spark.servlet.SparkApplication;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import sparksample.infrastructure.Sql2oBuilder;
import sparksample.infrastructure.Sql2oDao;
import sparksample.model.BlogPost;
import sparksample.model.Pagenation;
import sparksample.model.PostGroup;

public class Program implements SparkApplication {

    private static final TemplateEngine templateEngine = new ThymeleafTemplateEngine(); // link expressions are unavailable.
    private final Sql2o db;

    public static void main(String[] args) {
        new Program().init();
    }

    public Program() {
        this.db = new Sql2oBuilder(ConfigFactory.load()).build();
    }

    @Override
    public void init() {
        staticFiles.location("/public");

        get("/", (req, res) -> {
            int page = getPage(req);
            int pageWidth = 10;
            int offset = page * pageWidth;
            Sql2oDao dao = new Sql2oDao(db);
            int count = dao.getCount();
            List<BlogPost> posts = dao.getPosts(pageWidth, offset);
            List<PostGroup> groups = dao.getGroupsByYearMonth();
            Map<String, Object> model = new HashMap<>();
            model.put("posts", posts);
            model.put("groups", groups);
            model.put("pagenation", new Pagenation(page, pageWidth, count));

            return templateEngine.render(
                new ModelAndView(model, "index")
            );
        });

        get("/archive/:year/:month/", (req, res) -> {
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
            model.put("year", year);
            model.put("month", month);
            model.put("posts", posts);
            model.put("groups", groups);
            model.put("pagenation", new Pagenation(page, pageWidth, count));

            return templateEngine.render(
                new ModelAndView(model, "index")
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
}
