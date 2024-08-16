package com.filesorter;

public enum DirectoryPath {
    HOME("./HOME"),
    DEV("./DEV"),
    TEST("./TEST");

    private final String path;

    DirectoryPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
