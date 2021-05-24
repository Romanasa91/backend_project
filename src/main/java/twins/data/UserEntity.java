package twins.data;

import javax.persistence.*;


/*
       USERS_TABLE

UID <PK>      | ROLE         | USER_NAME    | AVATAR
================================================================================
VARCHAR(255)  | VARCHAR(255) | VARCHAR(255) | VARCHAR(255)
 */

@Entity
@Table(name="USERS_TABLE")
public class UserEntity {
    private String userId; // = "space;email" (2021b.twins;example@mail.com)
    private UserRole role;
    private String username;
    private String avatar;

    public UserEntity() {
    }

    @Id
    @Column(name = "UID")
    public String getUserId() {
        return userId;
    }


    @Column(name = "ROLE")
    @Enumerated
    public UserRole getRole() {
        return role;
    }

    @Column(name = "USER_NAME")
    public String getUsername() {
        return username;
    }

    @Column(name = "AVATAR")
    public String getAvatar() {
        return avatar;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
