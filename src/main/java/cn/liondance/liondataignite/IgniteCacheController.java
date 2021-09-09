package cn.liondance.liondataignite;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.CacheWriteSynchronizationMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.transactions.Transaction;
import org.apache.ignite.transactions.TransactionConcurrency;
import org.apache.ignite.transactions.TransactionIsolation;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

/**
 * The type Ignite cache controller.
 */
@Api(tags = "IgniteCache API ")
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/ignite/cache")
public class IgniteCacheController {

    private final Ignite ignite;

    /**
     * Ignite info json object.
     *
     * @return the json object
     * @throws UnsupportedEncodingException the unsupported encoding exception
     */
    @ApiOperation(value = "获取ignite 信息")
    @GetMapping("igniteInfo")
    public JSONObject igniteInfo() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ignite", ignite);
        return jsonObject;
    }

    /**
     * Get json object.
     *
     * @param cache the cache
     * @param key   the key
     * @return the json object
     */
    @ApiOperation(value = "获取缓存信息")
    @GetMapping("get")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cache", value = "缓冲名称"),
            @ApiImplicitParam(name = "key", value = "缓冲key"),
    })
    public JSONObject get(String cache, String key) {
        JSONObject jsonObject = new JSONObject();
        IgniteCache<Object, Object> igniteCache = ignite.getOrCreateCache(cache);
        jsonObject.put("igniteCache", igniteCache.get(key));
        return jsonObject;
    }

    /**
     * Put async json object.
     *
     * @param cache the cache
     * @param key   the key
     * @param value the value
     * @return the json object
     */
    @ApiOperation(value = "异步put 缓存 ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cache", value = "缓冲名称，没有则创建改缓冲"),
            @ApiImplicitParam(name = "key", value = "缓冲key"),
            @ApiImplicitParam(name = "value", value = "缓冲value")
    })
    @PutMapping("putAsync")
    public JSONObject putAsync(String cache, String key, String value) {
        JSONObject jsonObject = new JSONObject();
        IgniteCache<Object, Object> igniteCache = ignite.getOrCreateCache(cache);
        jsonObject.put("igniteCache", igniteCache.putAsync(key, value));
        ignite.compute();
        return jsonObject;
    }

    /**
     * Put json object.
     *
     * @param cache the cache
     * @param key   the key
     * @param value the value
     * @return the json object
     */
    @ApiOperation(value = "put 缓存 ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cache", value = "缓冲名称，没有则创建改缓冲"),
            @ApiImplicitParam(name = "key", value = "缓冲key"),
            @ApiImplicitParam(name = "value", value = "缓冲value")
    })
    @PutMapping("put")
    public JSONObject put(String cache, String key, String value) {
        JSONObject jsonObject = new JSONObject();
        IgniteCache<Object, Object> igniteCache = ignite.getOrCreateCache(cache);
        //  通过使用  ignite.transactions().txStart(TransactionConcurrency, TransactionIsolation); 来创建事务。
        //  其中事务并发模式 TransactionConcurrency 可以是 乐观的OPTIMISTIC 或   悲观  PESSIMISTIC。
        //  事务级别 TransactionIsolation 可以是
        //  读提交  READ-COMMITTED
        //  可重复阅读 REPEATABLE_READ
        //  可序列化  SERIALIZABLE。
        Transaction transaction = ignite.transactions().txStart(TransactionConcurrency.OPTIMISTIC, TransactionIsolation.READ_COMMITTED);
        //事务超时时间
        transaction.timeout(1000L);
        try {
            igniteCache.put(key, value);
            //事务提交
            transaction.commit();
        } catch (Exception e) {
            //事务回滚
            transaction.rollback();
        }
        ignite.compute();
        return jsonObject;
    }

    /**
     * Remove json object.
     *
     * @param cache the cache
     * @param key   the key
     * @return the json object
     */
    @ApiOperation(value = "remove 缓存 ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cache", value = "缓冲名称，没有则创建改缓冲"),
            @ApiImplicitParam(name = "key", value = "缓冲key"),
    })
    @DeleteMapping("remove")
    public JSONObject remove(String cache, String key) {
        JSONObject jsonObject = new JSONObject();
        IgniteCache<Object, Object> igniteCache = ignite.getOrCreateCache(cache);
        //  通过使用  ignite.transactions().txStart(TransactionConcurrency, TransactionIsolation); 来创建事务。
        //  其中事务并发模式 TransactionConcurrency 可以是 乐观的OPTIMISTIC 或   悲观  PESSIMISTIC。
        //  事务级别 TransactionIsolation 可以是
        //  读提交  READ-COMMITTED
        //  可重复阅读 REPEATABLE_READ
        //  可序列化  SERIALIZABLE。
        Transaction transaction = ignite.transactions().txStart(TransactionConcurrency.OPTIMISTIC, TransactionIsolation.READ_COMMITTED);
        //事务超时时间
        transaction.timeout(1000L);
        try {
            igniteCache.remove(key);
            //事务提交
            transaction.commit();
        } catch (Exception e) {
            //事务回滚
            transaction.rollback();
        }
        ignite.compute();
        return jsonObject;
    }

    /**
     * Remove async json object.
     *
     * @param cache the cache
     * @param key   the key
     * @return the json object
     */
    @ApiOperation(value = "removeAsync 缓存 ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cache", value = "缓冲名称，没有则创建改缓冲"),
            @ApiImplicitParam(name = "key", value = "缓冲key"),
    })
    @DeleteMapping("removeAsync")
    public JSONObject removeAsync(String cache, String key) {
        JSONObject jsonObject = new JSONObject();
        IgniteCache<Object, Object> igniteCache = ignite.getOrCreateCache(cache);
        //  通过使用  ignite.transactions().txStart(TransactionConcurrency, TransactionIsolation); 来创建事务。
        //  其中事务并发模式 TransactionConcurrency 可以是 乐观的OPTIMISTIC 或   悲观  PESSIMISTIC。
        //  事务级别 TransactionIsolation 可以是
        //  读提交  READ-COMMITTED
        //  可重复阅读 REPEATABLE_READ
        //  可序列化  SERIALIZABLE。
        Transaction transaction = ignite.transactions().txStart(TransactionConcurrency.OPTIMISTIC, TransactionIsolation.READ_COMMITTED);
        //事务超时时间
        transaction.timeout(1000L);
        try {
            igniteCache.removeAsync(key);
            //事务提交
            transaction.commit();
        } catch (Exception e) {
            //事务回滚
            transaction.rollback();
        }
        ignite.compute();
        return jsonObject;
    }


    /**
     * Gets or create cache.
     *
     * @param cache the cache
     * @return the or create cache
     */
    @ApiOperation(value = "获取或创建缓存")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cache", value = "缓冲名称，没有则创建改缓冲")
    })
    @PutMapping("getOrCreateCache")
    public JSONObject getOrCreateCache(String cache) {
        JSONObject jsonObject = new JSONObject();
        CacheConfiguration cacheCfg = new CacheConfiguration()
                .setBackups(2)
                .setName(cache)
                // 如果缓存为AtomicityMode.TRANSACTIONAL模式，则客户端支持事务。
                .setCacheMode(CacheMode.REPLICATED)
                .setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL)
                .setWriteSynchronizationMode(CacheWriteSynchronizationMode.FULL_SYNC);
        IgniteCache<Object, Object> igniteCache = ignite.getOrCreateCache(cacheCfg);
        jsonObject.put("igniteCache", igniteCache);
        ignite.compute();
        return jsonObject;
    }


    /**
     * Remove async json object.
     *
     * @param cache the cache
     * @return the json object
     */
    @ApiOperation(value = "销毁缓存")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cache", value = "缓冲名称，没有则创建改缓冲"),
    })
    @DeleteMapping("destroy")
    public JSONObject destroy(String cache) {
        JSONObject jsonObject = new JSONObject();
        IgniteCache<Object, Object> igniteCache = ignite.getOrCreateCache(cache);
        //  通过使用  ignite.transactions().txStart(TransactionConcurrency, TransactionIsolation); 来创建事务。
        //  其中事务并发模式 TransactionConcurrency 可以是 乐观的OPTIMISTIC 或   悲观  PESSIMISTIC。
        //  事务级别 TransactionIsolation 可以是
        //  读提交  READ-COMMITTED
        //  可重复阅读 REPEATABLE_READ
        //  可序列化  SERIALIZABLE。
        Transaction transaction = ignite.transactions().txStart(TransactionConcurrency.OPTIMISTIC, TransactionIsolation.READ_COMMITTED);
        //事务超时时间
        transaction.timeout(1000L);
        try {
            igniteCache.destroy();
            //事务提交
            transaction.commit();
        } catch (Exception e) {
            //事务回滚
            transaction.rollback();
        }
        ignite.compute();
        return jsonObject;
    }

}
