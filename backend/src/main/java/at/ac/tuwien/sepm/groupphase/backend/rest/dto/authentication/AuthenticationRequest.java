package at.ac.tuwien.sepm.groupphase.backend.rest.dto.authentication;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "AuthenticationRequest",
          description = "Data Transfer Objects for Authentication Requests via REST")
public class AuthenticationRequest {

    @ApiModelProperty(required = true, name = "The unique name of the user", example = "admin")
    private String email;

    @ApiModelProperty(required = true, name = "The password of the user", example = "password")
    private String password;


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public CharSequence getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public String toString() {
        return "AuthenticationRequest{" +
               "email='" + email + '\'' +
               ", password='" + password + '\'' +
               '}';
    }


    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        AuthenticationRequest that = (AuthenticationRequest) o;

        if(email != null ? !email.equals(that.email) : that.email != null) return false;
        return password != null ? password.equals(that.password) : that.password == null;
    }


    @Override
    public int hashCode() {
        int result = email != null ? email.hashCode() : 0;
        result = 31 * result + ( password != null ? password.hashCode() : 0 );
        return result;
    }


    public static AuthenticationRequestBuilder builder() {
        return new AuthenticationRequestBuilder();
    }


    public static final class AuthenticationRequestBuilder {

        private String username;
        private String password;


        public AuthenticationRequestBuilder username(String username) {
            this.username = username;
            return this;
        }


        public AuthenticationRequestBuilder password(String password) {
            this.password = password;
            return this;
        }


        public AuthenticationRequest build() {
            AuthenticationRequest authenticationRequest = new AuthenticationRequest();
            authenticationRequest.setEmail(username);
            authenticationRequest.setPassword(password);
            return authenticationRequest;
        }
    }
}
