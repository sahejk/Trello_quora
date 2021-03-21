package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.SignUpUserResponse;
import com.upgrad.quora.service.business.UserAuthenticationBusinessService;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

//RestController annotation specifies that this class represents a REST API(equivalent of @Controller + @ResponseBody)
@RestController
@RequestMapping("/user")
public class UserController {
    //Required services are autowired to enable access to methods defined in respective Business services
    @Autowired
    private UserAuthenticationBusinessService userAuthenticationBusinessService;

    /**
     * @param  signupUserRequest the first {@code SignUpUserRequest} to signup a particular user with details.
     * @return ResponseEntity is returned with Status CREATED.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/signup", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignUpUserResponse> signup(final com.upgrad.quora.api.model.SignUpUserRequest signupUserRequest) throws SignUpRestrictedException {

        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName(signupUserRequest.getFirstName());
        userEntity.setLastName(signupUserRequest.getLastName());
        userEntity.setUserName(signupUserRequest.getUserName());
        userEntity.setEmail(signupUserRequest.getEmailAddress());
        userEntity.setPassword(signupUserRequest.getPassword());
        userEntity.setCountry(signupUserRequest.getCountry());
        userEntity.setAboutMe(signupUserRequest.getAboutMe());
        userEntity.setDob(signupUserRequest.getDob());
        //Since this is user sign up so the role is set as "nonadmin"
        userEntity.setRole("nonadmin");
        userEntity.setContactNumber(signupUserRequest.getContactNumber());

        UserEntity createdUserEntity = userAuthenticationBusinessService.signup(userEntity);
        SignUpUserResponse userResponse = new SignUpUserResponse().id(createdUserEntity.getUuid()).status("USER SUCCESSFULLY REGISTERED");
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }
}