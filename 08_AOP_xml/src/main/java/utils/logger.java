package utils;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 用于记录日志的工具类，提供了公共的代码
 */
public class logger {
    /**
     * 让其在切入点方法执行之前执行
     */
    public void beforePrintLog(){
        System.out.println("前置通知...");
    }

    public void afterReturningPrintLog(){
        System.out.println("后置通知");
    }

    public void afterThrowingPrintLog(){
        System.out.println("异常通知...");
    }

    public void afterPrintLog(){
        System.out.println("最终通知...");
    }

    public Object aroundPrintLog(ProceedingJoinPoint pjp){
        Object rtValue = null;
        try {
            Object[] args = pjp.getArgs();
            System.out.println("前置通知...");
            rtValue = pjp.proceed(args);
            System.out.println("后置通知...");
            return rtValue;
        } catch (Throwable e) {
            e.printStackTrace();
            System.out.println("异常通知...");
        } finally {
            System.out.println("最终通知...");
            return rtValue;
        }
    }
}
