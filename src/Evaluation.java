import java.util.ArrayList;
import java.util.List;

public class Evaluation {
    TypeBoolean True = new TypeBoolean(true);
    TypeBoolean False = new TypeBoolean(false);
    TypeNULL Null = new TypeNULL();

    public Object Eval(AST node, Environment environment) {
        switch (node.NodeType()) {
            case Program:
                return evalStatements(((Program) node).getStatements(), environment);
            case ExpressionStatement:
                return Eval(((ExpressionStatement) node).getValue(), environment);
            case IntegerLiteral:
                return new TypeInteger(((IntegerLiteral) node).getValue());
            case Boolean:
                return nativeBoolToBoolean(((Boolean) node).getValue());
            case PrefixExpression:
                return evalPrefixExpression((PrefixExpression) node, environment);
            case InfixExpression:
                return evalInfixExpression((InfixExpression) node, environment);
            case BlockStatement:
                return evalBlockStatement((BlockStatement) node, environment);
            case IfExpression:
                return evalIfExpression((IFExpression) node, environment);
            case ReturnStatement:
                return new TypeReturnValue(Eval(((ReturnStatement) node).getValue(), environment));
            case VarStatement:
                Object eval = Eval(((VarStatement) node).getValue(), environment);
                environment.set(((VarStatement) node).getName().getValue(), eval);
                return Null;
            case Identifier:
                return evalIdentifier((Identifier) node, environment);
            case FunctionLiteral:
                FunctionLiteral newNode = (FunctionLiteral) node;
                List<Identifier> parameters = newNode.getParameters();
                BlockStatement body = newNode.getBody();
                return new TypeFunction(parameters, body, environment);
            case CallExpression:
                Object function = Eval(((CallExpression) node).getFunction(), environment);
                List<Object> arguments = evalExpressions(((CallExpression) node).getArguments(), environment);
                return applyFunction(function, arguments);
            default:
                return Null;
        }
    }

    public Object evalStatements(List<Statement> statements, Environment environment) {
        Object result = null;
        for (Statement statement : statements) {
            result = Eval(statement, environment);
            if (result.getClass().getName().equals("TypeReturnValue")) {
                return ((TypeReturnValue) result).getValue();
            }
        }
        return result;
    }

    public TypeBoolean nativeBoolToBoolean(boolean str) {
        return str ? True : False;
    }

    public Object evalPrefixExpression(PrefixExpression prefixExpression, Environment environment) {
        Object right = Eval(prefixExpression.getRight(), environment);
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

    public Object evalInfixExpression(InfixExpression infixExpression, Environment environment) {
        Object left = Eval(infixExpression.getLeft(), environment);
        String leftType = left.getClass().getName();
        Object right = Eval(infixExpression.getRight(), environment);
        String rightType = right.getClass().getName();

        if (leftType.equals("TypeInteger") && rightType.equals("TypeInteger")) {
            return evalIntegerInfixExpression(infixExpression.getOperator(), left, right);
        } else if (infixExpression.getOperator().equals("==")) {
            return nativeBoolToBoolean(left == right);
        } else if (infixExpression.getOperator().equals("!=")) {
            return nativeBoolToBoolean(!(left == right));
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

    public Object evalIfExpression(IFExpression ifExpression, Environment environment) {
        Object condition = Eval(ifExpression.getCondition(), environment);
        if (isTruthy(condition)) {
            return Eval(ifExpression.getConsequence(), environment);
        } else if (ifExpression.getAlternative() != null) {
            return Eval(ifExpression.getAlternative(), environment);
        } else {
            return Null;
        }
    }

    public boolean isTruthy(Object object) {
        if (Null.equals(object)) {
            return false;
        } else if (True.equals(object)) {
            return true;
        } else if (False.equals(object)) {
            return false;
        }
        return true;
    }

    public Object evalBlockStatement(BlockStatement blockStatement, Environment environment) {
        Object result = null;
        for (Statement statement : blockStatement.getStatements()) {
            result = Eval(statement, environment);
            if (result != null && result.getClass().getName().equals("TypeReturnValue")) {
                return result;
            }
        }
        return result;
    }

    public Object evalIdentifier(Identifier identifier, Environment environment) {
        Object object = environment.get(identifier.getValue());
        if (object == null) {
            return Null;
        }
        return object;
    }

    public List<Object> evalExpressions(List<Expression> expressions, Environment environment) {
        List<Object> list = new ArrayList<>();
        for (Expression expression : expressions) {
            Object eval = Eval(expression, environment);
            list.add(eval);
        }
        return list;
    }

    public Object applyFunction(Object fn, List<Object> args) {
        TypeFunction function = (TypeFunction) fn;
        Environment environment = extendFunctionEnv(function, args);
        Object eval = Eval(function.getBody(), environment);
        return unwrapReturnValue(eval);
    }

    public Environment extendFunctionEnv(TypeFunction function, List<Object> args) {
        Environment environment = Environment.EncloseEnvironment(function.getEnv());
        for (int i = 0; i < args.size(); i++) {
            environment.set(function.getParameters().get(i).getValue(), args.get(i));
        }
        return environment;
    }

    public Object unwrapReturnValue(Object object) {
        if (object.getClass().getName().equals("TypeReturnValue")) return ((TypeReturnValue) object).getValue();
        return object;
    }
}