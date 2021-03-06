package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserAuthDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommonService {

    @Autowired
    private UserAuthDao userAuthDao;

    @Autowired
    private UserDao userDao;

    @Transactional(propagation =  Propagation.REQUIRED)
    public UserEntity getUserProfile(final String userId, String authorization) throws AuthorizationFailedException, UserNotFoundException {

        UserAuthTokenEntity userAuthTokenEntity = userAuthDao.getUserAuthByToken(authorization);

        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        if (userAuthTokenEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get user details");
        }

        UserEntity userEntity = userDao.getUserByUuid(userId);

        if (userEntity == null) {
            throw new UserNotFoundException("USR-001", "User with entered uuid does not exist");
        }

        return userEntity;

    }
}