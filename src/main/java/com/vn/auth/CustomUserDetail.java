package com.vn.auth;

import com.vn.model.Users;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Setter
@Getter
public class CustomUserDetail implements UserDetails {

    final Users usersDB;

    public CustomUserDetail(Users usersDB) {
        this.usersDB = usersDB;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = usersDB.getRole().name();
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return usersDB.getPassword();
    }

    @Override
    public String getUsername() {
        return usersDB.getUsername();
    }
}