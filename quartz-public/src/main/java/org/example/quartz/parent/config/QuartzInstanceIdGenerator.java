package org.example.quartz.parent.config;

import org.quartz.SchedulerException;
import org.quartz.spi.InstanceIdGenerator;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * This generator generate the instance id with the format "ip:port", like 192.168.10.2:8080
 */
@Configuration
public class QuartzInstanceIdGenerator implements ApplicationContextAware, InstanceIdGenerator {



    @Override
    public String generateInstanceId() throws SchedulerException {
        return getIp() + ":" + applicationContext.getEnvironment().getProperty("server.port");
    }

    /**
     * Get host ip
     * @return the prefect ipv4 ip, else return loop back ip(127.0.0.1) if no any host ip found.
     */
    public static String getIp() {
        String ip = "127.0.0.1";
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                if (iface.isLoopback() || !iface.isUp()) {
                    continue;
                }
                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    if (addr instanceof Inet6Address) {
                        continue;
                    }
                    ip = addr.getHostAddress();
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        return ip;
    }

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        QuartzInstanceIdGenerator.applicationContext = applicationContext;
    }
}
