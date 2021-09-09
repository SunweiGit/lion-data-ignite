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
import org.apache.ignite.cache.query.FieldsQueryCursor;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "ignite SQL")
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/ignite/sql")
public class IgniteSQLController {


    private final Ignite ignite;

    @ApiOperation(value = "执行SQL")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cache", value = "缓冲名称"),
            @ApiImplicitParam(name = "sql", value = "sql")})
    @PutMapping("query")
    public JSONObject query(String cache, String sql) {
        JSONObject jsonObject = new JSONObject();
        IgniteCache<Object, Object> igniteCache = ignite.getOrCreateCache(cache);
        FieldsQueryCursor<List<?>> fieldsQueryCursor = igniteCache
                .query(new SqlFieldsQuery(sql)
                        //Ignite默认会试图将所有结果集加载到内存然后将其发送给查询发起方(通常为应用端），这个方式在查询结果集不太大时提供了比较好的性能，不过如果相对于可用内存来说结果集过大，就是导致长期的GC暂停甚至OutOfMemoryError错误。
                        //为了降低内存的消耗，以适度降低性能为代价，可以对结果集进行延迟加载和处理，这个可以通过给JDBC或者ODBC连接串传递lazy参数，
                        .setLazy(true)
                        //当Ignite执行分布式查询时，它将子查询发送给各个集群节点，并在汇总节点（通常是应用端）上对结果集进行分组。如果预先知道查询的数据是按GROUP BY条件并置处理的，可以使用SqlFieldsQuery.collocated = true来通知SQL引擎在远程节点进行分组处理，这会减少节点之间的网络流量和查询执行时间。当此标志设置为true时，首先对单个节点执行查询，并将结果发送到汇总节点进行最终计算。
                        .setCollocated(true)
                        .setSchema("PUBLIC"));
        jsonObject.put("fieldsQueryCursor", fieldsQueryCursor);
        return jsonObject;
    }

}
