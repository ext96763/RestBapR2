package eu.profinit.opendata.ipthrottlingfilter;


import org.apache.catalina.connector.Request;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

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
        String ipAddress = request.getRemoteAddr();
        ipTimeWindowManager.addIpRequest(ipAddress);
        Date date = new Date();
        int addMinuteTime = 1;
        Date waitDate;
        //add minutes
        waitDate  = DateUtils.addMinutes(date, addMinuteTime);

        boolean isRestServicePostCall = isRestPublicUserServicePostCall(request);

        // is http request on declared paths?
        if(isRestServicePostCall) {

            //Does i reached throttling limit?
            if (ipTimeWindowManager.ipAddressReachedThrottlingLimit(ipAddress)) {

                if (date.before(new Date()) && ipTimeWindowManager.ipAddressReachedThrottlingLimit(ipAddress)) {
                    String message = "The ip address: " + ipAddress + " made more than " + IpTimeWindowManager.MAX_REQUEST_PER_IP_Before_Throttling_IN_WINDOW + " requests in " + IpTimeWindowManager.WINDOW_SIZE_IN_MINUTES + " minutes. Your requests will be blocked for: " + addMinuteTime + "minutes. Error time expiring: " + waitDate;
                    logger.error(message);
                    throw new ServletException(message);
                }else if (date.before(waitDate) && ipTimeWindowManager.ipAddressReachedThrottlingLimit(ipAddress)) {
                    ipTimeWindowManager.requestsPerIp.remove(ipAddress, null);
                }
                else if (date.before(waitDate) && !(ipTimeWindowManager.ipAddressReachedThrottlingLimit(ipAddress))) {
                    String message = "The ip address: " + ipAddress + " can now make full number of requests";
                    logger.info(message);
                }
            }
            } else if (ipTimeWindowManager.ipAddressReachedLimit(ipAddress)) {
                String message = "The ip address: " + ipAddress + " made more than " + IpTimeWindowManager.MAX_REQUEST_PER_IP_IN_WINDOW + " requests in " + IpTimeWindowManager.WINDOW_SIZE_IN_MINUTES + " minutes. It's suspicious.";
                logger.error(message);
                throw new ServletException(message);
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