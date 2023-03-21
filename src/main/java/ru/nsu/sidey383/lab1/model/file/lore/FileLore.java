package ru.nsu.sidey383.lab1.model.file.lore;

import org.jetbrains.annotations.NotNull;
import ru.nsu.sidey383.lab1.model.file.FileType;
import ru.nsu.sidey383.lab1.model.file.exception.*;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;


/**
 * Дополнительные данные для {@link ru.nsu.sidey383.lab1.model.file.File}.
 */
public interface FileLore {


    long originalSize();

    @NotNull
    Path originalPath();

    long resolvedSize();

    @NotNull
    Path resolvedPath();

    @NotNull
    FileType fileType();

    /**
     * Фабричный метод для создания {@link FileLore}.
     * <p> Перед созданием объекта разрешает путь до файла, а для ссылок переходит по ссылке с помощью {@link Path#toRealPath(LinkOption...)}.
     *
     * @throws PathUnsupportedOperationException если невозможно получить атрибуты файла.
     * @throws PathSecurityException если нет прав на работу с данным файлом.
     * @throws PathFileSystemException при ошибке файловой системы.
     * @throws IOException если файл не существует или в случае I/O exception.
     *
     * @see Path#toRealPath(LinkOption...)
     * @see Files#readAttributes(Path, Class, LinkOption...)
     * @see Files#size(Path)
     */
    static FileLore createFileLore(@NotNull Path path) throws PathException, IOException {
        try {
            Path originalPath;
            try {
                originalPath = path.toRealPath(LinkOption.NOFOLLOW_LINKS);
            } catch (NotDirectoryException e1) {
                originalPath = path.toRealPath();
            }


            BasicFileAttributes originalAttributes = Files.readAttributes(originalPath, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
            long originalSize = originalAttributes.size();
            FileType originalType = FileType.toSimpleType(originalAttributes);
            if (originalSize < 0)
                originalSize = 0;

            if (originalType.isLink()) {

                Path resolvedPath = path.toRealPath();
                BasicFileAttributes resolvedAttributes = Files.readAttributes(resolvedPath, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
                long resolvedSize = resolvedAttributes.size();
                FileType resolvedType = FileType.toSimpleType(resolvedAttributes);

                if (resolvedSize < 0)
                    resolvedSize = 0;

                return new DefaultFileLore(resolvedType == FileType.UNDEFINED ? FileType.UNDEFINED_LINK : resolvedType.toLink(), originalPath, originalSize, resolvedPath, resolvedSize);
            } else {
                return new DefaultFileLore(originalType, originalPath, originalSize, originalPath, originalSize);
            }
            // CR: merge cases (if messages are convenient)?
        } catch (SecurityException e) {
            throw new PathSecurityException(path, e);
        } catch (UnsupportedOperationException e) {
            throw new PathUnsupportedOperationException(path, e);
        } catch (FileSystemException e) {
            throw new PathFileSystemException(path, e);
        }
    }

    /**
     * Хэш должен не зависеть от размера файла.
     */
    @Override
    int hashCode();

    /**
     * Сравнение должно не зависеть от размера файла.
     */
    @Override
    boolean equals(Object obj);

}
