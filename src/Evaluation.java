import java.util.List;

public class Evaluation {
    TypeBoolean True = new TypeBoolean(true);
    TypeBoolean False = new TypeBoolean(false);
    TypeNULL Null = new TypeNULL();

    public Object Eval(AST node) {
        switch (node.NodeType()) {
            case Program:
                return evalStatements(((Program) node).getStatements());
            case ExpressionStatement:
                return Eval( ((ExpressionStatement) node).getValue());
            case IntegerLiteral:
                return new TypeInteger(((IntegerLiteral) node).getValue());
            case Boolean:
                return nativeBoolToBoolean(((Boolean) node).getValue());
            case PrefixExpression:
                return evalPrefixExpression((PrefixExpression) node);
            case InfixExpression:
                return evalInfixExpression((InfixExpression) node);
            case BlockStatement:
                return evalBlockStatement((BlockStatement)node);
            case IfExpression:
                return evalIfExpression((IFExpression)node);
            case ReturnStatement:
                return new TypeReturnValue(Eval(((ReturnStatement) node).getValue()));
            default:
                return null;
        }
    }

    public Object evalStatements(List<Statement> statements) {
        Object result = null;
        for (Statement statement : statements) {
            result = Eval(statement);
            if(result.getClass().getName().equals("TypeReturnValue")){
                return ((TypeReturnValue)result).getValue();
            }
        }
        return result;
    }

    public TypeBoolean nativeBoolToBoolean(boolean str) {
        return str ? True : False;
    }

    public Object evalPrefixExpression(PrefixExpression prefixExpression) {
        Object right = Eval(prefixExpression.getRight());
        String type = right.getClass().getName();
        switch (prefixExpression.getOperator()) {
            case "!":
                if (type.equals("TypeBoolean")) {
                    return ((TypeBoolean) right).isValue() ? False : True;
                }
                return Null;
            case "-":
                if (type.equals("TypeInteger")) {
                    return new TypeInteger(((TypeInteger) right).getValue() * (-1));
                }
                return Null;
            default:
                return Null;
        }
    }

    public Object evalInfixExpression(InfixExpression infixExpression) {
        Object left = Eval(infixExpression.getLeft());
        String leftType = left.getClass().getName();
        Object right = Eval(infixExpression.getRight());
        String rightType = right.getClass().getName();

        if (leftType.equals("TypeInteger") && rightType.equals("TypeInteger")) {
            return evalIntegerInfixExpression(infixExpression.getOperator(), left, right);
        } else if (infixExpression.getOperator().equals("==")) {
            return nativeBoolToBoolean(left==right);
        } else if (infixExpression.getOperator().equals("!=")) {
            return nativeBoolToBoolean(!(left==right));
        } else {
            return Null;
        }
    }

    public Object evalIntegerInfixExpression(String operator, Object left, Object right) {
        switch (operator) {
            case "+":
                return new TypeInteger(((TypeInteger) left).getValue() + ((TypeInteger) right).getValue());
            case "-":
                return new TypeInteger(((TypeInteger) left).getValue() - ((TypeInteger) right).getValue());
            case "*":
                return new TypeInteger(((TypeInteger) left).getValue() * ((TypeInteger) right).getValue());
            case "/":
                return new TypeInteger(((TypeInteger) left).getValue() / ((TypeInteger) right).getValue());
            case "<":
                return nativeBoolToBoolean(((TypeInteger) left).getValue() < ((TypeInteger) right).getValue());
            case ">":
                return nativeBoolToBoolean(((TypeInteger) left).getValue() > ((TypeInteger) right).getValue());
            case "==":
                return nativeBoolToBoolean(((TypeInteger) left).getValue() == ((TypeInteger) right).getValue());
            case "!=":
                return nativeBoolToBoolean(((TypeInteger) left).getValue() != ((TypeInteger) right).getValue());
            default:
                return new TypeNULL();
        }
    }

    public Object evalIfExpression(IFExpression ifExpression){
        Object condition = Eval(ifExpression.getCondition());
        if(isTruthy(condition)){
            return Eval(ifExpression.getConsequence());
        }else if(ifExpression.getAlternative()!=null){
            return Eval(ifExpression.getAlternative());
        }else{
            return Null;
        }
    }

    public boolean isTruthy(Object object){
        if (Null.equals(object)) {
            return false;
        } else if (True.equals(object)) {
            return true;
        } else if (False.equals(object)) {
            return false;
        }
        return true;
    }

    public Object evalBlockStatement(BlockStatement blockStatement){
        Object result = null;
        for(Statement statement: blockStatement.getStatements()){
            result=Eval(statement);
            if(result!=null&&result.getClass().getName().equals("TypeReturnValue")){
                return result;
            }
        }
        return result;
    }
    
}