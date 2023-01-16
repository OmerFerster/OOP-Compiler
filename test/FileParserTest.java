import oop.ex6.checker.Checker;
import oop.ex6.checker.CheckerException;
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

            fileParser.reset();

            Checker checker = new Checker(fileParser);
            checker.check();
        } catch (CheckerException exception) {
            System.out.println(Result.ILLEGAL.getCode());
            System.err.println(exception.getMessage());
        } catch (IOException exception) {
            System.out.println(Result.IO_ERROR.getCode());
            System.err.println(exception.getMessage());
        }
    }
}
