import java.util.HashMap;
import java.util.Scanner;

public class Repl {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        Environment environment=new Environment(new HashMap<>(),null);
        TypeNULL Null=new TypeNULL();
        while (true) {
            System.out.print("User> ");
            String line = scanner.nextLine();
            if (line == null) {
                continue;
            }
            if(line.equals("exit()"))
                break;
            Lexer lexer = new Lexer(line);
            Parser parser=new Parser(lexer);
            Program program = parser.ParseProgram();
            Evaluation evaluation=new Evaluation();
            Object eval = evaluation.Eval(program,environment);
            if(!eval.ObjectType().equals(ObjectType.NULL_OBJ)){
                System.out.println(eval.Inspect());
            }
        }
    }
}
