package com.xos.smartchat.dao;

import com.xos.smartchat.entities.QaEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QuestionAnswerDao {
    QaEntity findByQuestion(@Param("question") String question);

    List<QaEntity> findAll();

    void increaseHit(@Param("question") String question, @Param("hits") int hits);
}
