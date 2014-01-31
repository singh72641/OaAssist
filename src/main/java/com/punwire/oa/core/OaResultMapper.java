package com.punwire.oa.core;

import java.lang.reflect.Constructor;

/**
 * Created by kanwal on 1/30/14.
 */
public class OaResultMapper {

    @SuppressWarnings(value = "unchecked")
    protected <T> T newInstance(Constructor<?> construct, Object[] args) {
        try {
            return (T) construct.newInstance(args);
        } catch (IllegalArgumentException e) {
            StringBuilder sb = new StringBuilder("Constructor not found for :\n");
            for (Object object : args) {
                sb.append("\t").append(object.getClass().getName()).append("\n");
            }
            throw new RuntimeException(sb.toString(), e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
