package ru.nsu.sidey383.lab1.model.file.lore;

import ru.nsu.sidey383.lab1.model.file.FileType;

import java.nio.file.Path;
import java.util.Objects;

public record DefaultFileLore(FileType fileType,
                              Path originalPath,
                              long originalSize,
                              Path resolvedPath,
                              long resolvedSize) implements FileLore {

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof FileLore lore) && originalPath.equals(lore.originalPath()) && resolvedPath.equals(lore.resolvedPath());
    }

    @Override
    public String toString() {
        return "DefaultFileLore{" +
                "fileType=" + fileType +
                ", originalSize=" + originalSize +
                ", originalPath=" + originalPath +
                ", resolvedSize=" + resolvedSize +
                ", resolvedPath=" + resolvedPath +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(originalPath, resolvedPath);
    }

}
