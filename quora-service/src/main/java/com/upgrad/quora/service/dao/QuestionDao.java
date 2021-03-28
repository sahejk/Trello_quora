package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class QuestionDao {

  @PersistenceContext
  private EntityManager entityManager;

  public QuestionEntity createQuestion(QuestionEntity questionEntity) {
    entityManager.persist(questionEntity);
    return questionEntity;
  }

   public List<QuestionEntity> getAllQuestions(){
     return entityManager.createNamedQuery("getAllQuestions",QuestionEntity.class).getResultList();
   }
}
