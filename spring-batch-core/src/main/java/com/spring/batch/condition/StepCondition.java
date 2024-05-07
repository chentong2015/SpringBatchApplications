package com.spring.batch.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

// 配置Step执行的条件Condition
public class StepCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        // context.getEnvironment().getActiveProfiles();
        return true;
    }
}
