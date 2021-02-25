class ObjectType {
    public static String INTEGER_OBJ = "INTEGER",
            BOOLEAN_OBJ = "BOOLEAN",
            NULL_OBJ = "NULL";
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