package org.elf.framework.core.annotation;

@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Target({java.lang.annotation.ElementType.FIELD})
public @interface Column {

    String name();

    boolean primary() default false;
}