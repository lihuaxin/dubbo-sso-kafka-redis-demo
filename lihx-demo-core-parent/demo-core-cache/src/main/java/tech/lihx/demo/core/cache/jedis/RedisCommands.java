package tech.lihx.demo.core.cache.jedis;

import redis.clients.jedis.BinaryJedisCommands;
import redis.clients.jedis.JedisCommands;

/**
 * redis访问接口
 * <p>
 * 
 * @author lihx
 * @Date 2014-7-18
 */
public interface RedisCommands extends JedisCommands, BinaryJedisCommands {

}
