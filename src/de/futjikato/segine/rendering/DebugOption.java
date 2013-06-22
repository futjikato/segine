package de.futjikato.segine.rendering;

/**
 * @author moritzspindelhirn
 * @todo Documentation
 * @category de.futjikato.segine.rendering
 */
public enum DebugOption {

    NONE(""),

    WARN("[WARN] "),

    INFO("[INFO] ");

    private String logPrefix;

    DebugOption(String logPrefix) {
        this.logPrefix = logPrefix;
    }

    public String getLogPrefix() {
        return logPrefix;
    }

}
