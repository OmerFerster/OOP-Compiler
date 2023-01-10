import oop.ex6.main.Result;
import oop.ex6.parser.FileNotFoundException;
import oop.ex6.parser.FileParser;

public class FileParserTest {

    public static void main(String[] args) {
        try {
            FileParser fileParser = new FileParser(args[0]);

            while(fileParser.hasMoreLines()) {
                System.out.println(fileParser.getCurrentLine());
                fileParser.advance();
            }
        } catch (FileNotFoundException exception) {
            System.out.println(Result.IO_ERROR);
            System.err.println(exception.getMessage());
        }
    }
}
