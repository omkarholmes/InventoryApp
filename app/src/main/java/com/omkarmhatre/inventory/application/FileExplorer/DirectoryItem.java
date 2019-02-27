package com.omkarmhatre.inventory.application.FileExplorer;

public class DirectoryItem {
    public String file;
    public int icon;

    public DirectoryItem(String file, Integer icon) {
        this.file = file;
        this.icon = icon;
    }

    @Override
    public String toString() {
        return file;
    }
}
