package com.punwire.oa.core;

import javax.persistence.Query;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kanwal on 1/30/14.
 */
public class OaJpaMapper extends OaResultMapper {

    @SuppressWarnings("unchecked")
    public <T> List<T> getList(Query q, Class<T> clazz) throws IllegalArgumentException {
        List<T> result = new ArrayList<T>();
        Constructor<?> construct = (Constructor<?>) clazz.getDeclaredConstructors()[0];
        List<Object[]> list = q.getResultList();
        for (Object obj : list) {
            if (construct.getParameterTypes().length == 1) {
                obj = new Object[]{obj};
            }
            result.add((T) newInstance(construct, (Object[]) obj));
        }
        return result;
    }

    public <T> T getSingle(Query q, Class<T> clazz) {
        Object rec = q.getSingleResult();
        Constructor<?> ctor = (Constructor<?>) clazz.getDeclaredConstructors()[0];
        if (ctor.getParameterTypes().length == 1) {
            rec = new Object[]{rec};
        }
        return newInstance(ctor, (Object[]) rec);
    }


}
