package org.jplus.robot;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hyberbin on 2017/5/3.
 */
public class ELUtils {
    private static final Logger log = LoggerFactory.getLogger(ELUtils.class);
    /**
     * 获取EL表达式的值
     * @param args
     * @param paraNames
     * @param expression
     * @param beanFactory
     * @return
     */
    public static Object getSpelValue(Object[] args, String[] paraNames, String expression, BeanFactory beanFactory) {
        if(StringUtils.isBlank(expression)){
            return null;
        }
        ExpressionParser ep = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        if (beanFactory != null) {
            context.setBeanResolver(new BeanFactoryResolver(beanFactory));
        }

        if (!ArrayUtils.isEmpty(args) && !ArrayUtils.isEmpty(paraNames)) {
            if (args.length != paraNames.length) {
                throw new IllegalArgumentException("args length must be equal to paraNames length");
            }

            for (int i = 0; i < paraNames.length; i++) {
                context.setVariable(paraNames[i], args[i]);
            }
        }
        try {
            context.setVariables(SpringContextUtil.getBeanMap());
            log.info("setVariables :{}",StringUtils.join(SpringContextUtil.getBeanMap().keySet()));
            return ep.parseExpression(expression).getValue(context);
        }catch (Exception e){
            log.error("运算SPEL表达式：{}出错！",expression,e);
        }
        return null;
    }

    public static Object getSpelValue(String expression){
        return getSpelValue(null,null,expression,SpringContextUtil.getApplicationContext());
    }
    /**
     * 通过EL表达式给对象赋值
     * @param invocation
     * @param paraNames
     * @param expression
     * @param value
     * @param beanFactory
     */
    public static void setSpelValue(MethodInvocation invocation, String[] paraNames, String expression, Object value, BeanFactory beanFactory){
        Assert.hasText(expression);
        Object[] args = invocation.getArguments();
        Map<String,Object> map=new HashMap();
        if (!ArrayUtils.isEmpty(args) && !ArrayUtils.isEmpty(paraNames)) {
            if (args.length != paraNames.length) {
                throw new IllegalArgumentException("args length must be equal to paraNames length");
            }
            for (int i = 0; i < paraNames.length; i++) {
                map.put(paraNames[i], args[i]);
            }
        }
        ExpressionParser ep = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        if (beanFactory != null) {
            context.setBeanResolver(new BeanFactoryResolver(beanFactory));
        }
        context.addPropertyAccessor(new MapAccessor());
        context.setVariable("argMap", map);
        expression=expression.replace("#","#argMap.");
        ep.parseExpression(expression).setValue(context,value);
        for (int i=0;i<args.length;i++){
            args[i]=map.get(paraNames[i]);
        }
    }
}
