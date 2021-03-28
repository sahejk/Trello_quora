package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserAuthDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QuestionService {

  @Autowired
  private QuestionDao questionDao;

  @Autowired
  private UserAuthDao userAuthDao;

  @Transactional(propagation = Propagation.REQUIRED)
  public QuestionEntity createQuestion(final String accessToken, QuestionEntity questionEntity)
      throws AuthorizationFailedException {

    UserAuthTokenEntity userAuthTokenEntity = userAuthDao.getUserAuthByToken(accessToken);

    if (userAuthTokenEntity == null) {
      throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
    } else if (userAuthTokenEntity.getLogoutAt() != null) {
      throw new AuthorizationFailedException("ATHR-002",
          "User is signed out.Sign in first to post a question");
    }

    questionEntity.setUuid(UUID.randomUUID().toString());
    questionEntity.setDate(ZonedDateTime.now());
    questionEntity.setUserEntity(userAuthTokenEntity.getUser());

    return questionDao.createQuestion(questionEntity);
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public List<QuestionEntity> getAllQuestions(final String accessToken)
      throws AuthorizationFailedException {

    UserAuthTokenEntity userAuthTokenEntity = userAuthDao.getUserAuthByToken(accessToken);

    if (userAuthTokenEntity == null) {
      throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
    } else if (userAuthTokenEntity.getLogoutAt() != null) {
      throw new AuthorizationFailedException("ATHR-002",
          "User is signed out.Sign in first to post a question");
    }

    return questionDao.getAllQuestions();
  }
}
