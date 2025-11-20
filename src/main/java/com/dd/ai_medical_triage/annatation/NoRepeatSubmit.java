package com.dd.ai_medical_triage.annatation;

import java.lang.annotation.*;

@Target(ElementType.METHOD) // 作用在方法上
@Retention(RetentionPolicy.RUNTIME) // 运行时有效
@Documented
public @interface NoRepeatSubmit {

    /**
     * 锁的 Key，支持 SpEL 表达式
     * 例如："'order:' + #user.id"
     */
    String key();

    /**
     * 锁的过期时间 (秒)，默认 5 秒
     * -1 代表使用看门狗自动续期
     */
    long lockTime() default 5;

    /**
     * 获取锁等待时间 (秒)，默认 0 (不等待，立即失败)
     */
    long waitTime() default 0;

    /**
     * 获取锁失败时的提示信息
     */
    String message() default "操作太频繁，请稍后再试";
}
