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
    Map<String, TokenType> map = new HashMap<String, TokenType>() {
        {
            put("fn", TokenType.FUNCTION);
            put("var", TokenType.VAR);
            put("true", TokenType.TRUE);
            put("false", TokenType.FALSE);
            put("if", TokenType.IF);
            put("else", TokenType.ELSE);
            put("return", TokenType.RETURN);
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
                    token = new Token(TokenType.EQ, "==");
                } else {
                    token = new Token(TokenType.ASSIGN, "=");
                }
                break;
            case '!':
                if (peekChar() == '=') {
                    readChar();
                    token = new Token(TokenType.NOT_EQ, "!=");
                } else {
                    token = new Token(TokenType.BANG, "!");
                }
                break;
            case ';':
                token = new Token(TokenType.SEMICOLON, ";");
                break;
            case '(':
                token = new Token(TokenType.LPAREN, "(");
                break;
            case ')':
                token = new Token(TokenType.RPAREN, ")");
                break;
            case ',':
                token = new Token(TokenType.COMMA, ",");
                break;
            case '+':
                token = new Token(TokenType.PLUS, "+");
                break;
            case '-':
                token = new Token(TokenType.MINUS, "-");
                break;
            case '*':
                token = new Token(TokenType.ASTERISK, "*");
                break;
            case '/':
                token = new Token(TokenType.SLASH, "/");
                break;
            case '<':
                token = new Token(TokenType.LT, "<");
                break;
            case '>':
                token = new Token(TokenType.GT, ">");
                break;
            case '{':
                token = new Token(TokenType.LBRACE, "{");
                break;
            case '}':
                token = new Token(TokenType.RBRACE, "}");
                break;
            case '"':
                token = new Token(TokenType.STRING, readString());
                break;
            case '[':
                token = new Token(TokenType.LBRACKET, "[");
                break;
            case ']':
                token = new Token(TokenType.RBRACKET, "]");
                break;
            case '\0':
                token = new Token(TokenType.EOF, "");
                break;
            default:
                if (isLetter(ch)) {
                    String literal = readIdentifier();
                    TokenType type = LookupIdent(literal);
                    token = new Token(type, literal);
                    return token;
                } else if (isDigit(ch)) {
                    TokenType type = TokenType.INT;
                    String literal = readNumber();
                    token = new Token(type, literal);
                    return token;
                } else {
                    token = new Token(TokenType.ILLEGAL, Character.toString(ch));
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
    public TokenType LookupIdent(String ident) {
        return map.getOrDefault(ident, TokenType.IDENT);
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

    public String readString() {
        readChar();
        int Position = currentPosition;
        while (ch != '"') {
            readChar();
        }
        return input.substring(Position, currentPosition);
    }
}
