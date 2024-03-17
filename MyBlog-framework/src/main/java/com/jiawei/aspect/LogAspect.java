package com.jiawei.aspect;


import com.alibaba.fastjson.JSON;
import com.jiawei.annotation.SystemLog;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

//AOP切面类
@Component
@Aspect
@Slf4j  //添加LomBook的日志输出
public class LogAspect {

    //定义切点
    @Pointcut("@annotation(com.jiawei.annotation.SystemLog)") //使用自定义注解告诉其他的类 日志切面
    public void pc(){
    }

    //定义通知
    //环绕通知
    @Around("pc()") //告诉通知应用的切点是哪一个
    public Object pointLog(ProceedingJoinPoint joinPoint) throws Throwable {

        Object ret;//拿回目标方法掉用的结果
        try {
            //打印日志信息
            handeBefore(joinPoint);
            ret = joinPoint.proceed();
            handeAfter(ret);
        } finally {
            // 结束后换行
            log.info("=======End=======" + System.lineSeparator()); // System.lineSeparator()   获取当前系统的换行符


        }
        return ret;//拿回目标方法掉用的结果  作为  切面的返回值

    }

    //切点前面
    private void handeBefore(ProceedingJoinPoint joinPoint) {
        //获取 spring封装好的   当前的    请求对象request
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        //获取被增强方法上的注解对象
        SystemLog systemLog = getSystemLog(joinPoint);

        //打印日志
        log.info("=======Start=======");
        // 打印请求 URL
        log.info("URL : {}",request.getRequestURL());
        // 打印描述信息
        log.info("BusinessName : {}", systemLog.BusinessName());
        // 打印 Http method
        log.info("HTTP Method : {}",request.getMethod() );
        // 打印调用 controller 的全路径以及执行方法
        log.info("Class Method : {}.{}",
                joinPoint.getSignature().getDeclaringTypeName(),((MethodSignature)joinPoint.getSignature()).getName());
        // 打印请求的 IP
        log.info("IP : {}",request.getRemoteHost());
        // 打印请求入参
        log.info("Request Args : {}", JSON.toJSONString(joinPoint.getArgs()) );

    }


    //切点后
    private void handeAfter(Object ret) {
        // 打印出参
        log.info("Response : {}",JSON.toJSONString(ret) );
    }


    //获取被增强方法上的注解对象
    private SystemLog getSystemLog(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        SystemLog systemLog = methodSignature.getMethod().getAnnotation(SystemLog.class);
        return systemLog;

    }


}
