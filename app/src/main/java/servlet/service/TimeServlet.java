package servlet.service;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.thymeleaf.web.servlet.JavaxServletWebApplication;
import servlet.settings.TimeZoneQueryParams;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {
    private TemplateEngine engine;

    private final TimeZoneQueryParams timeZoneQueryParams = new TimeZoneQueryParams();
    private String initTime;

    @Override
    public void init(ServletConfig config) throws ServletException {
        engine = new TemplateEngine();
        JavaxServletWebApplication application = JavaxServletWebApplication.buildApplication(config.getServletContext());

        FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setPrefix("/WEB-INF/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setOrder(engine.getTemplateResolvers().size());
        resolver.setCacheable(false);
        engine.addTemplateResolver(resolver);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ZoneId zid = ZoneId.of(timeZoneQueryParams.parseTimeZone(req));
        Clock clock = Clock.system(zid);
        initTime = LocalDateTime.now(clock).format(DateTimeFormatter.ofPattern(
                "yyyy-MM-dd hh:mm:ss "
        )) + zid;

        resp.setContentType("text/html");
        resp.addCookie(new Cookie("lastTimezone", zid.toString()));

        Context simpleContext = new Context(
                req.getLocale(),
                Map.of("initTime", initTime));

        engine.process("time", simpleContext, resp.getWriter());
        resp.getWriter().close();
    }
}
