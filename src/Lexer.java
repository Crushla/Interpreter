import java.util.HashMap;
import java.util.Map;

public class Lexer {
    String input;
    //正在读取的位置
    int currentPosition;
    //下一要读取的位置
    int readPositon;
    //正在读取的字符
    char ch;
    Map<String, Token.TokenType> map = new HashMap<String, Token.TokenType>() {
        {
            put("fn", Token.TokenType.FUNCTION);
            put("var", Token.TokenType.VAR);
            put("true", Token.TokenType.TRUE);
            put("false", Token.TokenType.FALSE);
            put("if", Token.TokenType.IF);
            put("else", Token.TokenType.ELSE);
            put("return", Token.TokenType.RETURN);
        }
    };

    public Lexer(String input) {
        this.input = input;
        readChar();
    }

    public void readChar() {
        if (readPositon >= input.length())
            ch = '\0';
        else
            ch = input.charAt(readPositon);
        currentPosition = readPositon;
        readPositon++;
    }

    public Token nextToken() {
        Token token = null;
        skipWhitespace();
        switch (ch) {
            case '=':
                if (peekChar() == '=') {
                    readChar();
                    token = new Token(Token.TokenType.EQ, "==");
                } else {
                    token = new Token(Token.TokenType.ASSIGN, Character.toString(ch));
                }
                break;
            case '!':
                if (peekChar() == '=') {
                    readChar();
                    token = new Token(Token.TokenType.NOT_EQ, "!=");
                } else {
                    token = new Token(Token.TokenType.BANG, Character.toString(ch));
                }
                break;
            case ';':
                token = new Token(Token.TokenType.SEMICOLON, Character.toString(ch));
                break;
            case '(':
                token = new Token(Token.TokenType.LPAREN, Character.toString(ch));
                break;
            case ')':
                token = new Token(Token.TokenType.RPAREN, Character.toString(ch));
                break;
            case ',':
                token = new Token(Token.TokenType.COMMA, Character.toString(ch));
                break;
            case '+':
                token = new Token(Token.TokenType.PLUS, Character.toString(ch));
                break;
            case '-':
                token = new Token(Token.TokenType.MINUS, Character.toString(ch));
                break;
            case '*':
                token = new Token(Token.TokenType.ASTERISK, Character.toString(ch));
                break;
            case '/':
                token = new Token(Token.TokenType.SLASH, Character.toString(ch));
                break;
            case '<':
                token = new Token(Token.TokenType.LT, Character.toString(ch));
                break;
            case '>':
                token = new Token(Token.TokenType.GT, Character.toString(ch));
                break;
            case '{':
                token = new Token(Token.TokenType.LBRACE, Character.toString(ch));
                break;
            case '}':
                token = new Token(Token.TokenType.RBRACE, Character.toString(ch));
                break;
            case '\0':
                token = new Token(Token.TokenType.EOF, "");
                break;
            default:
                if (isLetter(ch)) {
                    String literal = readIdentifier();
                    Token.TokenType type = LookupIdent(literal);
                    token = new Token(type, literal);
                    return token;
                } else if (isDigit(ch)) {
                    Token.TokenType type = Token.TokenType.INT;
                    String literal = readNumber();
                    token = new Token(type, literal);
                    return token;
                } else {
                    token = new Token(Token.TokenType.ILLEGAL, Character.toString(ch));
                    readChar();
                    return token;
                }
        }
        readChar();
        return token;
    }

    public String readIdentifier() {
        int position = currentPosition;
        while (isLetter(ch)) {
            readChar();
        }
        return input.substring(position, currentPosition);
    }

    //判断是否是一个信息
    //大小写字符和_组成信息
    public boolean isLetter(char ch) {
        return ('a' <= ch && ch <= 'z') || ('A' <= ch && ch <= 'Z') || ch == '_';
    }

    //判断是否为数字
    public boolean isDigit(char ch) {
        return '0' <= ch && ch <= '9';
    }

    //判断是否是标识符
    public Token.TokenType LookupIdent(String ident) {
        return map.getOrDefault(ident, Token.TokenType.IDENT);
    }

    //读取数字
    public String readNumber() {
        int position = currentPosition;
        while (isDigit(ch)) {
            readChar();
        }
        return input.substring(position, currentPosition);
    }

    //跳过空白
    public void skipWhitespace() {
        while (ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r') {
            readChar();
        }
    }

    //观察后面一个字符，查看后面的符号
    public char peekChar() {
        if (readPositon >= input.length())
            return '\0';
        else
            return input.charAt(readPositon);
    }
}
