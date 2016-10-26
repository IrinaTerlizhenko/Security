package by.bsu.cinemarating.filter;

import by.bsu.cinemarating.exception.LogicException;
import by.bsu.cinemarating.logic.TokenLogic;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by User on 16.10.2016.
 */
@WebFilter(servletNames = {"controller"},
        initParams = {@WebInitParam(name = "INDEX_PATH", value = "/index.jsp")})
public class TokenFilter implements Filter {
    private String indexPath;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        indexPath = filterConfig.getInitParameter("INDEX_PATH");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        int userId = (int) httpRequest.getSession().getAttribute("user_id");
        String token = (String) httpRequest.getSession().getAttribute("token");
        try {
            if (!TokenLogic.checkToken(userId, token)) {
                httpRequest.getSession().invalidate();
                httpResponse.sendRedirect(httpRequest.getContextPath() + indexPath);
                return;
            }
            if (TokenLogic.expiredToken(userId)) {
                String newToken = TokenLogic.generateToken(userId);
                httpRequest.getSession().setAttribute("token", newToken);
            }
        } catch (LogicException e) {

        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        indexPath = null;
    }
}
