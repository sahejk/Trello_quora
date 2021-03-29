package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.QuestionDeleteResponse;
import com.upgrad.quora.api.model.QuestionDetailsResponse;
import com.upgrad.quora.api.model.QuestionEditRequest;
import com.upgrad.quora.api.model.QuestionEditResponse;
import com.upgrad.quora.api.model.QuestionRequest;
import com.upgrad.quora.api.model.QuestionResponse;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/question")
public class QuestionController {

  /**
   * Create a question
   *
   * @param questionRequest This object has the content i.e the question.
   * @param accessToken access token to authenticate user.
   * @return UUID of the question created in DB.
   * @throws AuthorizationFailedException when user is invalid or signedout .
   */
  @Autowired private QuestionService questionService;

  @RequestMapping(
      method = RequestMethod.POST,
      path = "/create",
      consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<QuestionResponse> createQuestion(
      @RequestHeader("authorization") String accessToken, final QuestionRequest questionRequest)
      throws AuthorizationFailedException {

    QuestionEntity questionEntity = new QuestionEntity();
    questionEntity.setContent(questionRequest.getContent());

    QuestionEntity question = questionService.createQuestion(accessToken, questionEntity);

    QuestionResponse questionResponse = new QuestionResponse();
    questionResponse.setStatus("QUESTION CREATED");
    questionResponse.setId(question.getUuid());

    return new ResponseEntity<>(questionResponse, HttpStatus.CREATED);
  }

  /**
   * Get all questions posted by any user.
   *
   * @param accessToken access token to authenticate user.
   * @return List of QuestionDetailsResponse
   * @throws AuthorizationFailedException When the user is invalid or signedout.
   */
  @RequestMapping(
      method = RequestMethod.GET,
      path = "/all",
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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

    return new ResponseEntity<>(
        questionDetailsResponseList, HttpStatus.OK);
  }

  /**
   * Edit a question
   *
   * @param accessToken access token to authenticate user.
   * @param questionId id of the question to be edited.
   * @param questionEditRequest new content for the question.
   * @return Id and status of the question edited.
   * @throws AuthorizationFailedException  when the user is invalid or signedout
   * @throws InvalidQuestionException if question with questionId doesn't exist.
   */
  @RequestMapping(
      method = RequestMethod.PUT,
      path = "/question/edit/{questionId}",
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<QuestionEditResponse> editQuestion(
      @RequestHeader("authorization") final String accessToken,
      @PathVariable("questionId") final String questionId,
      QuestionEditRequest questionEditRequest)
      throws AuthorizationFailedException, InvalidQuestionException {
    QuestionEntity questionEntity =
        questionService.editQuestion(accessToken, questionId, questionEditRequest.getContent());
    QuestionEditResponse questionEditResponse = new QuestionEditResponse();
    questionEditResponse.setId(questionEntity.getUuid());
    questionEditResponse.setStatus("QUESTION EDITED");
    return new ResponseEntity<>(questionEditResponse, HttpStatus.OK);
  }

  /**
   * Delete a question
   *
   * @param accessToken access token to authenticate user.
   * @param questionId id of the question to be edited.
   * @return Id and status of the question deleted.
   * @throws AuthorizationFailedException when the user is invalid or signedout
   * @throws InvalidQuestionException if question with questionId doesn't exist.
   */
  @RequestMapping(method = RequestMethod.DELETE, path = "/question/delete/{questionId}")
  public ResponseEntity<QuestionDeleteResponse> deleteQuestion(
      @RequestHeader("authorization") final String accessToken,
      @PathVariable("questionId") final String questionId)
      throws AuthorizationFailedException, InvalidQuestionException {

    QuestionEntity questionEntity = questionService.deleteQuestion(accessToken, questionId);
    QuestionDeleteResponse questionDeleteResponse = new QuestionDeleteResponse();
    questionDeleteResponse.setId(questionEntity.getUuid());
    questionDeleteResponse.setStatus("QUESTION DELETED");
    return new ResponseEntity<QuestionDeleteResponse>(questionDeleteResponse, HttpStatus.OK);
  }

  /**
   * Get all questions posted by a user with given userId.
   *
   * @param userId of the user for whom we want to see the questions asked by him
   * @param accessToken access token to authenticate user.
   * @return List of QuestionDetailsResponse
   * @throws AuthorizationFailedException when the user is invalid or signedout.
   */
  @RequestMapping(
      method = RequestMethod.GET,
      path = "question/all/{userId}",
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<List<QuestionDetailsResponse>> getQuestionByUserId(
      @RequestHeader("authorization") final String accessToken,
      @PathVariable("userId") String userId)
      throws AuthorizationFailedException, UserNotFoundException {

    List<QuestionEntity> questions = questionService.getAllQuestionsByUser(userId, accessToken);
    List<QuestionDetailsResponse> questionDetailResponses = new ArrayList<>();
    for (QuestionEntity questionEntity : questions) {
      QuestionDetailsResponse questionDetailResponse = new QuestionDetailsResponse();
      questionDetailResponse.setId(questionEntity.getUuid());
      questionDetailResponse.setContent(questionEntity.getContent());
      questionDetailResponses.add(questionDetailResponse);
    }
    return  new ResponseEntity<>(questionDetailResponses, HttpStatus.OK);
  }
}
