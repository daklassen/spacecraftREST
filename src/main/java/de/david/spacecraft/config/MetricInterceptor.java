package de.david.spacecraft.config;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class MetricInterceptor extends HandlerInterceptorAdapter {

    private Meter requestMeter;

    @Autowired
    public MetricInterceptor(MetricRegistry metricRegistry) {
        requestMeter = metricRegistry.meter("requests");
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        requestMeter.mark();
        return super.preHandle(request, response, handler);
    }
}
