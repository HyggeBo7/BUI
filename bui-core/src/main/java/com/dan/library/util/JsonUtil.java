package com.dan.library.util;

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

    private static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    public static <T> T fromJson(String json, Class<T> cls) {

        if (StringUtils.isBlank(json)) {
            return null;
        }
        return gson.fromJson(json, cls);
    }

    /**
     * 转成list
     *
     * @param json json字符串
     * @param cls  类
     * @return 返回泛型
     */
    public static <T> List<T> fromListJson(String json, Class<T> cls) {
        List<T> list = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for (final JsonElement elem : array) {
            list.add(gson.fromJson(elem, cls));
        }
        return list;
    }

    /**
     * 将object对象转成json字符串
     *
     * @param obj 对象
     * @return json String
     */
    public static String toJson(Object obj) {
        return gson.toJson(obj);
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
        return gson.fromJson(json, objectType);
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
