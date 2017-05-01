package eu.profinit.opendata.ipfilter;


import org.apache.catalina.connector.Request;
import org.apache.log4j.Logger;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;

/**
 *Filter class that is listening on LIMITED_PATHS URL. Throws exception if particular ip address have more requests
 * than limit.
 */

public class IpLimitFilter implements Filter {

    private static String[] LIMITED_PATHS = new String[]{"/"};

    Logger logger = Logger.getLogger(IpLimitFilter.class);

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

        boolean isRestServicePostCall = isRestPublicUserServicePostCall(request);

        if(isRestServicePostCall) {
            String ipAddress = request.getRemoteAddr();
            ipTimeWindowManager.addIpRequest(ipAddress);

            if (ipTimeWindowManager.ipAddressReachedLimit(ipAddress)) {
                String message = "The ip address: " + ipAddress + " made more than " + IpTimeWindowManager.MAX_REQUEST_PER_IP_IN_WINDOW + " requests in " + IpTimeWindowManager.WINDOW_SIZE_IN_MINUTES + " minutes. It's suspicious.";
                logger.error(message);
                throw new ServletException(message);
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
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