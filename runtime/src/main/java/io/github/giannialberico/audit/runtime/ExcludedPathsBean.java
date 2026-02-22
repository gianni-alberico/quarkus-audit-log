package io.github.giannialberico.audit.runtime;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@ApplicationScoped
public class ExcludedPathsBean {
    private final List<String> excludedPaths = new ArrayList<>();
    private final List<Pattern> excludedPathsRegex = new ArrayList<>();

    public List<String> getExcludedPaths() {
        return excludedPaths;
    }

    public List<Pattern> getExcludedPathsRegex() {
        return excludedPathsRegex;
    }

    synchronized public void setExcludedPaths(List<String> excludedPaths) {
        this.excludedPaths.clear();
        this.excludedPaths.addAll(excludedPaths);
    }

    synchronized public void setExcludedPathsRegex(List<Pattern> excludedPathsRegex) {
        this.excludedPathsRegex.clear();
        this.excludedPathsRegex.addAll(excludedPathsRegex);
    }
}
