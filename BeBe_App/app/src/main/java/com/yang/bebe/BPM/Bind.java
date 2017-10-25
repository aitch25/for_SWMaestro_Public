package com.yang.bebe.BPM;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Created by jy on 2017-01-19.
 */
@Retention(CLASS) @Target(FIELD)
public @interface Bind {
    /** View ID to which the field will be bound. */
    int[] value();
}
