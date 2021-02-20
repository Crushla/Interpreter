import java.util.ArrayList;
import java.util.List;

public class Parser {
    Lexer lexer;
    //当前的Token
    Token curToken;
    //下一个Token
    Token peekToken;
    List<String> errors;

    public Parser(Lexer lexer) {
        lexer = lexer;
        errors = new ArrayList<>();
        nextToken();
        nextToken();
    }

    public List<String> Errors() {
        return errors;
    }
    //当前错误
    public void peekError(TokenType type) {
        errors.add(String.format("expected next token to be %s,got %s instead", type, peekToken.getType()));
    }

    public void nextToken() {
        curToken = peekToken;
        peekToken = lexer.nextToken();
    }

    //递归下降解析入口
    //构造AST的根节点
    public Program ParseProgram() {
        //Statement[]因为要初始化,故改用list
        //构造AST的根节点，然后遍历Token，直到标记是EOF为止
        Program program = new Program(new ArrayList<Statement>());
        while (curToken.getType() != TokenType.EOF) {
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
                return null;
        }
    }

    public Statement parseVarStatement() {
        Token token =curToken;
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

        //如果不是分号就继续找下一个token
        //跳过了表达式部分
        while (!curTokenIs(TokenType.SEMICOLON)) {
            nextToken();
        }
        return new VarStatement(token, name, value);
    }
    public Statement parseReturnStatement() {
        //return 2
        Token token =curToken;
        nextToken();
        //如果不是分号就继续找下一个token
        //跳过了表达式部分
        while (!curTokenIs(TokenType.SEMICOLON)) {
            nextToken();
        }
        return new ReturnStatement(token,value );
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
            peekError(type);
            return false;
        }
    }

    public parseIdentifier() {
        identifier = newIdentifierASTNode();
        identifier.token = currentToken();
        return identifier;
    }

    //递归入口
    //递归解析其余部分
    public parseExpression() {
        if (currentToken() == INTEGER_TOKEN) {
            if (nextToken() == PLUS_TOKEN) {
                return parseOperatorExpression();
            } else if (nextToken() == SEMICOLON_TOEN) {
                return parseIntegerLiteral();
            }
        } else if (currentToken() == LEFT_PAREN) {
            return parseGroupedExpression();
        }
    }

    public parseOperatorExpression() {
        operatorExpression = newOperatorExpression();
        operatorExpression.left = parseIntegerLiteral();
        operatorExpression.operator = currentToken();
        operatorExpression.right = parseExpression();
        return operatorExpression();
    }
}
