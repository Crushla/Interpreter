import com.sun.deploy.util.StringUtils;

import java.util.ArrayList;

import java.util.List;
import java.util.Map;


class ObjectType {
    public static String INTEGER_OBJ = "INTEGER",
            BOOLEAN_OBJ = "BOOLEAN",
            NULL_OBJ = "NULL",
            STRING_OBJ = "STRING",
            RETURN_VALUE_OBJ = "RETURN_VALUE",
            FUNCTION_OBJ = "FUNCTION",
            ARRAY_OBJ = "ARRAY",
            HASH_OBJ = "HASH";
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

class TypeString implements Object {
    private String Value;

    public TypeString(String value) {
        Value = value;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    @Override
    public String ObjectType() {
        return ObjectType.STRING_OBJ;
    }

    @Override
    public String Inspect() {
        return Value;
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

class TypeFunction implements Object {
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
        String out = "";
        List<String> list = new ArrayList<>();
        for (Identifier p : Parameters) list.add(p.string());
        out = out + "fn" + "(" + StringUtils.join(list, ",") + "){\n" + Body.string() + "\n}";
        return out;
    }
}

class TypeArray implements Object {
    private List<Object> Elements;

    public TypeArray(List<Object> elements) {
        Elements = elements;
    }

    public List<Object> getElements() {
        return Elements;
    }

    public void setElements(List<Object> elements) {
        Elements = elements;
    }

    @Override
    public String ObjectType() {
        return ObjectType.ARRAY_OBJ;
    }

    @Override
    public String Inspect() {
        String out = "";
        List<String> list = new ArrayList<>();
        for (Object o : Elements) list.add(o.Inspect());
        out = out + "[" + StringUtils.join(list, ",") + "]";
        return out;
    }
}

//hash
class TypeHash implements Object {
    private Map<TypeHashKey, TypeHashPair> Pairs;

    public TypeHash(Map<TypeHashKey, TypeHashPair> pairs) {
        Pairs = pairs;
    }

    public Map<TypeHashKey, TypeHashPair> getPairs() {
        return Pairs;
    }

    public void setPairs(Map<TypeHashKey, TypeHashPair> pairs) {
        Pairs = pairs;
    }

    @Override
    public String ObjectType() {
        return ObjectType.HASH_OBJ;
    }

    @Override
    public String Inspect() {
        return null;
    }
}

//hashkey
class TypeHashKey implements Object {
    private ObjectType Type;
    private int Value;

    @Override
    public String ObjectType() {
        return null;
    }

    @Override
    public String Inspect() {
        return null;
    }
}

//hashpair
class TypeHashPair implements Object {

    @Override
    public String ObjectType() {
        return null;
    }

    @Override
    public String Inspect() {
        return null;
    }
}