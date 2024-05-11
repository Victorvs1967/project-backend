package com.vvs.backend.model;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Document("users")
public class User implements UserDetails {

    @Id
    private String id;
    private String username;
    private String password;
    private String email;

    private String firstName;
    private String lastName;
    private String phone;
    private String address;

    private String avatar;
    private String photo;

    private Date onCreate;
    private Date onUpdate;
    private boolean isActive;
    private UserRole role;

    public User() {
        onCreate = Date.from(Instant.now());
        onUpdate = onCreate;
        isActive = true;
        role = UserRole.USER;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }
    @Override
    public boolean isAccountNonExpired() {
        return isActive;
    }
    @Override
    public boolean isAccountNonLocked() {
        return isActive;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return isActive;
    }
    @Override
    public boolean isEnabled() {
        return isActive;
    }

}
