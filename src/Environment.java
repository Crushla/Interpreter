import java.util.HashMap;
import java.util.Map;

public class Environment {
    private Map<String, Object> map;
    private Environment outer;

    public Environment(Map<String, Object> map, Environment outer) {
        this.map = map;
        this.outer = outer;
    }

    //如果内部作用域找不到，就去外部作用域找
    public Object get(String key) {
        if (map.get(key) == null && outer != null) {
            return outer.get(key);
        }
        return map.get(key);
    }

    public void set(String key, Object value) {
        map.put(key, value);
    }

    public static Environment EncloseEnvironment(Environment outer) {
        return new Environment(new HashMap<>(), outer);
    }
}