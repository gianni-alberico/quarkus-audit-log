package io.github.giannialberico.audit.runtime;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.List;

@ConfigMapping(prefix = "quarkus.audit-log")
@ConfigRoot(phase = ConfigPhase.RUN_TIME)
public interface AuditLogConfig {

    /**
     * Enables or disables the HTTP audit logging extension.
     * <p>
     * When enabled, all incoming HTTP requests and their corresponding responses
     * are logged, including method, path, status code and execution duration.
     * </p>
     * <p>
     * Disabling this property completely turns off the audit logging filter.
     * </p>
     */
    @WithDefault("true")
    boolean enabled();

    /**
     * Enables or disables logging of HTTP request and response bodies.
     * <p>
     * When enabled, the content of request and response bodies will be logged
     * along with the usual audit information (method, path, status, duration).
     * </p>
     * <p>
     * Be cautious when enabling this option in production environments:
     * request or response bodies may contain sensitive information such as
     * passwords, tokens, or personal data.
     * </p>
     * <p>
     * Default value: {@code false}.
     * </p>
     */
    @ConfigProperty(name = "log-body")
    @WithDefault("false")
    boolean logBody();

    /**
     * Specifies a list of HTTP paths to exclude from audit logging.
     * <p>
     * Any request whose path matches one of these entries will not be logged
     * by the audit logging filter. This can be useful to avoid logging
     * high-traffic or sensitive endpoints such as health checks or metrics.
     * </p>
     * <p>
     * Paths should be provided as strings, for example:
     * <pre>
     * excluded-paths=/health,/metrics,/actuator/*
     * </pre>
     * Wildcards (*) are supported at the end of a path segment.
     * </p>
     * <p>
     * Default value: empty list (all paths are logged).
     * </p>
     */
    @ConfigProperty(name = "excluded-paths")
    @WithDefault("")
    List<String> excludedPaths();
}
