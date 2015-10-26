package com.gnatiuk.searcher.ui.utils.filters.components.builders;

import com.gnatiuk.searcher.filters.IFilter;
import com.gnatiuk.searcher.ui.utils.filters.components.ASearchFilterComponent;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by sgnatiuk on 9/23/15.
 */
public abstract class ASearchComponentBuilder {

    public abstract ASearchFilterComponent buildByFilter(IFilter filter);

    public static ASearchFilterComponent build(Class<? extends ASearchFilterComponent> classObj) {
        try {
            //get default constructor
            Constructor<? extends ASearchFilterComponent> constructor = classObj.getConstructor();
            return constructor.newInstance();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        throw new IllegalArgumentException(String.format("Problems during creating an instance using" +
                " '%s' Class object", classObj));
    }
}
