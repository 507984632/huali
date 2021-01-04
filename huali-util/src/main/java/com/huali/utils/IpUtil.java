package com.huali.utils;


import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * @author lvws
 * @since 2019/5/23.
 */
public class IpUtil {

    private static final String UNKNOWN = "unknown";
    private static final String LOCALHOST = "127.0.0.1";
    private static final int IP_LENGTH = 15;

    /**
     * 获取客户端IP
     */
    public static String getHost(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        if (LOCALHOST.equals(ip)) {
            try { // 根据网卡取本机配置的IP
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }

        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.length() > IP_LENGTH) {
            int index = ip.indexOf(",");
            if (index > 0) {
                ip = ip.substring(0, index);
            }
        }
        return ip;
    }

    /**
     * 获取主机MAC地址
     *
     * @return 主机MAC地址
     * @throws UnknownHostException e
     * @throws SocketException      e
     */
    public static String getHostMacAddress() throws UnknownHostException, SocketException {
        NetworkInterface netInterface = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
        byte[] macAddr = netInterface.getHardwareAddress();

        StringBuilder address = new StringBuilder();
        for (byte b : macAddr) {
            String postion = Integer.toHexString(b & 0xff);
            if (postion.length() == 1) {
                postion = "0" + postion;
            }
            address.append(postion).append('-');
        }
        return address.deleteCharAt(address.length() - 1).toString();
    }

}
