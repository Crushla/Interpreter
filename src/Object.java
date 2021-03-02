import com.sun.deploy.util.StringUtils;

import java.lang.reflect.Type;
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

public interface Object extends Hashable {
    String ObjectType();

    String Inspect();
}

interface Hashable {
    TypeHashKey HashKey();
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

    @Override
    public TypeHashKey HashKey() {
        return new TypeHashKey(ObjectType(), Value);
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

    public TypeHashKey HashKey() {
        int value;
        if (Value) {
            value = 1;
        } else {
            value = 0;
        }
        return new TypeHashKey(ObjectType(), value);
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

    public TypeHashKey HashKey() {
        int hashCode = Value.hashCode();
        return new TypeHashKey(ObjectType(), hashCode);
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

    @Override
    public TypeHashKey HashKey() {
        return null;
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

    @Override
    public TypeHashKey HashKey() {
        return null;
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

    @Override
    public TypeHashKey HashKey() {
        return null;
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

    @Override
    public TypeHashKey HashKey() {
        return null;
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
        String out = "";
        List<String> list = new ArrayList<>();
        for (Map.Entry<TypeHashKey, TypeHashPair> p : Pairs.entrySet()) {
            String str = p.getKey().Inspect() + p.getValue().Inspect();
            list.add(str);
        }
        out = out + "{" + StringUtils.join(list, ",") + "}";
        return out;
    }

    @Override
    public TypeHashKey HashKey() {
        return null;
    }
}

//hashPair
class TypeHashPair implements Object {
    private Object Key;
    private Object Value;

    public Object getKey() {
        return Key;
    }

    public void setKey(Object key) {
        Key = key;
    }

    public Object getValue() {
        return Value;
    }

    public void setValue(Object value) {
        Value = value;
    }

    public TypeHashPair(Object key, Object value) {
        Key = key;
        Value = value;
    }

    @Override
    public String ObjectType() {
        return null;
    }

    @Override
    public String Inspect() {
        return "Key:" + Key.Inspect() + ",Value:" + Value.Inspect()+"  ";
    }

    @Override
    public TypeHashKey HashKey() {
        return null;
    }
}


//hashKey
class TypeHashKey implements Object {
    private String Type;
    private int Value;

    public TypeHashKey(String type, int value) {
        Type = type;
        Value = value;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public int getValue() {
        return Value;
    }

    public void setValue(int value) {
        Value = value;
    }

    @Override
    public String ObjectType() {
        return null;
    }

    @Override
    public String Inspect() {
        return "  Hash:" + Value + "    ";
    }

    @Override
    public TypeHashKey HashKey() {
        return null;
    }
}