package com.nowcoder.community.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

/**
 * ClassName: AlphaAspect
 * Package: com.nowcoder.community.aspect
 * Description:
 *
 * @Author CC
 * @Create 2023/8/18 10:53
 * @Version 1.0
 */
//@Aspect
//@Component
public class AlphaAspect {

    //定义切点（连接点）
    //第一个*表示方法的返回值可以是任何类型，作用于所有service类中的所有方法，（..）表示接受任何参数
    //定义切点（连接点）
    @Pointcut("execution(* com.nowcoder.community.service.*.*(..))")
    public void pointcut() {

    }

    //定义通知（分为5类）
    @Before("pointcut()")
    public void before() {
        System.out.println("before");
    }

    @After("pointcut()")
    public void after() {
        System.out.println("after");
    }

    @AfterReturning("pointcut()")
    public void afterReturing() {
        System.out.println("afterReturing");
    }

    @AfterThrowing("pointcut()")
    public void afterThrowing() {
        System.out.println("afterThrowing");
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("aroundBefore");
        Object obj = joinPoint.proceed();//调用原始方法
        System.out.println("aroundAfter");
        return obj;
    }
}
