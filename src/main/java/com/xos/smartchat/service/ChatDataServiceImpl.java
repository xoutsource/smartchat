package com.xos.smartchat.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xos.smartchat.dao.QuestionAnswerDao;
import com.xos.smartchat.entities.QaEntity;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChatDataServiceImpl implements ChatDataService {

    @Value("${esconfig.index}")
    private String index;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private QuestionAnswerDao qaDao;

    @Override
    public List<String> getPrompts(String question) throws IOException {
        if (StringUtils.isEmpty(question)) {
            return null;
        }

        // ES中取热度问题
        SearchRequest request = new SearchRequest(index);
        SearchSourceBuilder searchSource = new SearchSourceBuilder()
                .query(QueryBuilders.matchQuery("keywords", question))
                .sort("_score", SortOrder.DESC)
                .sort("hits", SortOrder.DESC)
                .size(5);

        request.source(searchSource);

        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        SearchHit[] hits = response.getHits().getHits();

        List<String> list = new ArrayList<>();
        for (SearchHit hit : hits) {
            JSONObject jsonObject = JSONObject.parseObject(hit.getSourceAsString());
            Object questionVal = jsonObject.get("question");
            String questionStr = questionVal == null ? "" : questionVal.toString();

//            Object keywordsVal = jsonObject.get("keywords");
//            String keywordsStr = keywordsVal == null ? "" : keywordsVal.toString();
            list.add(questionStr);
        }
        return list;
    }

    @Override
    public String getAnswer(String question) throws IOException {
        if (StringUtils.isEmpty(question)) {
            return null;
        }
        // 数据库 hits+1
        QaEntity qa = qaDao.findByQuestion(question);
        if (qa == null) {
            return null;
        }
        int hits = qa.getHits() + 1;
        qaDao.increaseHit(question, hits);
        // ES hits+1 (在正式环境中,ES数据应该是从数据库定时抽取)
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSource = new SearchSourceBuilder()
                .query(QueryBuilders.termQuery("question.keyword", question))
                .size(1);
        searchRequest.source(searchSource);
        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] hitses = response.getHits().getHits();
        if (hitses != null && hitses.length > 0) {
            SearchHit searchHits = hitses[0];
            String id = searchHits.getId();
            UpdateRequest updateRequest = new UpdateRequest(index, id);
            Map<String, Object> valsMap = new HashMap<>();
            valsMap.put("keywords", qa.getKeywords());
            valsMap.put("question", qa.getQuestion());
            valsMap.put("hits", hits);
            updateRequest.doc(JSON.toJSONString(valsMap), XContentType.JSON);
            restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        }
        return qa.getAnswer();
    }
}
