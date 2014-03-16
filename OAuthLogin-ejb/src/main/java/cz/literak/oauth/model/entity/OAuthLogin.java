package cz.literak.oauth.model.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Date: 26.1.14
 */
@Entity
@Table(name = "oauth_login")
@NamedQueries(value = {
    @NamedQuery(
        name="findLoginByProvider",
        query="SELECT OBJECT(login) FROM OAuthLogin login WHERE login.provider = :provider AND login.providerId = :providerId"
    ),
    @NamedQuery(
        name="findLoginByAccessToken",
        query="SELECT OBJECT(login) FROM OAuthLogin login WHERE login.provider = :provider AND login.accessToken = :accessToken"
    )
})

public class OAuthLogin implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;
    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    User user;
    @Column(name = "provider_code", nullable = false, length = 2)
    String provider;
    @Column(name = "provider_id", nullable = false, length = 255)
    String providerId;
    @Column(name = "access_token", nullable = true, length = 255)
    String accessToken;
    @Column(name = "registration_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date registered = new Date();

    public OAuthLogin() {
    }

    public OAuthLogin(User user, String provider, String providerId) {
        this.user = user;
        setProvider(provider);
        this.providerId = providerId;
        user.getOauthLogins().add(this);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Date getRegistered() {
        return registered;
    }

    public void setRegistered(Date registered) {
        this.registered = registered;
    }
}
