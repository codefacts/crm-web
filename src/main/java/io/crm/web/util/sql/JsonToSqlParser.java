package io.crm.web.util.sql;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.crm.intfs.ConsumerUnchecked;
import io.crm.util.Util;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import static io.crm.util.Util.*;

/**
 * Created by shahadat on 1/5/16.
 */
public class JsonToSqlParser {
    public static void inset(String tableName, JsonObject data, ConsumerUnchecked<Long> consumerUnchecked) {

    }

    public static void main(String... args) {
//        toSqlQuery(new JsonObject()
//                .put("select", new JsonArray()
//                                .put(new JsonObject().put("id", "").put("as", ""))
//                                .put(new JsonObject().put("name", "name").put("as", ""))
//                )
//                .put("from", new JsonArray()
//                        .put(new JsonObject().put("name", ))));
        JsonObject JsonObject = null;
        try {
            JsonObject = new JsonObject().put("id", "").put("name", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String toSqlInsert(JsonObject js, List<String> params) {
        JsonObject data = js.getJsonObject("data");
        StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO `").append(js.getString("tableName")).append("` ");
        Util.join(", ", data.getMap().keySet().iterator(), builder, "`", "`")
            .append(") ")
            .append("VALUES (");

        values(data, builder, params);

        builder.append(")");

        return builder.toString();
    }

    public static String toSqlUpdate(JsonObject js, List<String> params) {
        JsonObject data = js.getJsonObject("data");
        StringBuilder builder = new StringBuilder();

        builder.append("UPDATE ").append("`").append(js.getString("tableName")).append("`").append(" SET ");

        Iterator<String> keys = data.getMap().keySet().iterator();
        final int length = builder.length();
        for (; keys.hasNext(); ) {
            String key = keys.next();
            builder.append("`").append(key).append("`").append(" = ").append(singleOperand(
                data.getValue(key), params)).append(", ");
        }
        if (builder.length() > length) {
            builder.delete(builder.length() - ", ".length(), builder.length());
        }
        String where = whereStr(js.getJsonObject("where"), new StringBuilder(), params).toString();
        where = where.trim().isEmpty() ? "" : "where " + where;

        return builder.append(" ").append(where).toString();
    }

    public static String toSqlDelete(JsonObject js, ArrayList<String> params) {
        StringBuilder builder = new StringBuilder();
        builder.append("delete from ").append(js.getString("tableName"));
        String where = whereStr(js.getJsonObject("where"), new StringBuilder(), params).toString();
        where = where.trim().isEmpty() ? "" : "where " + where;
        return builder.append(" ").append(where).toString();
    }

    public static String toSqlQuery(JsonObject query, List<String> params) {
        return toSqlQuery(or(query.getJsonArray("select"), EMPTY_JSON_ARRAY),
            or(query.getJsonArray("from"), EMPTY_JSON_ARRAY),
            or(query.getJsonArray("join"), EMPTY_JSON_ARRAY),
            or(query.getJsonObject("where"), EMPTY_JSON_OBJECT),
            or(query.getJsonArray("orderBy"), EMPTY_JSON_ARRAY),
            or(query.getJsonArray("groupBy"), EMPTY_JSON_ARRAY),
            or(query.getJsonObject("having"), EMPTY_JSON_OBJECT), params);
    }

    public static String toSqlQuery(JsonArray select, JsonArray from, JsonArray join,
                                    JsonObject where, JsonArray orderBy,
                                    JsonArray groupBy, JsonObject having, List<String> params) {

        String selectStr = selectStr(select, new StringBuilder(), params).toString();
        String fromStr = fromStr(from, new StringBuilder()).toString();
        String joinStr = joinStr(join, new StringBuilder()).toString();
        String whereStr = whereStr(where, new StringBuilder(), params).toString();
        String groupByStr = groupByStr(groupBy, new StringBuilder()).toString();
        String havingStr = havingStr(having, new StringBuilder(), params).toString();
        String orderByStr = orderByStr(orderBy, new StringBuilder()).toString();

        selectStr = selectStr.isEmpty() ? "select *" : "select " + selectStr;
        fromStr = "from " + fromStr;
        whereStr = whereStr.trim().isEmpty() ? "" : "where " + whereStr;

        if (!groupByStr.trim().isEmpty()) {
            groupByStr = "group by " + groupByStr;
            havingStr = havingStr.trim().isEmpty() ? "" : "having " + havingStr;
        }

        return selectStr + " " + fromStr + " " + joinStr + " " + whereStr + " " + groupByStr + " " + havingStr + " " + orderByStr;
    }

    public static StringBuilder joinStr(JsonArray joinList, final StringBuilder builder) {
        final int length = builder.length();
        for (int i = 0; i < joinList.size(); i++) {
            JsonObject value = joinList.getJsonObject(i);
            JsonObject on = value.getJsonObject("on");
            String toField = on.getMap().keySet().iterator().next();
            builder.append(value.getString("type", "")).append(" ").append(value.getString("name"))
                .append(" ").append(value.getString("as"))
                .append(" on ")
                .append(value.getString("as").isEmpty() ? "" : value.getString("as") + ".")
                .append(value.getString("field"))
                .append(" = ")
                .append(on.getString(toField).isEmpty() ? "" : on.getString(toField) + ".")
                .append(toField).append(" ");
        }
        return builder.length() > length ? builder.delete(builder.length() - " ".length(), builder.length()) : builder;
    }

    public static StringBuilder fromStr(JsonArray from, StringBuilder builder) {
        final int length = builder.length();
        for (int i = 0; i < from.size(); i++) {
            Object obj = from.getValue(i);

            if (obj instanceof JsonObject) {
                JsonObject js = (JsonObject) obj;
                if (js.size() == 1) {
                    String name = js.getMap().keySet().iterator().next();
                    String as = js.getString(name, "");
                    builder.append(name).append(as.isEmpty() ? "" : " " + as).append(", ");
                } else {
                    throw new RuntimeException("Json Object does not contain a valid key to parse. JS: " + js.toString());
                }
            } else if (obj instanceof String) {
                builder.append((String) obj).append(", ");
            } else {
                throw new RuntimeException("Input Object is not valid. Obj: " + obj);
            }
        }
        return builder.length() > length ? builder.delete(builder.length() - ", ".length(), builder.length()) : builder;
    }

    public static StringBuilder selectStr(JsonArray select, final StringBuilder builder, List<String> params) {
        final int length = builder.length();

        for (int i = 0; i < select.size(); i++) {
            Object obj = select.getValue(i);

            if (obj instanceof JsonObject) {
                JsonObject js = (JsonObject) obj;
                if (js.size() == 1) {
                    if (js.containsKey("$param")) {
                        builder.append(singleOperand(js.getValue("$param"), params));
                    } else {
                        String name = js.getMap().keySet().iterator().next();
                        String as = js.getString(name, "");
                        builder.append(as.isEmpty() ? "" : as + ".").append(name).append(", ");
                    }
                } else if (js.containsKey("rename")) {
                    builder.append(js.getString("as", "").isEmpty() ? "" : js.getString("as") + ".")
                        .append(js.getString("name")).append(js.getString("rename", "").isEmpty() ? "" : " as " + js.getString("rename"))
                        .append(", ");
                } else {
                    throw new RuntimeException("Json Object does not contain a valid key to parse. JS: " + js.toString());
                }
            } else if (obj instanceof String) {
                builder.append((String) obj).append(", ");
            } else {
                throw new RuntimeException("Input Object is not valid. Obj: " + obj);
            }
        }
        return builder.length() > length ? builder.delete(builder.length() - ", ".length(), builder.length()) : builder;
    }

    public static StringBuilder havingStr(JsonObject having, StringBuilder builder, List<String> params) {
        return parseOp(having, new StringBuilder(), params);
    }

    public static StringBuilder orderByStr(JsonArray orderBy, final StringBuilder builder) {
        final int length = builder.length();
        for (int i = 0; i < orderBy.size(); i++) {
            JsonObject value = orderBy.getJsonObject(i);
            builder.append(value.getString("as") == null ? "" : value.getString("as") + ".")
                .append(value.getString("name")).append(" ").append(value.getString("order"))
                .append(", ");
        }
        return builder.length() > length ? builder.delete(builder.length() - ", ".length(), builder.length()) : builder;
    }

    public static StringBuilder groupByStr(JsonArray groupBy, final StringBuilder builder) {
        final int length = builder.length();

        for (int i = 0; i < groupBy.size(); i++) {

            Object obj = groupBy.getValue(i);

            if (obj instanceof JsonObject) {
                JsonObject js = (JsonObject) obj;
                if (js.size() == 1) {

                    String name = js.getMap().keySet().iterator().next();
                    String as = js.getString(name, "");

                    builder.append(as.isEmpty() ? "" : as + ".")
                        .append(name).append(", ");
                } else {
                    throw new RuntimeException("Json Object does not contain a valid key to parse. JS: " + js.toString());
                }
            } else if (obj instanceof String) {
                builder.append((String) obj).append(", ");
            } else {
                throw new RuntimeException("Input Object is not valid. Obj: " + obj);
            }
        }

        return builder.length() > length ? builder.delete(builder.length() - ", ".length(), builder.length()) : builder;
    }

    public static StringBuilder whereStr(JsonObject where, StringBuilder builder, List<String> params) {
        return parseOp(where, builder, params);
    }

    public static String escapeQuote(String s) {
        return "'" + s.replace("'", "\\'") + "'";
    }

    private static StringBuilder values(JsonObject js, StringBuilder builder, List<String> params) {
        Iterator<String> keys = js.getMap().keySet().iterator();
        final int length = builder.length();
        for (; keys.hasNext(); ) {
            String key = keys.next();
            Object obj = js.getValue(key);
            if (obj instanceof String) {
                builder.append("?").append(", ");
                params.add((String) obj);
            } else {
                builder.append(obj).append(", ");
            }
        }
        if (builder.length() > length) {
            builder.delete(builder.length() - ", ".length(), builder.length());
        }
        return builder;
    }

    private static String singleOperand(Object operand, List<String> params) {
        if (operand instanceof String) {
            params.add((String) operand);
            return "?";
        }
        return operand.toString();
    }

    private static StringBuilder parseOp(JsonObject js, StringBuilder builder, List<String> params) {

        int type = js.getInteger("type");

        if (type == OperatorTypes.UNERY_PREFIX) {

            builder.append(js.getString("op")).append(" ");

            if (js.getValue("opnd") instanceof JsonObject) {
                parseOp(js.getJsonObject("opnd"), builder, params);
            } else {
                builder.append(singleOperand(js.getValue("opnd"), params));
            }

        } else if (type == OperatorTypes.BINERY) {

            builder.append("(");
            if (js.getValue("opnd1") instanceof JsonObject) {
                parseOp(js.getJsonObject("opnd1"), builder, params);
            } else {
                builder.append(singleOperand(js.getValue("opnd1"), params));
            }
            builder.append(" ").append(js.getString("op")).append(" ");
            if (js.getValue("opnd2") instanceof JsonObject) {
                parseOp(js.getJsonObject("opnd2"), builder, params);
            } else {
                builder.append(singleOperand(js.getValue("opnd2"), params));
            }
            builder.append(")");
        } else if (type == OperatorTypes.FIELD) {
            builder.append(js.getString("as", "").isEmpty() ? js.getString("name") : js.getString("as") + "." + js.getString("name"));

        } else if (type == OperatorTypes.LIST) {
            builder.append(js.getString("before"));
            String seperator = js.getString("seperator");
            JsonArray list = js.getJsonArray("opnds");
            final int length = builder.length();
            for (int i = 0; i < list.size(); i++) {
                builder.append(singleOperand(list.getValue(i), params)).append(seperator);
            }
            if (builder.length() > length) {
                builder.delete(builder.length() - seperator.length(), builder.length());
            }
            builder.append(js.getString("after"));
        }
        return builder;
    }
}
