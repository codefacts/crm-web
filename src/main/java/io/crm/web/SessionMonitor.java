package io.crm.web;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by someone on 09/11/2015.
 */
final public class SessionMonitor {
    public static final AtomicInteger sessionCount = new AtomicInteger(0);
}
