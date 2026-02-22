package io.github.giannialberico.audit.runtime;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

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
}
