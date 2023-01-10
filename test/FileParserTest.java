import oop.ex6.main.Result;
import oop.ex6.parser.FileParser;

import java.io.IOException;

public class FileParserTest {

    public static void main(String[] args) {
        try {
            FileParser fileParser = new FileParser(args[0]);

            while(fileParser.hasMoreLines()) {
                System.out.println(fileParser.getCurrentLine());
                fileParser.advance();
            }
        } catch (IOException exception) {
            System.out.println(Result.IO_ERROR);
            System.err.println(exception.getMessage());
        }
    }
}
