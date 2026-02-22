package io.github.giannialberico.audit.runtime;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@RequestScoped
public class AuditLogFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditLogFilter.class);
    private final long startTime = System.nanoTime();
    private final String requestId = UUID.randomUUID().toString();

    @Override
    public void filter(ContainerRequestContext requestContext) {
        LOGGER.info("[{}] Request {} {}",
                requestId,
                requestContext.getMethod(),
                requestContext.getUriInfo().getPath());
    }

    @Override
    public void filter(ContainerRequestContext requestContext,
                       ContainerResponseContext responseContext) {

        long durationMs = (System.nanoTime() - startTime) / 1_000_000;

        LOGGER.info("[{}] Response {} {} status={} duration={}ms",
                requestId,
                requestContext.getMethod(),
                requestContext.getUriInfo().getPath(),
                responseContext.getStatus(),
                durationMs);
    }
}
