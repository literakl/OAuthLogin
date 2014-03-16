package cz.literak.oauth.model.dao;

import cz.literak.oauth.model.entity.OAuthLogin;

import javax.ejb.Stateless;
import javax.persistence.*;
import java.util.List;

/**
 * Date: 11.1.14
 */
@Stateless(name = "OAuthLoginDAOEJB")
public class OAuthLoginDAO {
    @PersistenceContext(unitName="OAUTHDEMO")
    EntityManager em;

    public void persist(OAuthLogin entity) throws EntityExistsException {
        em.persist(entity);
   	}

   	public OAuthLogin merge(OAuthLogin entity) {
   		return em.merge(entity);
   	}

   	public void remove(OAuthLogin entity) {
        em.remove(entity);
   	}

    public OAuthLogin findByProvider(String provider, String id) {
        Query query = em.createNamedQuery("findLoginByProvider");
        query.setParameter("provider", provider);
        query.setParameter("providerId", id);
        return getInitialized(query.getResultList());
   	}

    public OAuthLogin findByAccessToken(String provider, String accessToken) {
        Query query = em.createNamedQuery("findLoginByAccessToken");
        query.setParameter("provider", provider);
        query.setParameter("accessToken", accessToken);
        return getInitialized(query.getResultList());
    }

   	public OAuthLogin findById(int id, LockModeType lock) {
        OAuthLogin entity;
   		entity = em.find(OAuthLogin.class, id);
        em.lock(entity, lock);
   		return entity;
   	}

   	public void refresh(final OAuthLogin entity) {
        em.refresh(entity);
   	}

   	public void flush() {
        em.flush();
   	}

    private OAuthLogin getInitialized(List list) {
        if (list == null || list.isEmpty()) {
            return null;
        }

        OAuthLogin oAuthLogin = (OAuthLogin) list.get(0);
        oAuthLogin.getUser().getOauthLogins().size(); // JPA avoid no session error
        return oAuthLogin;
    }
}
