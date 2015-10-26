package com.gnatiuk.searcher.ui.utils.filters.components.builders;

import com.gnatiuk.searcher.filters.ATextFilter;
import com.gnatiuk.searcher.filters.IFilter;
import com.gnatiuk.searcher.filters.text_processors.ITextPreprocessor;
import com.gnatiuk.searcher.ui.utils.filters.components.ASearchTextFilterComponent;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


/**
 * Created by sgnatiuk on 9/23/15.
 */
public class TextSearchComponentBuilder extends ASearchComponentBuilder {
    @Override
    public ASearchTextFilterComponent buildByFilter(IFilter filter) {
        ATextFilter filterFileName = (ATextFilter) filter;

        ASearchTextFilterComponent textFilterComponent = buildTextFilterComponent(filter);
        textFilterComponent.addKeywords(filterFileName.getKeywords());
        if(filterFileName.getTextPreprocessor().getClass() == ITextPreprocessor.LOWERCASE_PROCESSOR.getClass()){
            textFilterComponent.setIgnoreCase(true);
        }else if(filterFileName.getTextPreprocessor().getClass() == ITextPreprocessor.NONE_PROCESSOR.getClass()){
            textFilterComponent.setIgnoreCase(false);
        }else{
            throw new RuntimeException(String.format("Unknown text processor %s", filterFileName.getTextPreprocessor().getClass().getName()));
        }
        textFilterComponent.setRegexCheck(false);
        return textFilterComponent;
    }

    private ASearchTextFilterComponent buildTextFilterComponent(IFilter filter) {
        if(!(filter instanceof ATextFilter)){
            throw new IllegalArgumentException(String.format("'%s' is not instance of %s", filter.getClass().getName(),
                    ATextFilter.class.getName()));
        }
        Class<? extends ASearchTextFilterComponent> classObj =
                (Class<? extends ASearchTextFilterComponent>)FilterToComponentMap.getFilterComponentByFilterClass(filter);
        try {
            //get default constructor
            Constructor<? extends ASearchTextFilterComponent> constructor = classObj.getConstructor();
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
