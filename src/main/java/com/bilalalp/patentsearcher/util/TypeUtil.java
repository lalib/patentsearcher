package com.bilalalp.patentsearcher.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class TypeUtil {

    private TypeUtil() {
        //Util Class
    }

    public static <T> Class<T> getParametrizedType(Class<?> baseClass) {
        final Type genericSuperClass = baseClass.getGenericSuperclass();

        if (!(genericSuperClass instanceof ParameterizedType)) {
            throw new IllegalArgumentException("Your super class need to have a parameterized type");
        }

        final ParameterizedType parameterizedType = (ParameterizedType) genericSuperClass;
        final Type[] types = parameterizedType.getActualTypeArguments();

        if (types[0] instanceof Class<?>) {
            return (Class<T>) types[0];
        } else {
            throw new IllegalArgumentException("Parameter type is mismatched");
        }
    }
}