package net.zdsoft.framework.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author shenke
 * @since 2017.08.24
 */
@Configuration
@PropertySource("classpath:/conf/redis.properties")
//@EnableCache(advice = Advice.PROXY)
public class RedisConfig {

    @Value("${redis.ip}")
    private String host;
    @Value("${redis.port}")
    private int port;
    @Value("${redis.pass}")
    private String password;

    //@Bean(name = "dynamicCacheClassFilter")
    //public DynamicCacheClassFilter classFilter () {
    //    return new DynamicCacheClassFilter() {
    //        @Override
    //        public boolean matches(Class<?> aClass) {
    //            if ( aClass == null ) {
    //                return false;
    //            }
    //            if ( Object.class.equals(aClass.getSuperclass())
    //                    && "net.zdsoft.system.service.mcode.impl.McodeDetailServiceImpl".equals(aClass.getName())) {
    //                return true;
    //            } else if (aClass.getSuperclass() != null
    //                    && "net.zdsoft.basedata.service.impl.BaseServiceImpl".equals(aClass.getSuperclass().getName()) ) {
    //                return true;
    //            } else {
    //                return false;
    //            }
    //        }
    //    };
    //}
    //
    ///**
    // * 此处为了解决因为基类泛型方法而无法获取实际类型的问题<br>
    // * 若是基本类型，则重新构建类型信息
    // */
    //@Bean
    //public TypeDescriptor typeDescriptor() {
    //    return new TypeDescriptor() {
    //        private Logger logger = Logger.getLogger("net.zdsoft.framework.config.RedisConfig$TypeDescriptor");
    //        private Map<MethodClassKey, TypeBuilder> typeMap = new ConcurrentHashMap<>();
    //
    //        /**
    //         * 该方法实现取决于基类方法的返回类型，若基类方法的返回类型是Map<String,List<T>> 则该方法就应该构建这个Map的实际类型信息
    //         * @param invocation
    //         * @param targetClass 实际的对象类型信息，不要从invocation获取，可能是代理对象
    //         * @return
    //         */
    //        @Override
    //        public TypeBuilder buildType(MethodInvocation invocation, Class<?> targetClass) {
    //            TypeBuilder builder = TypeBuilder.build();
    //            Type type = invocation.getMethod().getGenericReturnType();
    //
    //            MethodClassKey methodClassKey = new MethodClassKey(invocation.getMethod(), targetClass);
    //            if ( typeMap.get(methodClassKey) != null ) {
    //                return typeMap.get(methodClassKey);
    //            }
    //            //泛型类型
    //            if ( type instanceof TypeVariable ) {
    //                TypeVariableImpl typeInfo = (TypeVariableImpl)type;
    //                typeInfo.getBounds();
    //                Type returnType = invocation.getMethod().getReturnType();
    //                if ( BaseEntity.class.equals(returnType) ) {
    //                    builder.buildRowType(BeanUtils.getFirstGenericType(targetClass));
    //                } else {
    //                    logger.error("无法处理的类型信息，" + targetClass.getName() + "#" + invocation.getMethod().getName());
    //                    throw new IllegalArgumentException("无法处理的类型信息，" + targetClass.getName() + "#" + invocation.getMethod().getName());
    //                }
    //            }
    //            else if ( Map.class.equals(BeanUtils.getRowType(type))
    //                    || List.class.equals(BeanUtils.getRowType(type)) ) {
    //                List<Type> argumentsType = BeanUtils.getActualTypeArguments(type);
    //                List<Type> reArgumentsType = Lists.newArrayList();
    //                for (Type t : argumentsType) {
    //                    if ( t instanceof TypeVariable ) {
    //                        if ( ((TypeVariable)t).getName().equals("K") ) {
    //                            reArgumentsType.add(BeanUtils.getGenericType(targetClass, 2));
    //                        } else {
    //                            reArgumentsType.add(BeanUtils.getGenericType(targetClass, 1));
    //                        }
    //                    } else {
    //                        reArgumentsType.add(t);
    //                    }
    //                }
    //                builder.buildRowType(BeanUtils.getRowType(type)).buildArgumentType(reArgumentsType.toArray(new Type[reArgumentsType.size()]));
    //            }
    //            //other
    //            else {
    //                builder.buildRowType(type);
    //            }
    //            typeMap.put(methodClassKey, builder);
    //            return builder;
    //        }
    //
    //    };
    //}
    //
    //@Bean
    //public CacheManager registerCacheManager(RedisTemplate redisTemplate) {
    //    return new RedisCacheManager(redisTemplate);
    //}

    @Bean
    public RedisConnectionFactory redisConnectionFactory(){
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName(host);
        jedisConnectionFactory.setPort(port);
        if (StringUtils.isNotBlank(password) ) {
            jedisConnectionFactory.setPassword(password);
        }
        return jedisConnectionFactory;
    }

    @Bean(name = "redisTemplate")
    public RedisTemplate<String,String> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String,String> redisTemplate = new RedisTemplate<String, String>();
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(stringRedisSerializer);
        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }
}
