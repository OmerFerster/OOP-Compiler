package oop.ex6.parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileParser {

    private static final String FILE_NOT_FOUND_MESSAGE = "Couldn't find source file %s!";
    private static final String IO_ERROR_MESSAGE = "An error occurred while reading file %s!";

    // TODO: might be better to use a linked list
    private final List<String> lines;

    private int currentLineIndex;

    public FileParser(String sourceFile) throws IOException {
        this.lines = new ArrayList<>();

        this.currentLineIndex = 0;

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(sourceFile))) {
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                this.lines.add(line);
            }
        } catch(FileNotFoundException exception) {
            throw new FileNotFoundException(String.format(FILE_NOT_FOUND_MESSAGE, sourceFile));
        } catch(IOException exception) {
            throw new IOException(String.format(IO_ERROR_MESSAGE, sourceFile));
        }
    }


    public boolean hasMoreLines() {
        return (this.currentLineIndex < this.lines.size() - 1);
    }

    public String getCurrentLine() {
        if ((this.currentLineIndex >= this.lines.size()) || (this.currentLineIndex < 0)) {
            return null;
        }

        return this.lines.get(this.currentLineIndex);
    }

    public void advance() {
        this.currentLineIndex++;
    }

    public void reset() {
        this.currentLineIndex = 0;
    }
}
