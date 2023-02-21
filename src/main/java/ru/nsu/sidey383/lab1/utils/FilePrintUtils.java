package ru.nsu.sidey383.lab1.utils;

import ru.nsu.sidey383.lab1.model.FileType;
import ru.nsu.sidey383.lab1.model.files.DirectoryFile;
import ru.nsu.sidey383.lab1.model.files.File;
import ru.nsu.sidey383.lab1.options.FilesPrintOptions;

import java.util.Iterator;
import java.util.Stack;

public final class FilePrintUtils {

    private FilePrintUtils() {}

    public static void printFiles(File root, FilesPrintOptions options) {
        Stack<Iterator<File>> dirStack = new Stack<>();
        File now = root;
        do {
            System.out.println("  ".repeat(dirStack.size()) + prettyFileString(now));
            if (now instanceof DirectoryFile dir) {
                dirStack.add(dir.getChildren().listIterator());
            }
            now = null;
            while (!dirStack.isEmpty()) {
                Iterator<File> iterator = dirStack.peek();
                if (iterator.hasNext()) {
                    now = iterator.next();
                    break;
                }
                dirStack.pop();
            }
        } while (now != null);
    }

    public static String prettyFileString(File file) {
        StringBuilder builder = new StringBuilder();
        FileType type = file.getFileType();
        if (type.isLink()) {
            builder.append("*");
        }
        if (type.isDirectory()) {
            builder.append("/");
        }
        builder.append(file.getOriginalPath().getFileName()).append(" [").append(SizeSuffix.BYTE.getSuffix(file.getSize())).append("]");
        return builder.toString();
    }

}
