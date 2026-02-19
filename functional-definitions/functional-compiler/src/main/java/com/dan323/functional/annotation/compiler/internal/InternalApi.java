package com.dan323.functional.annotation.compiler.internal;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a public type or method as internal API, not intended for public use.
 * Internal APIs are subject to change without notice between versions and should
 * not be relied upon by library users. They are exposed as public only because
 * they are needed for annotation processing or internal compiler operations.
 *
 * @since 2.0
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PACKAGE})
@Retention(RetentionPolicy.RUNTIME)
public @interface InternalApi {
    /**
     * Optional description of why this is internal.
     */
    String value() default "Internal API - not intended for public use";
}

