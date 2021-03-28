package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.QuestionDetailsResponse;
import com.upgrad.quora.api.model.QuestionRequest;
import com.upgrad.quora.api.model.QuestionResponse;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/question")
public class QuestionController {

  @Autowired
  private QuestionService questionService;

  @RequestMapping(method = RequestMethod.POST, path = "/create", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<QuestionResponse> createQuestion(
      @RequestHeader("authorization") String accessToken, final QuestionRequest questionRequest)
      throws AuthorizationFailedException {

    QuestionEntity questionEntity = new QuestionEntity();
    questionEntity.setContent(questionRequest.getContent());

    QuestionEntity question = questionService.createQuestion(accessToken, questionEntity);

    QuestionResponse questionResponse = new QuestionResponse();
    questionResponse.setStatus("QUESTION CREATED");
    questionResponse.setId(question.getUuid());

    return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.CREATED);
  }

  @RequestMapping(method = RequestMethod.GET, path = "/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestion(
      @RequestHeader("authorization") String accessToken) throws AuthorizationFailedException {

    List<QuestionEntity> questionEntityList = questionService.getAllQuestions(accessToken);
    List<QuestionDetailsResponse> questionDetailsResponseList = new ArrayList<>();

    for (QuestionEntity question : questionEntityList) {
      QuestionDetailsResponse questionDetailsResponse = new QuestionDetailsResponse();
      questionDetailsResponse.setContent(question.getContent());
      questionDetailsResponse.setId(question.getUuid());
      questionDetailsResponseList.add(questionDetailsResponse);
    }

    return new ResponseEntity<List<QuestionDetailsResponse>>(questionDetailsResponseList,
        HttpStatus.OK);
  }
}


