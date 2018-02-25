package com.advanceutils.util.aoputils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation will explicitely disable all AOP functionality on selected methods.
 * How to use: Just place it over any method in your class after passing it to the advice such as below:
 * @Around("execution(* com.advanceutils.util..*.*(..)) && !@annotation(com.advanceutils.util.aoputils.AOPKiller))"
 * @author sbansal
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface AOPKiller {

}
