# ignite Exception

### Unsupported connection setting "MULTI_THREADED" [90113-200]

引入 maven 依赖

`
<dependency>
<groupId>com.h2database</groupId>
<artifactId>h2</artifactId>
<version>1.4.197</version>
</dependency>
`
***

### Directory does not exist and cannot be created: /opt/storage

    <property name="dataStorageConfiguration">
            <bean class="org.apache.ignite.configuration.DataStorageConfiguration">
                <property name="defaultDataRegionConfiguration">
                    <bean class="org.apache.ignite.configuration.DataRegionConfiguration">
                        <property name="persistenceEnabled" value="true"/>
                    </bean>
                </property>
                <!--配置持久化存储目录 启用持久化之后，节点就会在{IGNITE_WORK_DIR}/db目录中存储用户的数据、索引和WAL文件，该目录称为存储目录。
                通过配置DataStorageConfiguration的storagePath属性可以修改存储目录。每个节点都会在存储目录下维护一个子目录树，来存储缓存数据、WAL文件和WAL存档文件。-->
                <property name="storagePath" value="../opt/storage"/>
            </bean>
        </property>

    创建文件夹，ignite不会自动创建

#### Can not perform the operation because the cluster is inactive. Note, that the cluster is considered inactive by default if Ignite Persistent Store is used to let all the nodes join the cluster. To activate the cluster call Ignite.active(true).

     IgniteCluster igniteCluster = ignite.cluster();
      igniteCluster.state(ClusterState.ACTIVE);











