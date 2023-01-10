package oop.ex6.main;

import oop.ex6.parser.FileNotFoundException;
import oop.ex6.parser.FileParser;

public class Sjavac {

    public static void main(String[] args) {
        // TODO: check if args.length > 0

        try {
            FileParser fileParser = new FileParser(args[0]);

        } catch (FileNotFoundException exception) {
            System.out.println(Result.IO_ERROR);
            System.err.println(exception.getMessage());
        }
    }
}
