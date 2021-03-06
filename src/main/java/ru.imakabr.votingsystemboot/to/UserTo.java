package ru.imakabr.votingsystemboot.to;


import ru.imakabr.votingsystemboot.model.AbstractNamedEntity;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class UserTo extends AbstractNamedEntity implements Serializable {
    private static final long serialVersionUID = 1L;

//    @NotBlank
//    @Size(min = 2, max = 100)
//    private String name;

    @Email
    @NotBlank
    @Size(max = 100)
    private String email;

    @NotBlank
    @Size(min = 5, max = 32, message = "length must be between 5 and 32 characters")
    private String password;

    public UserTo() {
    }

    public UserTo(Integer id, String name, String email, String password) {
        super(id, name);
//        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserTo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
