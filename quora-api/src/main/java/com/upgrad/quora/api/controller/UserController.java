package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.SignUpUserRequest;
import com.upgrad.quora.api.model.SigninResponse;
import com.upgrad.quora.api.model.SignoutResponse;
import com.upgrad.quora.api.model.SignupUserResponse;
import com.upgrad.quora.service.business.UserAuthenticationBusinessService;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;

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
    public ResponseEntity<SignupUserResponse> signup(final SignUpUserRequest signupUserRequest) throws SignUpRestrictedException {

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
        SignupUserResponse userResponse = new SignupUserResponse().id(createdUserEntity.getUuid()).status("USER SUCCESSFULLY REGISTERED");
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    /**
     * This method is for a user to signIn.
     *
     * @param authorization for the basic authentication
     * @return Signin resopnse which has userId and access-token in response header.
     * @throws AuthenticationFailedException : if username or password is invalid
     */
    @RequestMapping(method = RequestMethod.POST, path = "/signin", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SigninResponse> userSignin(@RequestHeader("authorization") final String authorization) throws AuthenticationFailedException {

        // The encoded Base64 format string has to be decoded to a separate string of username and password
        // and need to pass as arguments to the authenticate method for calling the business logic

        byte[] decode = Base64.getDecoder().decode(authorization.split("Basic ")[1]);
        String decodedText = new String(decode);
        String[] decodedArray = decodedText.split(":");
        UserAuthTokenEntity userAuthEntity = userAuthenticationBusinessService.signin(decodedArray[0], decodedArray[1]);
        HttpHeaders headers = new HttpHeaders();
        headers.add("access-token", userAuthEntity.getAccessToken());

        SigninResponse signinResponse = new SigninResponse();
        signinResponse.setId(userAuthEntity.getUser().getUuid());
        signinResponse.setMessage("SIGNED IN SUCCESSFULLY");

        return new ResponseEntity<>(signinResponse, headers, HttpStatus.OK);
    }

    //**userSignout**//
    /**
     * Request mapping to sign-out user
     *
     * @param acessToken
     * @return SignoutResponse
     * @throws SignOutRestrictedException
     */

    @RequestMapping(method = RequestMethod.POST, path = "/signout", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)

    public ResponseEntity<SignoutResponse> signout(
            @RequestHeader("authorization") final String acessToken) throws SignOutRestrictedException {
        UserEntity userEntity = userAuthenticationBusinessService.signOut(acessToken);
        SignoutResponse signoutResponse =
                new SignoutResponse().id(userEntity.getUuid()).message("SIGNED OUT SUCCESSFULLY");
        return new ResponseEntity<>(signoutResponse, HttpStatus.OK);
    }
}