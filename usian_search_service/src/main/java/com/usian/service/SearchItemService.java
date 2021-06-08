package com.usian.service;

import com.github.pagehelper.PageHelper;
import com.rabbitmq.client.Channel;
import com.usian.dto.ItemDto;
import com.usian.mapper.TbItemMapper;
import com.usian.utils.EsUtils;
import com.usian.utils.JsonUtils;
import com.usian.vo.ItemEsVo;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SearchItemService {
    @Autowired
    private TbItemMapper itemMapper;
    @Autowired
    private EsUtils esUtils;
    @Autowired
    private RestHighLevelClient restHighLevelClient;


    //将新添加的商品加入es中
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value="item_queue",durable = "true"),
            exchange = @Exchange(value="item_exchange",type= ExchangeTypes.TOPIC),
            key= {"*.insert"}
    ))
    public void listen(ItemDto itemDto, Channel channel, Message message){
        String source = JsonUtils.objectToJson(itemDto);
        esUtils.insertDoc("usian","item",source);
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //删除es商品信息
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value="item_queue",durable = "true"),
            exchange = @Exchange(value="item_exchange",type= ExchangeTypes.TOPIC),
            key= {"*.delete"}
    ))
    public void listenDelete(String id){
        esUtils.deleteDoc("usian","item",id);
    }

    //导入商品信息到es
    public void importAll() {
        //分页查询  查一部分导入一部门
        int pageNum=1;
        while (true){
            //查询mysql
            PageHelper.startPage(pageNum,1000);
            List<ItemEsVo> list=itemMapper.importAll();

            if (list==null || list.size()==0){
                break;
            }
            //判断是否存在索引  不存在就创建
            if (!esUtils.existsIndex("usian")){
                esUtils.createIndex(1,0,"usian","item","{\n" +
                        "  \"_source\": {\n" +
                        "    \"excludes\": [\n" +
                        "      \"item_desc\"\n" +
                        "    ]\n" +
                        "  },\n" +
                        "  \"properties\": {\n" +
                        "    \"id\": {\n" +
                        "      \"type\": \"keyword\",\n" +
                        "       index: false\n" +
                        "    },\n" +
                        "    \"item_title\": {\n" +
                        "      \"type\": \"text\",\n" +
                        "      \"analyzer\": \"ik_max_word\",\n" +
                        "      \"search_analyzer\": \"ik_smart\"\n" +
                        "    },\n" +
                        "    \"item_sell_point\": {\n" +
                        "      \"type\": \"text\",\n" +
                        "      \"analyzer\": \"ik_max_word\",\n" +
                        "      \"search_analyzer\": \"ik_smart\"\n" +
                        "    },\n" +
                        "    \"item_price\": {\n" +
                        "      \"type\": \"float\"\n" +
                        "    },\n" +
                        "    \"item_image\": {\n" +
                        "      \"type\": \"text\",\n" +
                        "      \"index\": false\n" +
                        "    },\n" +
                        "    \"item_category_name\": {\n" +
                        "      \"type\": \"text\",\n" +
                        "      \"analyzer\": \"ik_max_word\",\n" +
                        "      \"search_analyzer\": \"ik_smart\"\n" +
                        "    },\n" +
                        "    \"item_desc\": {\n" +
                        "      \"type\": \"text\",\n" +
                        "      \"analyzer\": \"ik_max_word\",\n" +
                        "      \"search_analyzer\": \"ik_smart\"\n" +
                        "    }\n" +
                        "  }\n" +
                        "}");
            }

            BulkRequest bulkRequest = new BulkRequest();
            //存入es
            list.forEach(e->{
                bulkRequest.add(new IndexRequest("usian","item",e.getId()+"")
                        .source(JsonUtils.objectToJson(e), XContentType.JSON));
            });

            try {
                restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
            pageNum++;
        }

    }

    public List<ItemEsVo> list(String q) {

        // 搜索请求对象
        SearchRequest searchRequest = new SearchRequest("usian");
        searchRequest.types("item");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.multiMatchQuery(q,"item_title","item_category_name","item_desc","item_sell_point"));
        //设置高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<font color='red'>");
        highlightBuilder.postTags("</font>");
        List<HighlightBuilder.Field> fields = highlightBuilder.fields();
        fields.add(new HighlightBuilder.Field("item_title"));
        fields.add(new HighlightBuilder.Field("item_category_name"));
        fields.add(new HighlightBuilder.Field("item_desc"));
        fields.add(new HighlightBuilder.Field("item_sell_point"));
        searchSourceBuilder.highlighter(highlightBuilder);
        // 设置搜索源
        searchRequest.source(searchSourceBuilder);
        ArrayList<ItemEsVo> itemEsVosList = new ArrayList<>();
        // 执行搜索
        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest,RequestOptions.DEFAULT);
            // 匹配的文档
            SearchHit[] searchHits = searchResponse.getHits().getHits();
            for (SearchHit hit : searchHits) {
                // 源文档内容
                String source = hit.getSourceAsString();
                System.out.println(source);
                ItemEsVo itemEsVo = JsonUtils.jsonToPojo(source, ItemEsVo.class);
                if (itemEsVo!=null){
                    Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                    if (highlightFields!=null){
                        HighlightField highlightField = highlightFields.get("item_title");
                        if (highlightField!=null){
                            Text[] fragments = highlightField.getFragments();
                            itemEsVo.setItemTitle(fragments[0].toString());
                        }
                        highlightField = highlightFields.get("item_category_name");
                        if (highlightField!=null){
                            Text[] fragments = highlightField.getFragments();
                            itemEsVo.setItemCategoryName(fragments[0].toString());
                        }
                        highlightField = highlightFields.get("item_desc");
                        if (highlightField!=null){
                            Text[] fragments = highlightField.getFragments();
                            itemEsVo.setItemDesc(fragments[0].toString());
                        }
                        highlightField = highlightFields.get("item_sell_point");
                        if (highlightField!=null){
                            Text[] fragments = highlightField.getFragments();
                            itemEsVo.setItemSellPoint(fragments[0].toString());
                        }
                    }
                }
                itemEsVosList.add(itemEsVo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return itemEsVosList;
    }
}
