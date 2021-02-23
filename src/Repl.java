import java.util.Scanner;

public class Repl {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("User> ");
            String line = scanner.nextLine();
            if (line == null) {
                continue;
            }
            if(line.equals("exit()"))
                break;

            Lexer lexer = new Lexer(line);
            for(Token token= lexer.nextToken();token.getType()!=TokenType.EOF;token=lexer.nextToken()){
                System.out.println(token.getLiteral());
            }
            System.out.println("**********************");
            lexer = new Lexer(line);
            Parser parser=new Parser(lexer);
            Program program = parser.ParseProgram();
            System.out.println(program.string());
        }
    }
}
