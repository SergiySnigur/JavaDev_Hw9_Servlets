package servlet;

import servlet.settings.TimeZoneQueryParams;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZoneId;

@WebFilter(value = "/time")
public class TimezoneValidateFilter extends HttpFilter {
    private final TimeZoneQueryParams queryParams = new TimeZoneQueryParams();

    protected void doFilter(HttpServletRequest req,
                            HttpServletResponse res,
                            FilterChain chain) throws ServletException, IOException {

        String timezone = req.getParameter("timezone");

        if (timezone == null || timezone.equals("")) {
            chain.doFilter(req, res);
        }

        try {
            ZoneId.of(timezone.replace(" ", "+"));
        } catch (Exception e) {
            res.setStatus(400);
            res.setContentType("text/html");
            res.getWriter().write("Invalid timezone!");
            res.getWriter().close();
        }

        chain.doFilter(req, res);
    }
}
