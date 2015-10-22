package io.crm.web.util;

/**
 * Created by someone on 20/10/2015.
 */
final public class Script {
    public final String name;
    public final String src;
    public final Type type;

    public Script(final String name, final String src, final Type type) {
        this.name = name;
        this.src = src;
        this.type = type;
    }

    public enum Type {
        JavaScript("text/javascript"), Babel("text/babel");
        public final String value;

        Type(final String value) {
            this.value = value;
        }
    }
}
