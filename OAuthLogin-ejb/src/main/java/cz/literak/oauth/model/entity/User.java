package cz.literak.oauth.model.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

/**
 * Date: 11.1.14
 */
@Entity
@Table(name = "base_user")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;
    @Column(name = "first_name", nullable = true)
    String firstName;
    @Column(name = "last_name", nullable = true)
    String lastName;
    @Column(name = "nick_name", nullable = true)
    String nickName;
    @Column(name = "email", nullable = true)
    String email;
    @Column(name = "registration_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date registered = new Date();
    @OneToMany(fetch= FetchType.LAZY, mappedBy="user", cascade = {CascadeType.PERSIST})
    List<OAuthLogin> oauthLogins = new ArrayList<>();

    public User() {
    }

    public User(String name) {
        int spacePosition = name.indexOf(' ');
        if (spacePosition == -1) {
            nickName = name;
        } else {
            firstName = name.substring(0, spacePosition);
            lastName = name.substring(spacePosition + 1);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setName(String name) {
        int position = name.lastIndexOf(' ');
        if (position == -1) {
            lastName = name;
        } else {
            firstName = name.substring(0, position);
            lastName = name.substring(position + 1);
        }
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getRegistered() {
        return registered;
    }

    public void setRegistered(Date registered) {
        this.registered = registered;
    }

    public List<OAuthLogin> getOauthLogins() {
        return oauthLogins;
    }

    public void setOauthLogins(List<OAuthLogin> oauthLogins) {
        this.oauthLogins = oauthLogins;
    }

    public boolean isRegistered(String provider) {
        for (OAuthLogin login : oauthLogins) {
            if (login.getProvider().equalsIgnoreCase(provider)) {
                return true;
            }
        }
        return false;
    }

    public boolean areRegistered(String providers) {
        Set<String> providerSet = new HashSet<>();
        StringTokenizer stk = new StringTokenizer(providers, ",");
        while (stk.hasMoreTokens()) {
            providerSet.add(stk.nextToken().trim().toUpperCase());
        }

        int matched = 0;
        for (OAuthLogin login : oauthLogins) {
            if (providerSet.contains(login.getProvider().toUpperCase())) {
                matched++;
            }
        }

        return matched == providerSet.size();
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", email='" + email + '\'' +
                ", id=" + id +
                '}';
    }
}
