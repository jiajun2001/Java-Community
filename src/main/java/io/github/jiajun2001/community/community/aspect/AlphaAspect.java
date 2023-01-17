package io.github.jiajun2001.community.community.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

//@Component
//@Aspect
public class AlphaAspect {

    @Pointcut("execution(* io.github.jiajun2001.community.community.service.*.*(..))")
    public void pointcut() {

    }

    @Before("pointcut()")
    public void before() {
        System.out.println("Before");
    }

    @After("pointcut()")
    public void after() {
        System.out.println("After");
    }

    @AfterReturning("pointcut()")
    public void afterReturning() {
        System.out.println("After Returning");
    }

    @AfterThrowing("pointcut()")
    public void afterThrowing() {
        System.out.println("After Throwing");
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("Around Before");
        Object obj = joinPoint.proceed();
        System.out.println("Around After");
        return obj;
    }
}













