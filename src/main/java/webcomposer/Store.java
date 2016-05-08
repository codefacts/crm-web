package webcomposer;

import io.crm.util.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shahadat on 5/4/16.
 */
public class Store extends Context implements Map<String, Object> {

    public Store() {
        super(new HashMap<>());
    }

    public Store(Map<String, Object> storeMap) {
        super(storeMap);
    }

    @Override
    public Object put(String key, Object value) {
        return map.put(key, value);
    }

    @Override
    public Object remove(Object key) {
        return map.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        map.putAll(m);
    }

    @Override
    public void clear() {
        map.clear();
    }
}
