import java.util.List;


public interface ASTNode {
    //返回与它关联的token的值
    String TokenLiteral();

    //添加String方法便于调试
    String string();
}


class Statement implements ASTNode {
    private Token Token;

    Statement() {
    }

    public Token getToken() {
        return Token;
    }

    public void setToken(Token token) {
        Token = token;
    }

    public Statement(Token token) {
        Token = token;
    }

    @Override
    public String TokenLiteral() {
        return Token.getLiteral();
    }

    @Override
    public String string() {
        return null;
    }
}

class Expression implements ASTNode {
    private Token Token;

    public Expression() {
    }

    public Token getToken() {
        return Token;
    }

    public void setToken(Token token) {
        Token = token;
    }

    public Expression(Token token) {
        Token = token;
    }

    @Override
    public String TokenLiteral() {
        return Token.getLiteral();
    }

    @Override
    public String string() {
        return Token.getLiteral();
    }
}

//Node的第一个实现
class Program implements ASTNode {
    private List<Statement> statements;

    public List<Statement> getStatements() {
        return statements;
    }

    public void setStatements(List<Statement> statements) {
        this.statements = statements;
    }

    public Program(List<Statement> statements) {
        this.statements = statements;
    }

    @Override
    public String TokenLiteral() {
        if (statements.size() > 0) {
            return statements.get(0).TokenLiteral();
        } else {
            return "";
        }
    }

    @Override
    public String string() {
        StringBuilder out = new StringBuilder();
        for (Statement statement : statements) {
            out.append(statement.string());
        }
        return out.toString();
    }
}

//标识符
class Identifier extends Expression {
    private Token Token;
    private String Value;

    public Identifier(Token token, String value) {
        Token = token;
        Value = value;
    }

    public Token getToken() {
        return Token;
    }

    public void setToken(Token token) {
        Token = token;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    @Override
    public String TokenLiteral() {
        return Token.getLiteral();
    }

    @Override
    public String string() {
        return Value;
    }
}

//Var语句
class VarStatement extends Statement {
    private Token Token;
    private Identifier Name;
    private Expression Value;

    public VarStatement(Token token, Identifier name, Expression value) {
        Token = token;
        Name = name;
        Value = value;
    }

    public Token getToken() {
        return Token;
    }

    public void setToken(Token token) {
        Token = token;
    }

    public Identifier getName() {
        return Name;
    }

    public void setName(Identifier name) {
        Name = name;
    }

    public Expression getValue() {
        return Value;
    }

    public void setValue(Expression value) {
        Value = value;
    }

    @Override
    public String TokenLiteral() {
        return Token.getLiteral();
    }

    @Override
    public String string() {
        String out = "";
        out = out + TokenLiteral() + " " + Name.string() + " = ";
        if (Value != null) {
            out += Value.string();
        }
        out += ";";
        return out;
    }
}

//Return返回值
class ReturnStatement extends Statement {
    private Token Token;
    private Expression Value;

    public ReturnStatement(Token token, Expression value) {
        Token = token;
        Value = value;
    }

    @Override
    public Token getToken() {
        return Token;
    }

    @Override
    public void setToken(Token token) {
        Token = token;
    }

    public Expression getValue() {
        return Value;
    }

    public void setValue(Expression value) {
        Value = value;
    }

    @Override
    public String TokenLiteral() {
        return Token.getLiteral();
    }
    @Override
    public String string() {
        String out = "";
        out = out + TokenLiteral() + " ";
        if (Value != null) {
            out += Value.string();
        }
        out += ";";
        return out;
    }
}

class ExpressionStatement extends Statement {
    private Token Token;
    private Expression Value;

    public ExpressionStatement(Token token, Expression value) {
        Token = token;
        Value = value;
    }

    @Override
    public String TokenLiteral() {
        return Token.getLiteral();
    }
    @Override
    public String string() {
       if(Value!=null)
           return Value.string();
        return "";
    }
}
