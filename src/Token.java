public class Token {
    enum TokenType {
        ILLEGAL,  //不知道的token
        EOF,      //结束

        IDENT,    //变量
        INT,      //int类型
        STRING,   //字符串

        ASSIGN,   //=
        PLUS,     //+
        MINUS,    //-
        BANG,     //!
        ASTERISK, //*
        SLASH,    ///
        LT,       //<
        GT,       //>
        EQ,       //==
        NOT_EQ,   //!=

        COMMA,    //,
        SEMICOLON,//;

        LPAREN,   //(
        RPAREN,   //)
        LBRACE,   //{
        RBRACE,   //}

        FUNCTION, //函数
        VAR,      //创建变量
        TRUE,     //正确
        FALSE,    //错误
        IF,
        ELSE,
        RETURN    //返回值
    }

    private TokenType Type;
    private String Literal;

    public Token(TokenType type, String literal) {
        Type = type;
        Literal = literal;
    }

    public TokenType getType() {
        return Type;
    }

    public void setType(TokenType type) {
        Type = type;
    }

    public String getLiteral() {
        return Literal;
    }

    public void setLiteral(String literal) {
        Literal = literal;
    }
}
