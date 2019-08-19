package com.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.validator.constraints.Email;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Document(collection = "users")
@Data
@NoArgsConstructor
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Field("_id")
    private String id;

    @NotNull
    @Field("name")
    private String name;

    @Email
    @NotNull
    @Field("email")
    private String email;

    @NotNull
    private String password;

    @Field("role")
    private String role;

    public User(String name, String email, String password, String role) {
        this.setName(name);
        this.setEmail(email);
        this.setPassword(password);
        this.setRole(role);
    }

    @JsonIgnore
    public String getPassword() {
        return this.password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }
}
