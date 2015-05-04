package com.chttl.jee.util.io;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.regex.Pattern;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

public class AnnotationScanner {

	protected Reflections reflections ;

    public AnnotationScanner(String packageName){
        reflections = new Reflections(new ConfigurationBuilder()
                .addClassLoader(Thread.currentThread().getContextClassLoader())
                .filterInputsBy(new FilterBuilder().includePackage(packageName))
                .addUrls(ClasspathHelper.forPackage(packageName))
                .setScanners(new ResourcesScanner(), 
                             new TypeAnnotationsScanner(), 
                             new SubTypesScanner()));

    }
    
    public Set<Class<?>> getAllClassAnnotated(Class<? extends Annotation> annotation)
    {
        return reflections.getTypesAnnotatedWith(annotation);
    }
    
    public <T> Set<Class<? extends T>> getSubTypesOf(Class<T> parentType)
    {
        return reflections.getSubTypesOf(parentType);
    }
    
    public Set<String> getAllResources(String regexpattern)
    {
        return reflections.getResources(Pattern.compile(regexpattern));
    }
	
}