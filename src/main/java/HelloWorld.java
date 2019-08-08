import java.util.HashMap;
import java.util.Map;

import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;
import static spark.Spark.*;

public class HelloWorld {
    public static void main(String[] args) {
        staticFiles.location("/public");
        get("/hello", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("name", "Sam");

            return new MustacheTemplateEngine().render(
                new ModelAndView(model, "hello.mustache")
            );
        });

    }
}
