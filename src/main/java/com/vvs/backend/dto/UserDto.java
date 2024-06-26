package com.vvs.backend.dto;

import java.util.Date;

import com.vvs.backend.model.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String id;
    private String username;
    private String password;
    private String email;

    private String firstName;
    private String lastName;
    private String phone;
    private String address;

    private String photo;
    private String avatar;

    private Date onCreate;
    private Date onUpdate;
    private boolean isActive;
    private UserRole role;

}
