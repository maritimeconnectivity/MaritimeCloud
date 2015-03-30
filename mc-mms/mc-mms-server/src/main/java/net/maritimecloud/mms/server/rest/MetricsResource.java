package net.maritimecloud.mms.server.rest;

import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricRegistry;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * REST endpoint for returning metrics about the state of the server
 */
@Path("/metrics")
public class MetricsResource {

    final MetricRegistry metrics;

    /**
     * Constructor
     * @param metrics the metrics registry gets injected
     */
    public MetricsResource(MetricRegistry metrics) {
        this.metrics = metrics;

        // Enable JMX monitoring
        JmxReporter reporter = JmxReporter.forRegistry(metrics).build();
        reporter.start();
    }

    @GET
    @Path("/all")
    @Produces("text/plain;charset=UTF-8")
    public MetricRegistry all() {
        return metrics;
    }

}
