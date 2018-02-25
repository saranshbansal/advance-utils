/**
 *
 */
package com.advanceutils.util.aoputils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * This AOP class will automatically log the entry and exit of all the methods in the project.
 *
 * @author sbansal
 */
@Component
@Aspect
public class AOPAutoLogger {
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Around("execution(* com.advanceutils.util..*.*(..)) && !@annotation(com.advanceutils.util.aoputils.AOPKiller)")
  public Object autoLog(ProceedingJoinPoint joinPoint) throws Throwable {
    Object retVal = null;
    try {
      StringBuilder startMessageLogger = new StringBuilder();
      startMessageLogger.append("Entry - > ");
      startMessageLogger.append(joinPoint.getTarget().getClass().getName());
      startMessageLogger.append(" - ");
      startMessageLogger.append(joinPoint.getSignature().getName() + "()");
      logger.info(startMessageLogger.toString());
      // Method execution begins
      retVal = joinPoint.proceed();
      // Method execution ends
      StringBuilder endMessageLogger = new StringBuilder();
      endMessageLogger.append("Exit - > ");
      endMessageLogger.append(joinPoint.getTarget().getClass().getName());
      endMessageLogger.append(" - ");
      endMessageLogger.append(joinPoint.getSignature().getName() + "()");
      logger.info(endMessageLogger.toString());
    } catch (Exception ex) {
      StringBuilder errorMessageStringBuilder = new StringBuilder();
      errorMessageStringBuilder.append("Exception cause: " + ex.getMessage());
      // Create error message
      logger.error(errorMessageStringBuilder.toString(), ex);
    }
    return retVal;
  }
}
