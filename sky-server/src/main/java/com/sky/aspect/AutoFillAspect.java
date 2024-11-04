package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 自定义切面类，实现公共字段自动填充的处理逻辑
 */

@Aspect
@Component
@Slf4j
public class AutoFillAspect {

    /**
     * 切入点
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut() {
    }

    /**
     * 前置通知，在通知中进行公共字段的赋值
     */
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("开始进行公共字段的自动填充...");

        //1. 获取当前被拦截到的方法的数据库操作类型 (update or insert ?)

        // 获取方法签名对象
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取方法上的注解对象
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);
        // 获得数据库操作类型
        OperationType operationType = autoFill.value();

        //2. 获取方法中的参数--实体对象
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return;
        }
        // 获得实体对象
        Object entity = args[0];
        //3. 准备要赋值的数据
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

        //4. 为公共属性统一赋值，为对应的属性通过反射来赋值
        setCommentFields(entity, operationType, currentId, now);
    }

    private void setCommentFields(Object entity, OperationType operationType, Long currentId, LocalDateTime now) {
        // 根据操作类型选择需要设置的方法
        // insert 对应4个要执行, update 对应2个要执行
        // 暂时没有新增的类型要处理因此设计这样，有新增再修改逻辑为每个类型对应一个if-else代码块
        try {
            if (operationType == OperationType.INSERT) {
                invokeSetMethod(entity, AutoFillConstant.SET_CREATE_TIME, now);
                invokeSetMethod(entity, AutoFillConstant.SET_CREATE_USER, currentId);
            }
            invokeSetMethod(entity, AutoFillConstant.SET_UPDATE_TIME, now);
            invokeSetMethod(entity, AutoFillConstant.SET_UPDATE_USER, currentId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void invokeSetMethod(Object entity, String methodName, Object value) throws Exception {
        Method method = entity.getClass().getDeclaredMethod(methodName, value.getClass());
        method.invoke(entity, value);
    }

}
