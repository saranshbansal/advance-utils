/**
 *
 */
package com.advanceutils.util.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * This AOP class will log the time for execution of every single method in the project.
 *
 * @author sbansal
 */
@Component
@Aspect 
public class AOPTimeLogger {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Around("execution(* com.advanceutils.util..*.*(..))")
    public Object timeMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch ticktock = new StopWatch();
        ticktock.start();
        // Method execution begins
        Object retVal = joinPoint.proceed();
        // Method execution ends
        ticktock.stop();
        // Log message
        StringBuffer logMessage = new StringBuffer();
        logMessage.append(joinPoint.getTarget().getClass().getName());
        logMessage.append(".");
        logMessage.append(joinPoint.getSignature().getName());
        logMessage.append("(");
        // append args
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            logMessage.append(arg).append(",");
        }
        if (args.length > 0) {
            logMessage.deleteCharAt(logMessage.length() - 1);
        }
        logMessage.append(")");
        logMessage.append(" execution time: ");
        logMessage.append(ticktock.getTotalTimeMillis());
        logMessage.append(" ms");
        logger.info(logMessage.toString());
        return retVal;
    }
}
