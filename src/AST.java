import com.sun.deploy.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

enum NodeType {
    Program,
    ExpressionStatement,
    IntegerLiteral,
    Boolean,
    StringLiteral,
    PrefixExpression,
    InfixExpression,
    BlockStatement,
    IfExpression,
    ReturnStatement,
    VarStatement,
    Identifier,
    FunctionLiteral,
    CallExpression,
    ArrayLiteral,
    HashLiteral,
    IndexExpression
}

public interface AST {
    //返回与它关联的token的值
    String TokenLiteral();

    NodeType NodeType();

    //添加String方法便于调试
    String string();
}


class Statement implements AST {
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
    public NodeType NodeType() {
        return null;
    }

    @Override
    public String string() {
        return null;
    }
}

class Expression implements AST {
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
    public NodeType NodeType() {
        return null;
    }

    @Override
    public String string() {
        return Token.getLiteral();
    }
}

//Node的第一个实现
class Program implements AST {
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
    public NodeType NodeType() {
        return NodeType.Program;
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

//变量
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
    public NodeType NodeType() {
        return NodeType.Identifier;
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
    public NodeType NodeType() {
        return NodeType.ExpressionStatement;
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

    @Override
    public NodeType NodeType() {
        return NodeType.VarStatement;
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
    public NodeType NodeType() {
        return NodeType.ReturnStatement;
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

class PrefixExpression extends Expression {
    private Token Token;
    private String Operator;
    private Expression Right;

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

    @Override
    public NodeType NodeType() {
        return NodeType.PrefixExpression;
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
    private Token Token;
    private Expression Left;


    private String Operator;
    private Expression Right;

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

    @Override
    public NodeType NodeType() {
        return NodeType.InfixExpression;
    }

    //中缀表达式
    @Override
    public String string() {
        String out = "";
        out = out + "(" + Left.string() + " " + Operator + " " + Right.string() + ")";
        return out;
    }
}

//Integer类型
class IntegerLiteral extends Expression {
    private Token Token;
    private int Value;

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
    public NodeType NodeType() {
        return NodeType.IntegerLiteral;
    }

    @Override
    public String string() {
        return Token.getLiteral();
    }
}

class Boolean extends Expression {
    private Token Token;
    private boolean Value;

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
    public NodeType NodeType() {
        return NodeType.Boolean;
    }

    @Override
    public String string() {
        return Token.getLiteral();
    }
}

class StringLiteral extends Expression {
    private Token Token;
    private String Value;

    public StringLiteral(Token token, String value) {
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

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    @Override
    public NodeType NodeType() {
        return NodeType.StringLiteral;
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

//数组
class ArrayLiteral extends Expression {
    private Token Token;
    private List<Expression> Elements;

    public ArrayLiteral(Token token, List<Expression> elements) {
        Token = token;
        Elements = elements;
    }

    @Override
    public Token getToken() {
        return Token;
    }

    @Override
    public void setToken(Token token) {
        Token = token;
    }

    public List<Expression> getElements() {
        return Elements;
    }

    public void setElements(List<Expression> elements) {
        Elements = elements;
    }

    @Override
    public String TokenLiteral() {
        return Token.getLiteral();
    }

    @Override
    public NodeType NodeType() {
        return NodeType.ArrayLiteral;
    }

    @Override
    public String string() {
        String out = "";
        List<String> list = new ArrayList<>();
        for (Expression e : Elements) list.add(e.string());
        out = out + "[" + StringUtils.join(list, ",") + "]";
        return out;
    }
}
//hash
class HashLiteral extends Expression{
    private Token Token;
    private Map<Expression,Expression> map;

    public HashLiteral(Token token, Map<Expression, Expression> map) {
        Token = token;
        this.map = map;
    }

    @Override
    public Token getToken() {
        return Token;
    }

    @Override
    public void setToken(Token token) {
        Token = token;
    }

    public Map<Expression, Expression> getMap() {
        return map;
    }

    public void setMap(Map<Expression, Expression> map) {
        this.map = map;
    }

    @Override
    public String TokenLiteral() {
        return Token.getLiteral();
    }

    @Override
    public NodeType NodeType() {
        return NodeType.HashLiteral;
    }

    @Override
    public String string() {
        String out="";
        List<String>list=new ArrayList<>();
        for (Map.Entry<Expression,Expression> entry:map.entrySet()){
            String str=entry.getKey().string()+":"+entry.getValue().string();
            list.add(str);
        }
        out = out + "{" + StringUtils.join(list, ",") + "}";
        return out;
    }
}
class IFExpression extends Expression {
    private Token Token;
    private Expression Condition;
    private BlockStatement Consequence;
    private BlockStatement Alternative;

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
    public NodeType NodeType() {
        return NodeType.IfExpression;
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
    private Token Token;
    private List<Statement> Statements;

    public BlockStatement(Token token, List<Statement> statements) {
        Token = token;
        Statements = statements;
    }

    @Override
    public NodeType NodeType() {
        return NodeType.BlockStatement;
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

//函数
class FunctionLiteral extends Expression {
    private Token Token;
    private List<Identifier> Parameters;
    private BlockStatement Body;

    public FunctionLiteral(Token token, List<Identifier> parameters, BlockStatement body) {
        Token = token;
        Parameters = parameters;
        Body = body;
    }

    @Override
    public Token getToken() {
        return Token;
    }

    @Override
    public void setToken(Token token) {
        Token = token;
    }

    public List<Identifier> getParameters() {
        return Parameters;
    }

    public void setParameters(List<Identifier> parameters) {
        Parameters = parameters;
    }

    public BlockStatement getBody() {
        return Body;
    }

    public void setBody(BlockStatement body) {
        Body = body;
    }

    @Override
    public String TokenLiteral() {
        return Token.getLiteral();
    }

    @Override
    public NodeType NodeType() {
        return NodeType.FunctionLiteral;
    }

    @Override
    public String string() {
        String out = "";
        List<String> list = new ArrayList<>();
        for (Identifier p : Parameters) list.add(p.string());
        out = out + TokenLiteral() + "(" + StringUtils.join(list, ",") + ")" + Body.string();
        return out;
    }
}

class CallExpression extends Expression {
    private Token Token;
    private Expression Function;
    private List<Expression> Arguments;

    public CallExpression(Token token, Expression function, List<Expression> arguments) {
        Token = token;
        Function = function;
        Arguments = arguments;
    }

    @Override
    public Token getToken() {
        return Token;
    }

    @Override
    public void setToken(Token token) {
        Token = token;
    }

    public Expression getFunction() {
        return Function;
    }

    public void setFunction(Expression function) {
        Function = function;
    }

    public List<Expression> getArguments() {
        return Arguments;
    }

    public void setArguments(List<Expression> arguments) {
        Arguments = arguments;
    }

    @Override
    public String TokenLiteral() {
        return Token.getLiteral();
    }

    @Override
    public NodeType NodeType() {
        return NodeType.CallExpression;
    }

    @Override
    public String string() {
        String out = "";
        List<String> list = new ArrayList<>();
        for (Expression e : Arguments) list.add(e.string());
        out = out + Function.string() + "(" + StringUtils.join(list, ",") + ")";
        return out;
    }
}

//下标
class IndexExpression extends Expression{
    Token Token;
    Expression Left;
    Expression Index;

    public IndexExpression(Token token, Expression left, Expression index) {
        Token = token;
        Left = left;
        Index = index;
    }

    @Override
    public Token getToken() {
        return Token;
    }

    @Override
    public void setToken(Token token) {
        Token = token;
    }

    public Expression getLeft() {
        return Left;
    }

    public void setLeft(Expression left) {
        Left = left;
    }

    public Expression getIndex() {
        return Index;
    }

    public void setIndex(Expression index) {
        Index = index;
    }

    @Override
    public String TokenLiteral() {
        return Token.getLiteral();
    }

    @Override
    public NodeType NodeType() {
        return NodeType.IndexExpression;
    }

    @Override
    public String string() {
        String out="";
        out=out+"("+Left.string()+"["+Index.string()+"])";
        return out;
    }
}