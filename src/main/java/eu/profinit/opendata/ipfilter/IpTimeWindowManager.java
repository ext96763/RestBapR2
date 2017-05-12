package eu.profinit.opendata.ipfilter;

import com.google.common.collect.LinkedListMultimap;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 *Class that adds or removes IP address from map. Checks if time window has expired, if so removes IP from "BlackList"
 */

public class IpTimeWindowManager {

    //FIXME: Move to properties file
    public static final int WINDOW_SIZE_IN_MINUTES = 1;
    public static final int MAX_REQUEST_PER_IP_IN_WINDOW = 120;
    public static final int MAX_REQUEST_PER_IP_Before_Throttling_IN_WINDOW = 60;
    private long lastEpochMinute;
    private String ip;

    public LinkedListMultimap<String, Long> requestsPerIp;

    public IpTimeWindowManager() {
        requestsPerIp = LinkedListMultimap.create();
        lastEpochMinute = 0;
    }

    public synchronized void addIpRequest(String ipAddress) {
        long epochSecond = LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond();
        requestsPerIp.put(ipAddress, epochSecond);
        ip = ipAddress;

        long epochMinute = epochSecond - (epochSecond % 60);
        if (epochMinute > lastEpochMinute) {
            lastEpochMinute = epochMinute;
            cleanExpiredRequests();
        }
    }

    private void cleanExpiredRequests() {
        long expiredEpochMinute = lastEpochMinute - (WINDOW_SIZE_IN_MINUTES * 60);
        List<String> ipList = new ArrayList<>();

        for (String ipAddress : requestsPerIp.keySet()) {
            List<Long> requests = requestsPerIp.get(ipAddress);
            List<Long> requestsHelpList = new ArrayList<>();

            for (Long request : requests) {
                if (request < expiredEpochMinute) {
                    requestsHelpList.add(request);
                }
            }
            for (Long request : requestsHelpList) {
                requests.remove(request);
            }

            if (requests.isEmpty()) {
                ipList.add(ipAddress);
            }

        }
        for (String ip : ipList) {
           requestsPerIp.removeAll(ip);
        }
    }

    public synchronized boolean ipAddressReachedLimit(String ipAddress) {
        int amountRequests = requestsPerIp.get(ipAddress).size();
        return (amountRequests > MAX_REQUEST_PER_IP_IN_WINDOW);
    }

    public synchronized boolean ipAddressReachedThrottlingLimit(String ipAddress){
        int amountRequests = requestsPerIp.get(ipAddress).size();
        return (amountRequests > MAX_REQUEST_PER_IP_Before_Throttling_IN_WINDOW);
    }

    public  String getIp () {
        return ip;
    }
}