package eu.profinit.opendata.throttlingfilter;



import eu.profinit.opendata.controller.MainController;
import eu.profinit.opendata.ipfilter.IpTimeWindowManager;
import org.apache.catalina.connector.Request;
import org.apache.commons.lang.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

/**
 *Filter class that is listening on LIMITED_PATHS URL. Blocks REQ with ip that hav more REQs than MAX_REQUEST_PER_IP_Before_Throttling_IN_WINDOW
 *for a time that is in variable addSecondsTimeTillNextReq. If REQs continuing to come throws exception with message.
 */

public class ThrottlingFilter implements Filter {


    private static String[] LIMITED_PATHS = new String[]{"/"};
    static HashMap<String, ipObject> dateMap = new HashMap<>();
    private static int addSecondsTimeTillNextReq = 30;


    //Object for Map to find throttling ip.
    class ipObject {
        String ip;
        Date date;
        Integer count;
    }

    private static Logger logger = LogManager.getLogger(ThrottlingFilter.class);

    IpTimeWindowManager ipTimeWindowManager;

    public ThrottlingFilter(IpTimeWindowManager ipTimeWindowManager) {
        this.ipTimeWindowManager = ipTimeWindowManager;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = getHttpServletRequest(servletRequest);
        String ipAddress = request.getRemoteAddr();
        boolean isRestServicePostCall = isRestPublicUserServicePostCall(request);


        // is http request on declared paths?
        if (isRestServicePostCall) {

            if (dateMap.get(ipAddress) == null) {

                ipObject obj = new ipObject();
                obj.count = 1;
                obj.ip = ipAddress;
                obj.date = new Date();
                dateMap.put(ipAddress, obj);
            } else {
                dateMap.get(ipAddress).count++;
            }

                if (dateMap.get(ipAddress).count > IpTimeWindowManager.MAX_REQUEST_PER_IP_Before_Throttling_IN_WINDOW) {
                    Date dateOfBlockedReq = new Date();
                    String message = "The ip address: " + ipAddress + " made more than " + IpTimeWindowManager.MAX_REQUEST_PER_IP_Before_Throttling_IN_WINDOW + " requests in " + IpTimeWindowManager.WINDOW_SIZE_IN_MINUTES + " minute. Number of possible requests in " + IpTimeWindowManager.WINDOW_SIZE_IN_MINUTES + " minute will be limited till" + DateUtils.addSeconds(dateMap.get(ipAddress).date, addSecondsTimeTillNextReq);
                    logger.error(message);
                    //throw new ServletException(message);
                    if(dateOfBlockedReq.after(DateUtils.addSeconds(dateMap.get(ipAddress).date, addSecondsTimeTillNextReq))){
                        dateMap.remove(ipAddress);
                    }else {
                        String message1 = "The ip address: " + ipAddress + " made more than " + IpTimeWindowManager.MAX_REQUEST_PER_IP_Before_Throttling_IN_WINDOW + " requests in " + IpTimeWindowManager.WINDOW_SIZE_IN_MINUTES + " minute. Number of possible requests in " + IpTimeWindowManager.WINDOW_SIZE_IN_MINUTES + " minute will be limited till " + DateUtils.addSeconds(dateMap.get(ipAddress).date, addSecondsTimeTillNextReq);
                        logger.error(message1);
                        throw new ServletException(message1);
                    }
                }
            }
            filterChain.doFilter(servletRequest, servletResponse);
        }

    private HttpServletRequest getHttpServletRequest(ServletRequest request) throws ServletException {
        if (request instanceof HttpServletRequest) {
            return (HttpServletRequest) request;
        } else {
            Request springRequest = (Request) request.getAttribute("org.springframework.web.context.request.RequestContextListener.REQUEST_ATTRIBUTES");
            if (springRequest instanceof HttpServletRequest) {
                return springRequest;
            } else {
                throw new ServletException("At least the inner request should be a HttpServletRequest ");
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