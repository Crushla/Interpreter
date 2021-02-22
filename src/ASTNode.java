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

//表达式
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
        if (Value != null)
            return Value.string();
        return "";
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

//Integer类型
class IntegerLiteral extends Expression {
    Token Token;
    int Value;

    public IntegerLiteral(Token token, int value) {
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

    public int getValue() {
        return Value;
    }

    public void setValue(int value) {
        Value = value;
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


class PrefixExpression extends Expression {
    Token Token;

    String Operator;
    Expression Right;

    public PrefixExpression(Token token, String operator, Expression right) {
        Token = token;
        Operator = operator;
        Right = right;
    }

    @Override
    public Token getToken() {
        return Token;
    }

    @Override
    public void setToken(Token token) {
        Token = token;
    }

    public String getOperator() {
        return Operator;
    }

    public void setOperator(String operator) {
        Operator = operator;
    }

    public Expression getRight() {
        return Right;
    }

    public void setRight(Expression right) {
        Right = right;
    }

    @Override
    public String TokenLiteral() {
        return Token.getLiteral();
    }

    //前缀操作符有-和！
    @Override
    public String string() {
        String out = "";
        out = out + "(" + Operator + Right.string() + ")";
        return out;
    }
}

class InfixExpression extends Expression {
    Token Token;
    Expression Left;
    String Operator;
    Expression Right;

    public InfixExpression(Token token, Expression left, String operator, Expression right) {
        Token = token;
        Left = left;
        Operator = operator;
        Right = right;
    }

    public Expression getLeft() {
        return Left;
    }

    public void setLeft(Expression left) {
        Left = left;
    }

    @Override
    public Token getToken() {
        return Token;
    }

    @Override
    public void setToken(Token token) {
        Token = token;
    }

    public String getOperator() {
        return Operator;
    }

    public void setOperator(String operator) {
        Operator = operator;
    }

    public Expression getRight() {
        return Right;
    }

    public void setRight(Expression right) {
        Right = right;
    }

    @Override
    public String TokenLiteral() {
        return Token.getLiteral();
    }

    //中缀表达式
    @Override
    public String string() {
        String out = "";
        out = out + "(" + Left.string() + " " + Operator + " " + Right.string() + ")";
        return out;
    }
}

class Boolean extends Expression {
    Token Token;
    boolean Value;

    public Boolean(Token token, boolean value) {
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

    public boolean getValue() {
        return Value;
    }

    public void setValue(boolean value) {
        Value = value;
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

class IFExpression extends Expression {
    Token Token;
    Expression Condition;
    BlockStatement Consequence;
    BlockStatement Alternative;

    public IFExpression(Token token, Expression condition, BlockStatement consequence, BlockStatement alternative) {
        Token = token;
        Condition = condition;
        Consequence = consequence;
        Alternative = alternative;
    }

    @Override
    public Token getToken() {
        return Token;
    }

    @Override
    public void setToken(Token token) {
        Token = token;
    }

    public Expression getCondition() {
        return Condition;
    }

    public void setCondition(Expression condition) {
        Condition = condition;
    }

    public BlockStatement getConsequence() {
        return Consequence;
    }

    public void setConsequence(BlockStatement consequence) {
        Consequence = consequence;
    }

    public BlockStatement getAlternative() {
        return Alternative;
    }

    public void setAlternative(BlockStatement alternative) {
        Alternative = alternative;
    }

    @Override
    public String TokenLiteral() {
        return Token.getLiteral();
    }

    @Override
    public String string() {
        String out = "";
        out = out + "if" + Condition.string() + " " + Consequence.string();
        if (Alternative != null) {
            out = out + "else " + Alternative.string();
        }
        return out;
    }
}

class BlockStatement extends Statement {
    Token Token;
    List<Statement> Statements;

    public BlockStatement(Token token, List<Statement> statements) {
        Token = token;
        Statements = statements;
    }

    @Override
    public Token getToken() {
        return Token;
    }

    @Override
    public void setToken(Token token) {
        Token = token;
    }

    public List<Statement> getStatements() {
        return Statements;
    }

    public void setStatements(List<Statement> statements) {
        Statements = statements;
    }

    @Override
    public String TokenLiteral() {
        return Token.getLiteral();
    }

    @Override
    public String string() {
        StringBuilder out = new StringBuilder();
        for (Statement statement : Statements) {
            out.append(statement.string());
        }
        return out.toString();
    }
}