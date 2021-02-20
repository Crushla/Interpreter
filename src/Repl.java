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
            Lexer lexer = new Lexer(line);
            for(Token token=lexer.nextToken();token.getType()!=Token.TokenType.EOF;token= lexer.nextToken()){
                System.out.println(token.getType()+token.getLiteral());
            }
        }
    }
}
