package com.usian.utils;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class EsUtils {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    //删除doc
    public void deleteDoc(String indexName,String typeName,String id){
        //删除索引请求对象
        DeleteRequest deleteRequest = new DeleteRequest(indexName,typeName,id);
        //响应对象
        try {
            restHighLevelClient.delete(deleteRequest,RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //添加新的doc
    public void insertDoc(String indexName,String typeName,String source){
        //创建“索引请求”对象：索引当动词
        IndexRequest indexRequest = new IndexRequest(indexName, typeName);
        indexRequest.source(source, XContentType.JSON);
        try {
            IndexResponse indexResponse =
                    restHighLevelClient.index(indexRequest,RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //判断索引是否存在
    public boolean existsIndex(String indexName){
        //获取操作索引的客户端
        IndicesClient indicesClient = restHighLevelClient.indices();
        //判断指定索引名是否存在
        GetIndexRequest getIndexRequest = new GetIndexRequest().indices(indexName);
        try {
            indicesClient.exists(getIndexRequest,RequestOptions.DEFAULT);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    //创建索引，及类型及映射
    public void createIndex(Integer shardNums,Integer replicaNums,String indexName,String typeName,String mappingStr){
        //获取操作索引的客户端
        IndicesClient indicesClient = restHighLevelClient.indices();
        //创建索引
        Settings settings = Settings.builder()
                .put("number_of_shards", shardNums)
                .put("number_of_replicas", replicaNums)
                .build();
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName,settings);

        //创建映射
        createIndexRequest.mapping(typeName,mappingStr, XContentType.JSON);

        try {
            indicesClient.create(createIndexRequest,RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
