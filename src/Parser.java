import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class precedences {
    public static int LOWEST = 0,
            EQUALS = 1,     //==
            LESSGREATER = 2,//> or <
            SUM = 3,        //+
            PRODUCT = 4,    //*
            PREFIX = 5,     //-X or !X
            CALL = 6;        //fun(x)
}

//不支持错误提示
public class Parser {
    private final Lexer lexer;
    //当前的Token
    private Token curToken;
    //下一个Token
    private Token peekToken;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
        nextToken();
        nextToken();
    }

    //与优先级对应
    Map<TokenType, Integer> precedencesLeval = new HashMap<TokenType, Integer>() {
        {
            put(TokenType.EQ, precedences.EQUALS);
            put(TokenType.NOT_EQ, precedences.EQUALS);
            put(TokenType.LT, precedences.LESSGREATER);
            put(TokenType.GT, precedences.LESSGREATER);
            put(TokenType.PLUS, precedences.SUM);
            put(TokenType.MINUS, precedences.SUM);
            put(TokenType.ASTERISK, precedences.PRODUCT);
            put(TokenType.SLASH, precedences.PRODUCT);
            put(TokenType.LPAREN, precedences.CALL);
        }
    };

    //下一个优先级
    public int peekPrecedence() {
        try {
            return precedencesLeval.get(peekToken.getType());
        } catch (Exception e) {
            return precedences.LOWEST;
        }
    }

    //当前的优先级
    public int curPrecedence() {
        try {
            return precedencesLeval.get(curToken.getType());
        } catch (Exception e) {
            return precedences.LOWEST;
        }
    }

    //检查当前的token类型
    public boolean curTokenIs(TokenType type) {
        return curToken.getType() == type;
    }

    //检查下一个token类型
    public boolean peekTokenIs(TokenType type) {
        return peekToken.getType() == type;
    }

    //检查peekToken类型
    public boolean expectPeek(TokenType type) {
        if (peekTokenIs(type)) {
            nextToken();
            return true;
        } else {
            return false;
        }
    }

    //下一个token
    public void nextToken() {
        curToken = peekToken;
        peekToken = lexer.nextToken();
    }

    //递归下降解析入口
    //构造AST的根节点
    public Program ParseProgram() {
        //Statement[]因为要初始化,故改用list
        //构造AST的根节点，然后遍历Token，直到标记是EOF为止
        Program program = new Program(new ArrayList<>());
        while (!curTokenIs(TokenType.EOF)) {
            Statement statement = parseStatement();
            if (statement != null) {
                program.getStatements().add(statement);
            }
            nextToken();
        }
        return program;
    }

    //解析语句
    public Statement parseStatement() {
        switch (curToken.getType()) {
            case VAR:
                return parseVarStatement();
            case RETURN:
                return parseReturnStatement();
            default:
                return parseExpressionStatement();
        }
    }

    //解析Var语句
    public Statement parseVarStatement() {
        Token token = curToken;
        //var n =2;
        //判断是否为变量
        //如果是变量就会向前一个token
        if (!expectPeek(TokenType.IDENT)) {
            return null;
        }
        //变量
        Identifier name = new Identifier(curToken, curToken.getLiteral());
        if (!expectPeek(TokenType.ASSIGN)) {
            return null;
        }
        nextToken();
        Expression value = parseExpression(precedences.LOWEST);

        if (peekTokenIs(TokenType.SEMICOLON)) {
            nextToken();
        }
        return new VarStatement(token, name, value);
    }

    //解析Return语句
    public Statement parseReturnStatement() {
        //return 2
        Token token = curToken;
        nextToken();
        Expression value = parseExpression(precedences.LOWEST);
        //如果不是分号就继续找下一个token
        //跳过了表达式部分
        if (peekTokenIs(TokenType.SEMICOLON)) {
            nextToken();
        }
        return new ReturnStatement(token, value);
    }

    //解析表达式
    public ExpressionStatement parseExpressionStatement() {
        Token token = curToken;
        Expression expression = parseExpression(precedences.LOWEST);
        if (peekTokenIs(TokenType.SEMICOLON)) {
            nextToken();
        }
        return new ExpressionStatement(token, expression);
    }

    //递归入口
    //递归解析其余部分
    public Expression parseExpression(int precedence) {
        Expression leftExp = parsePrefix();
        if (leftExp == null) {
            return null;
        }
        //尝试为下一个token寻找，如果找到就调用，直到遇到更高优先级的token
        //如果不为分号，就继续
        //分号和peekPrecedence优先级高于高于precedence
        //1+2+3
        //第一次循环1为left
        //第二次循环1+2为left
        while (!peekTokenIs(TokenType.SEMICOLON) && precedence < peekPrecedence()) {
            nextToken();
            Expression nextExp = parseInfix(leftExp);
            if (nextExp == null) return leftExp;


            leftExp = nextExp;
        }
        return leftExp;
    }


    //前缀表达式
    public Expression parsePrefix() {
        switch (curToken.getType()) {
            case IDENT:
                return parseIdentifier();
            case INT:
                return parseIntegerLiteral();
            case MINUS:
            case BANG:
                return parsePrefixExpression();
            case TRUE:
            case FALSE:
                return parseBoolean();
            case LPAREN:
                return parseGroupExpression();
            case IF:
                return parseIFExpression();
            case FUNCTION:
                return parseFuctionLiteral();
            default:
                return null;
        }

    }

    //中缀类型
    public Expression parseInfix(Expression leftExp) {
        switch (curToken.getType()) {
            case PLUS:
            case MINUS:
            case SLASH:
            case ASTERISK:
            case EQ:
            case NOT_EQ:
            case LT:
            case GT:
                return parseInfixExpression(leftExp);
            case LPAREN:
                return parseCallExpression(leftExp);
            default:
                return null;
        }

    }

    //前缀表达式
    public Expression parsePrefixExpression() {
        Token token = curToken;
        String operator = token.getLiteral();
        nextToken();
        Expression rigth = parseExpression(precedences.PREFIX);
        return new PrefixExpression(token, operator, rigth);
    }

    //中缀表达式
    //left为左侧字段
    public Expression parseInfixExpression(Expression left) {
        //中缀表达式的操作符优先级赋值给局部变量优先级
        int precedence = curPrecedence();
        Token token = curToken;
        String operator = token.getLiteral();
        nextToken();
        Expression right = parseExpression(precedence);
        return new InfixExpression(token, left, operator, right);
    }

    //变量
    public Expression parseIdentifier() {
        return new Identifier(curToken, curToken.getLiteral());
    }

    //不支持错误提示
    //Int类型
    public Expression parseIntegerLiteral() {
        Token token = curToken;
        int i = Integer.parseInt(token.getLiteral());
        return new IntegerLiteral(token, i);
    }

    //布尔类型
    public Expression parseBoolean() {
        return new Boolean(curToken, curTokenIs(TokenType.TRUE));
    }

    //()组
    public Expression parseGroupExpression() {
        nextToken();
        Expression expression = parseExpression(precedences.LOWEST);
        if (!expectPeek(TokenType.RPAREN)) {
            return null;
        }
        return expression;
    }

    //If语句
    public Expression parseIFExpression() {
        Token token = curToken;
        //参数
        if (!expectPeek(TokenType.LPAREN)) {
            return null;
        }
        nextToken();
        Expression condition = parseExpression(precedences.LOWEST);
        if (!expectPeek(TokenType.RPAREN)) {
            return null;
        }
        if (!expectPeek(TokenType.LBRACE)) {
            return null;
        }
        BlockStatement consequence = parseBlockStatement();
        BlockStatement alternative = null;
        if (peekTokenIs(TokenType.ELSE)) {
            nextToken();
            if (!expectPeek(TokenType.LBRACE)) {
                return null;
            }
            alternative = parseBlockStatement();
        }
        return new IFExpression(token, condition, consequence, alternative);
    }

    //解析块
    public BlockStatement parseBlockStatement() {
        Token token = curToken;
        List<Statement> statements = new ArrayList<>();
        nextToken();
        //{}为块
        // 直到块语句结束或遇到EOF
        while (!curTokenIs(TokenType.RBRACE) && !curTokenIs(TokenType.EOF)) {
            Statement statement = parseStatement();
            if (statement != null)
                statements.add(statement);
            nextToken();
        }
        return new BlockStatement(token, statements);
    }

    //函数
    public Expression parseFuctionLiteral() {
        Token token = curToken;
        if (!expectPeek(TokenType.LPAREN)) {
            return null;
        }
        //参数
        List<Identifier> parameters = parseFunctionParameters();
        if (!expectPeek(TokenType.LBRACE)) {
            return null;
        }
        BlockStatement body = parseBlockStatement();
        return new FunctionLiteral(token, parameters, body);
    }

    //解析参数
    public List<Identifier> parseFunctionParameters() {
        List<Identifier> Parameters = new ArrayList<>();
        if (peekTokenIs(TokenType.RPAREN)) {
            nextToken();
            return Parameters;
        }
        nextToken();
        Identifier identifier = new Identifier(curToken, curToken.getLiteral());
        Parameters.add(identifier);
        while (peekTokenIs(TokenType.COMMA)) {
            nextToken();
            nextToken();
            identifier = new Identifier(curToken, curToken.getLiteral());
            Parameters.add(identifier);
        }
        if (!expectPeek(TokenType.RPAREN)) {
            return null;
        }
        return Parameters;
    }

    //函数调用
    public Expression parseCallExpression(Expression function) {
        return new CallExpression(curToken, function, parseCallArguments());
    }

    //函数调用的的参数
    public List<Expression> parseCallArguments() {
        List<Expression> arguments = new ArrayList<>();
        if (peekTokenIs(TokenType.RPAREN)) {
            nextToken();
            return arguments;
        }
        nextToken();
        arguments.add(parseExpression(precedences.LOWEST));
        while (peekTokenIs(TokenType.COMMA)) {
            nextToken();
            nextToken();
            arguments.add(parseExpression(precedences.LOWEST));
        }
        if (!expectPeek(TokenType.RPAREN)) {
            return null;
        }
        return arguments;
    }
}
