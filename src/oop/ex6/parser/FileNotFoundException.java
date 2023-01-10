package oop.ex6.parser;

import java.io.IOException;

public class FileNotFoundException extends IOException {

    FileNotFoundException(String message) {
        super(message);
    }
}
