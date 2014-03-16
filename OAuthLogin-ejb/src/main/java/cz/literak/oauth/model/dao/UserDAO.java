package cz.literak.oauth.model.dao;

import cz.literak.oauth.model.entity.User;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

/**
 * Date: 11.1.14
 */
@Stateless(name = "UserDAOEJB")
public class UserDAO {
    @PersistenceContext(unitName="OAUTHDEMO")
    EntityManager em;

    public void persist(User entity) throws EntityExistsException {
        em.persist(entity);
   	}

   	public User merge(User entity) {
   		return em.merge(entity);
   	}

   	public void remove(User entity) {
        em.remove(entity);
   	}

    public User findById(int id) {
        User entity;
   		entity = em.find(User.class, id);
   		return entity;
   	}

    public User findById(int id, boolean prefetch) {
        User entity;
   		entity = em.find(User.class, id);
        if (prefetch) {
            entity.getOauthLogins().size();
        }
   		return entity;
   	}

   	public User findById(int id, LockModeType lock) {
        User entity;
   		entity = em.find(User.class, id);
        em.lock(entity, lock);
   		return entity;
   	}

   	public void refresh(final User entity) {
        em.refresh(entity);
   	}

   	public void flush() {
        em.flush();
   	}
}
