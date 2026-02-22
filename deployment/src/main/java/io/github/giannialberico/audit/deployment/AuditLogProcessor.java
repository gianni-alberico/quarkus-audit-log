package io.github.giannialberico.audit.deployment;

import io.github.giannialberico.audit.runtime.AuditLogFilter;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.resteasy.reactive.spi.ContainerRequestFilterBuildItem;
import io.quarkus.resteasy.reactive.spi.ContainerResponseFilterBuildItem;

class AuditLogProcessor {

    private static final String FEATURE = "audit-log";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    ContainerRequestFilterBuildItem registerAsRequestFilterBean() {
        return new ContainerRequestFilterBuildItem.Builder(AuditLogFilter.class.getName())
                .setRegisterAsBean(true)
                .build();
    }

    @BuildStep
    ContainerResponseFilterBuildItem registerAsResponseFilterBean() {
        return new ContainerResponseFilterBuildItem.Builder(AuditLogFilter.class.getName())
                .setRegisterAsBean(true)
                .build();
    }
}
