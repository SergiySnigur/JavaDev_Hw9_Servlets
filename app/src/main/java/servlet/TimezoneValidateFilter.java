package servlet;

import servlet.settings.TimeZoneQueryParams;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.ZoneId;

@WebFilter(value = "/time")
public class TimezoneValidateFilter extends HttpFilter {
    private final TimeZoneQueryParams timeZoneUtil = new TimeZoneQueryParams();

    @Override
    protected void doFilter(HttpServletRequest req,
                            HttpServletResponse resp,
                            FilterChain chain) throws IOException, ServletException {
        boolean validZoneId = true;

        try {
            ZoneId.of(timeZoneUtil.parseTimeZone(req));
        } catch (DateTimeException ex) {
            validZoneId = false;
        }

        if (validZoneId) {
            chain.doFilter(req, resp);
        } else {
            resp.setStatus(400);

            resp.setContentType("application/json");
            resp.getWriter().write("Invalid timezone");
            resp.getWriter().close();
        }
    }
}
