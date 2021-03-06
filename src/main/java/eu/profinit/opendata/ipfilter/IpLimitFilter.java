package eu.profinit.opendata.ipfilter;


import eu.profinit.opendata.controller.MainController;
import org.apache.catalina.connector.Request;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 *Filter class that is listening on LIMITED_PATHS URL. Redirecting to error page if some IP have reached REQ limit.
 */

public class IpLimitFilter implements Filter {

    private static String[] LIMITED_PATHS = new String[]{"/"};

    private static Logger logger = LogManager.getLogger(IpLimitFilter.class);

    IpTimeWindowManager ipTimeWindowManager;

    public IpLimitFilter(IpTimeWindowManager ipTimeWindowManager) {
        this.ipTimeWindowManager = ipTimeWindowManager;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = getHttpServletRequest(servletRequest);
        String ipAddress = request.getRemoteAddr();
        ipTimeWindowManager.addIpRequest(ipAddress);

        boolean isRestServicePostCall = isRestPublicUserServicePostCall(request);

        // is http request on declared paths?
        if(isRestServicePostCall) {

            if (ipTimeWindowManager.ipAddressReachedLimit(ipAddress)) {
                String message = "The ip address: " + ipAddress + " made more than " + IpTimeWindowManager.MAX_REQUEST_PER_IP_IN_WINDOW + " requests in " + IpTimeWindowManager.WINDOW_SIZE_IN_MINUTES + " minute. Ip was moved to Blacklist.";
                logger.error(message);
                HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
                httpResponse.sendRedirect("/error.jsp");
                //throw new ServletException(message);
            }
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
    

    private HttpServletRequest getHttpServletRequest(ServletRequest servletRequest) throws ServletException {
        if (servletRequest instanceof HttpServletRequest) {
            return (HttpServletRequest) servletRequest;
        } else {
            Request springRequest = (Request) servletRequest.getAttribute("org.springframework.web.context.request.RequestContextListener.REQUEST_ATTRIBUTES");
            if (springRequest instanceof HttpServletRequest) {
                return springRequest;
            } else {
                throw new ServletException("At least the inner request should be a HttpServletRequest");
            }
        }
    }

    private boolean isRestPublicUserServicePostCall(HttpServletRequest request) {
        String requestedUri = request.getRequestURI();
        return Arrays.stream(LIMITED_PATHS).anyMatch(limitedPath -> requestedUri.startsWith(limitedPath));
    }

    @Override
    public void destroy() {
    }

}