package com.xos.smartchat.service;

import com.alibaba.fastjson.JSON;
import com.xos.smartchat.dao.QuestionAnswerDao;
import com.xos.smartchat.entities.QaEntity;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DataImportServiceImpl implements DataImportService {

    @Value("${esconfig.index}")
    private String index;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private QuestionAnswerDao questionAnswerDao;

    @Override
    public void createIndices() throws IOException {
        Assert.isTrue(indexNotExists(), "索引已存在");

        // 创建索引
        // 创建索引请求
        CreateIndexRequest request = new CreateIndexRequest(index);

        // 配置settings
        request.settings(
                Settings.builder()
                        .put("index.number_of_shards", 1)
                        .put("index.number_of_replicas", 1)
        );

        // 配置mapping
        XContentBuilder mapping = JsonXContent.contentBuilder()
                .startObject()
                    .startObject("properties")
                        .startObject("keywords")
                            .field("type", "text")
                            .startObject("fields")
                                .startObject("title_ik_smart")
                                .field("type", "text")
                                .field("analyzer", "ik_smart")
                            .endObject()
                            .startObject("title_ik_max_word")
                            .field("type", "text")
                            .field("analyzer", "ik_max_word")
                            .endObject()
                            .endObject()
                        .endObject()
                        // field question
                        .startObject("question")
                            .field("type", "text")
                            .startObject("fields")
                                .startObject("keyword")
                                    .field("type", "keyword")
                                .endObject()
                            .endObject()
                        .endObject()
                        // field hits
                        .startObject("hits")
                            .field("type", "long")
                        .endObject()
                    .endObject()
                .endObject();
        request.mapping(mapping);
        restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
    }

    private boolean indexNotExists() throws IOException {
        GetIndexRequest request = new GetIndexRequest(index);
        return !restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
    }

    @Override
    public void syncEs() throws IOException {
        List<QaEntity> qas = questionAnswerDao.findAll();

        Map<String, Object> valsMap = new HashMap<>();

        for (QaEntity qa : qas) {
            valsMap.clear();

            valsMap.put("keywords", qa.getKeywords());
            valsMap.put("question", qa.getQuestion());
            valsMap.put("hits", qa.getHits());

            IndexRequest request = new IndexRequest(index);
            request.source(JSON.toJSONString(valsMap), XContentType.JSON);
            restHighLevelClient.index(request, RequestOptions.DEFAULT);
        }
    }
}
