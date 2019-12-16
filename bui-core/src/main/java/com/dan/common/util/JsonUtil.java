package com.dan.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @fileName: JsonUtil
 * @author: Bo
 * @createDate: 2018-04-11 9:37.
 * @description: json工具类
 */
public class JsonUtil {

    private static final String TAG = "JsonUtil";

    private final static Gson GSON;

    static {
        GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    }

    public static Gson getGson() {
        return GSON;
    }

    public static <T> T fromJson(String json, Class<T> cls) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        return GSON.fromJson(json, cls);
    }

    public static <T> T fromTypeJson(final String json, final Type typeOfT) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        return GSON.fromJson(json, typeOfT);
    }

    /**
     * 转成list
     * Deprecated->fromListJsonType()
     *
     * @param json json字符串
     * @param cls  类
     * @return 返回泛型
     */
    @Deprecated
    public static <T> List<T> fromListJson(String json, Class<T> cls) {
        List<T> list = new ArrayList<T>();
        //JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        JsonArray array = JsonParser.parseString(json).getAsJsonArray();
        for (final JsonElement elem : array) {
            list.add(GSON.fromJson(elem, cls));
        }
        return list;
    }

    public static <T> List<T> fromListJsonType(final String json, final Class<T> clazz) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        return GSON.fromJson(json, TypeToken.getParameterized(List.class, clazz).getType());
    }

    /**
     * 将object对象转成json字符串
     *
     * @param obj 对象
     * @return json String
     */
    public static String toJson(Object obj) {
        return GSON.toJson(obj);
    }

    /**
     * 字符串转对象
     *
     * @param json      字符串
     * @param clazzType 转换的对象:Pagination.class <T>
     * @param clazz     泛型类型: User.cass
     * @param <T>       Pagination<User>
     * @return Pagination<User>
     */
    public static <T> T fromGenericJson(String json, Class clazzType, Class clazz) {
        Type objectType = type(clazzType, clazz);
        return GSON.fromJson(json, objectType);
    }

    class IntegerDefault0Adapter implements JsonSerializer<Integer>, JsonDeserializer<Integer> {
        @Override
        public Integer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            try {
                //定义为int类型,如果后台返回""或者null,则返回0
                if (json.getAsString().equals("") || json.getAsString().equals("null")) {
                    return null;
                }
            } catch (Exception ignore) {
            }
            try {
                return json.getAsInt();
            } catch (NumberFormatException e) {
                throw new JsonSyntaxException(e);
            }
        }

        @Override
        public JsonElement serialize(Integer src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src);
        }
    }

    static ParameterizedType type(final Class raw, final Type... args) {
        return new ParameterizedType() {
            @Override
            public Type getRawType() {
                return raw;
            }

            @Override
            public Type[] getActualTypeArguments() {
                return args;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };
    }

}
