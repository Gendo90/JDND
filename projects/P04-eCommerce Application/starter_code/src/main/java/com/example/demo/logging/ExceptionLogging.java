package com.example.demo.logging;

import org.apache.log4j.Logger;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExceptionLogging {
	
	private static final Logger log = Logger.getLogger(ExceptionLogging.class);
	
	@AfterThrowing(pointcut="execution(public * com.example.demo.security.*.*(..))", throwing="e")
    public void securityLogAfterThrowing(Throwable e) {
		System.out.println("Security Issue!");
        log.error(e.getMessage());
    }
	
	@AfterThrowing(pointcut="execution(public * com.example.demo.controllers.*.*(..))", throwing="e")
    public void controllerLogAfterThrowing(Throwable e) {
		System.out.println("Controller Issue!");
        log.error(e.getMessage());
    }
}