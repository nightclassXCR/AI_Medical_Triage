package com.dd.ai_medical_triage.aspect;


import com.dd.ai_medical_triage.annatation.NoRepeatSubmit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.annotation.Order;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Aspect
@Component
@Order(1)
public class NoRepeatSubmitAspect {

    @Autowired
    private RedissonClient redissonClient;

    // SpEL 解析器，用于解析 key 中的表达式
    private final ExpressionParser parser = new SpelExpressionParser();
    private final DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    @Around("@annotation(noRepeatSubmit)")
    public Object around(ProceedingJoinPoint joinPoint, NoRepeatSubmit noRepeatSubmit) throws Throwable {

        // 1. 解析 SpEL 表达式，生成最终的 Redis Key
        String lockKey = generateKeyBySpEL(noRepeatSubmit.key(), joinPoint);

        // 2. 获取 Redisson 锁对象
        RLock lock = redissonClient.getLock(lockKey);

        boolean isLocked = false;
        try {
            // 3. 尝试加锁
            isLocked = lock.tryLock(noRepeatSubmit.waitTime(), noRepeatSubmit.lockTime(), TimeUnit.SECONDS);

            if (!isLocked) {
                // 4. 加锁失败，直接抛出异常，阻断业务执行
                // 建议抛出自定义异常，由全局异常处理器捕获并返回前端 JSON
                // todo 自定义异常,哪个是自定义业务异常
                throw new RuntimeException(noRepeatSubmit.message());
            }

            // 5. 加锁成功，执行目标业务方法
            return joinPoint.proceed();

        } finally {
            // 6. 释放锁
            // 只有当前线程持有的锁，才能释放
            if (isLocked && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    /**
     * SpEL 表达式解析工具方法
     * 作用：将注解里的 "#req.userId" 翻译成具体的值 "1001"
     */
    private String generateKeyBySpEL(String spELString, ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] paramNames = nameDiscoverer.getParameterNames(signature.getMethod());
        Object[] args = joinPoint.getArgs();

        EvaluationContext context = new StandardEvaluationContext();
        if (paramNames != null) {
            for (int i = 0; i < args.length; i++) {
                context.setVariable(paramNames[i], args[i]);
            }
        }

        Expression expression = parser.parseExpression(spELString);
        return expression.getValue(context, String.class);
    }
}
