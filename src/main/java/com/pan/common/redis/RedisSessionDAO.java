package com.pan.common.redis;

import com.pan.common.constants.RedisConstants;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis实现共享session
 */
@Component
public class RedisSessionDAO extends AbstractSessionDAO {

    private static Logger logger = LoggerFactory.getLogger(RedisSessionDAO.class);

    // session 在redis过期时间是30分钟30*60
    @Value("${shiro.globalSessionTimeout}")
    //设置30分钟
    private Integer expireTime;

    private static String prefix = RedisConstants.REDIS_SHIRO_SESSION;

    public RedisSessionDAO() {
        super();
    }

    @Autowired
    private RedisTemplate redisTemplate;

    /** 创建session，保存到数据库 */
    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        //Serializable sessionId = super.doCreate(session);
        logger.debug("创建session:{}", session.getId());
        try {
            String key = prefix + sessionId.toString();
            //毫秒
            redisTemplate.opsForValue().set(key, session,expireTime,TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            logger.error("redis获取session异常："+e.getMessage(),e);
        }
        return sessionId;
    }


    /**
     * 更新session
     * @param session
     * @throws UnknownSessionException
     */
    @Override
    public void update(Session session) throws UnknownSessionException {
        logger.debug("更新session:{}", session.getId());
        try {
            //如果会话过期/停止 没必要再更新了
            //这里很重要，不然导致每次接口调用完毕后，由于会话结束，导致更新了空的session到redis了。导致源码老报错session不存在
            if (session instanceof ValidatingSession && !((ValidatingSession) session).isValid()) {
                return;
            }

            String key = prefix + session.getId().toString();
            redisTemplate.opsForValue().set(key, session,expireTime,TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            logger.error("redis更新session异常："+e.getMessage(),e);
        }
    }

    /**
     * 删除session
     * @param session
     */
    @Override
    public void delete(Session session) {
        logger.debug("删除session:{}", session.getId());
        try {
            String key = prefix + session.getId().toString();
            redisTemplate.delete(key);
        } catch (Exception e) {
            logger.error("redis删除session异常："+e.getMessage(),e);
        }
    }

    @Override
    public Collection<Session> getActiveSessions() {
        logger.info("获取存活的session");
        Set<Session> sessions = new HashSet<>();
        Set<String> keys = redisTemplate.keys(prefix+"*");
        if (keys != null && keys.size() > 0) {
            for (String key : keys) {
                Session session = (Session) redisTemplate.opsForValue().get(key);
                sessions.add(session);
            }
        }
        return sessions;
    }

    /** 获取session */
    @Override
    protected Session doReadSession(Serializable sessionId) {
        logger.debug("获取session:{}", sessionId.toString());
        Session session = null;
        try {
            String key = prefix + sessionId.toString();
            session = (Session) redisTemplate.opsForValue().get(key);
            if(session!=null){
                // session没过期，则刷新过期时间
                //毫秒
                redisTemplate.boundValueOps(key).expire(expireTime, TimeUnit.MILLISECONDS);
            }
        } catch (Exception e) {
            logger.error("redis获取session异常："+e.getMessage(),e);
        }
        return session;
    }
}
