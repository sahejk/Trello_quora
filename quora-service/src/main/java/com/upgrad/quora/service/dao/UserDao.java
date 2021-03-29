package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class UserDao {


    @PersistenceContext
    private EntityManager entityManager;

    /**
     * create user in database.
     *
     * @param userEntity : the userEntity body
     * @return User details
     */
    public UserEntity createUser(UserEntity userEntity) {
        entityManager.persist(userEntity);
        return userEntity;
    }

    /**
     * Fetch a single user by given id from the DB.
     *
     * @param userId Id of the user whose information is to be fetched.
     * @return User details if exist in the DB else null.
     */
    public UserEntity getUser(final String userId) {
        try {
            return entityManager
                .createNamedQuery("userByUserId", UserEntity.class)
                .setParameter("userId", userId)
                .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }


    //The method getUserByUserName fetches the user based on userName and if no record is found in the database it returns null

    public UserEntity getUserByUserName(final String userName) throws NoResultException  {
        try {
            return entityManager.createNamedQuery("userByName", UserEntity.class).setParameter("userName", userName)
                    .getSingleResult();
        } catch(NoResultException nre) {
            return null;
        }
    }

    //The method getUserByEmail fetches the user based on email and if no record is found in the database it returns null

    public UserEntity getUserByEmail(final String email) throws NoResultException {
        try {
            return entityManager.createNamedQuery("userByEmail", UserEntity.class).setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * Method to update user in db
     *
     * @param updatedUserEntity : UserEntity body
     * @return updated response
     */
    public void updateUserEntity(final UserEntity updatedUserEntity) {
        entityManager.merge(updatedUserEntity);
    }

    /**
     * Method to delete user by id
     *
     * @param userId : username which you want to delete
     * @return deleted response
     */
    public UserEntity deleteUser(final String userId) {
        UserEntity deleteUser = getUserByUuid(userId);
        if (deleteUser != null) {
            this.entityManager.remove(deleteUser);
        }
        return deleteUser;
    }

    //This method retrieves the user based on user uuid, if found returns user else null
    public UserEntity getUserByUuid(final String uuid) {
        try {
            return entityManager.createNamedQuery("userByUuid", UserEntity.class).setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

}