package io.github.giannialberico.audit.runtime;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.regex.Pattern;

@RequestScoped
public class AuditLogFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditLogFilter.class);
    private final long startTime = System.nanoTime();
    private final String requestId = UUID.randomUUID().toString();

    private boolean isPathExcluded = false;

    @Inject
    AuditLogConfig config;

    @Inject
    ExcludedPathsBean  excludedPathsBean;

    @Inject
    Serializer serializer;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        if (!config.enabled()) {
            return;
        }

        String path = requestContext.getUriInfo().getPath();

        if (excludedPathsChanged()) {
            reloadExcludedPaths();
        }

        if (isPathExcluded(path)) {
            isPathExcluded = true;
            return;
        }

        String method = requestContext.getMethod();
        String body = readRequestBody(requestContext);

        LOGGER.info("[{}] Request {} {} {}", requestId, method, path, formatBody(body));
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        if (!config.enabled() || isPathExcluded) {
            return;
        }

        String method = requestContext.getMethod();
        String path = requestContext.getUriInfo().getPath();
        int status = responseContext.getStatus();
        long durationMs  = (System.nanoTime() - startTime) / 1_000_000;
        String body = readResponseBody(responseContext);

        LOGGER.info("[{}] Response {} {} status={} duration={}ms {}",
                requestId, method, path, status, durationMs, formatBody(body));
    }

    private String readRequestBody(ContainerRequestContext requestContext) {
        if (!config.logBody()) {
            return null;
        }
        try {
            byte[] bytes = requestContext.getEntityStream().readAllBytes();
            requestContext.setEntityStream(new ByteArrayInputStream(bytes));
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOGGER.warn("[{}] Failed to read request body", requestId, e);
            return null;
        }
    }

    private String readResponseBody(ContainerResponseContext responseContext) {
        if (!config.logBody() || !responseContext.hasEntity()) {
            return null;
        }
        return serializer.serialize(responseContext.getEntity());
    }

    private String formatBody(String body) {
        if (config.logBody() && isNonNullNonEmpty(body)) {
            return " body=" + body;
        }

        return "";
    }

    private boolean isNonNullNonEmpty(String value) {
        return value != null && !value.isEmpty();
    }

    private boolean isPathExcluded(String path) {
        for (Pattern excludedPathRegex : excludedPathsBean.getExcludedPathsRegex()) {
            if(excludedPathRegex.matcher(path).matches()){
                return true;
            }
        }
        return false;
    }

    private boolean excludedPathsChanged() {
        return config.excludedPaths().hashCode() != excludedPathsBean.getExcludedPaths().hashCode();
    }

    private void reloadExcludedPaths() {
        excludedPathsBean.setExcludedPaths(config.excludedPaths());
        excludedPathsBean.setExcludedPathsRegex(
                config.excludedPaths()
                        .stream()
                        .map(p -> Pattern.compile(p.replace("*", ".*")))
                        .toList()
        );
    }
}