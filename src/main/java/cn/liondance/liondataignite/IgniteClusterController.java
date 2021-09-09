package cn.liondance.liondataignite;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCluster;
import org.apache.ignite.cluster.ClusterState;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

/**
 * 动态
 *
 * @author shadow
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/ignite/cluster")
public class IgniteClusterController {

    private final Ignite ignite;

    /**
     * Ignite info json object.
     *
     * @return the json object
     * @throws UnsupportedEncodingException the unsupported encoding exception
     */
    @GetMapping("ClusterState")
    public JSONObject ClusterState(String clusterState) {
        JSONObject jsonObject = new JSONObject();
        IgniteCluster igniteCluster = ignite.cluster();
        switch (clusterState) {
            case "INACTIVE":
                igniteCluster.state(ClusterState.INACTIVE);
                break;
            case "ACTIVE_READ_ONLY":
                igniteCluster.state(ClusterState.ACTIVE_READ_ONLY);
                break;
            default:
                igniteCluster.state(ClusterState.ACTIVE);
                break;
        }
        jsonObject.put("igniteCluster", igniteCluster);
        jsonObject.put("nodes", igniteCluster.nodes());
        return jsonObject;
    }



}
