import com.sun.deploy.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

class ObjectType {
    public static String INTEGER_OBJ = "INTEGER",
            BOOLEAN_OBJ = "BOOLEAN",
            NULL_OBJ = "NULL",
            RETURN_VALUE_OBJ = "RETURN_VALUE",
            FUNCTION_OBJ="FUNCTION";
}

public interface Object {
    String ObjectType();

    String Inspect();
}

//integer
class TypeInteger implements Object {
    private int Value;

    public TypeInteger(int value) {
        Value = value;
    }

    public int getValue() {
        return Value;
    }

    public void setValue(int value) {
        Value = value;
    }

    @Override
    public String ObjectType() {
        return ObjectType.INTEGER_OBJ;
    }

    @Override
    public String Inspect() {
        return String.valueOf(Value);
    }
}

//boolean
class TypeBoolean implements Object {
    private boolean Value;

    public boolean isValue() {
        return Value;
    }

    public void setValue(boolean value) {
        Value = value;
    }

    public TypeBoolean(boolean value) {
        Value = value;
    }

    @Override
    public String ObjectType() {
        return ObjectType.BOOLEAN_OBJ;
    }

    @Override
    public String Inspect() {
        return String.valueOf(Value);
    }
}

//null
class TypeNULL implements Object {
    public TypeNULL() {

    }

    @Override
    public String ObjectType() {
        return ObjectType.NULL_OBJ;
    }

    @Override
    public String Inspect() {
        return "null";
    }
}

class TypeReturnValue implements Object {
    Object Value;

    public TypeReturnValue(Object value) {
        Value = value;
    }

    public Object getValue() {
        return Value;
    }

    public void setValue(Object value) {
        Value = value;
    }

    @Override
    public String ObjectType() {
        return ObjectType.RETURN_VALUE_OBJ;
    }

    @Override
    public String Inspect() {
        return Value.Inspect();
    }
}

class TypeFunction implements Object{
    private List<Identifier> Parameters;
    private BlockStatement Body;
    private Environment Env;

    public TypeFunction(List<Identifier> parameters, BlockStatement body, Environment env) {
        Parameters = parameters;
        Body = body;
        Env = env;
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

    public Environment getEnv() {
        return Env;
    }

    public void setEnv(Environment env) {
        Env = env;
    }

    @Override
    public String ObjectType() {
        return ObjectType.FUNCTION_OBJ;
    }

    @Override
    public String Inspect() {
        String out="";
        List<String >list=new ArrayList<>();
        for (Identifier p:Parameters)list.add(p.string());
        out=out+"fn"+"("+ StringUtils.join(list,",")+"){\n"+Body.string()+"\n}";
        return out;
    }
}
