package com.casdoor.demo.entity;


import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


//@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class UserLiaison {

    private String casdoorUsername;
    private String casdoorPassword;
    private String casdoorEmail;
    private String casdoorMobileNumber;

    private UserLiaison(String casdoorUsername, String casdoorPassword, String casdoorEmail, String casdoorMobileNumber) {
        this.casdoorUsername = casdoorUsername;
        this.casdoorPassword = casdoorPassword;
        this.casdoorEmail = casdoorEmail;
        this.casdoorMobileNumber = casdoorMobileNumber;
    }


    public static class Builder {

        private String casdoorUsername;
        private String casdoorPassword;
        private String casdoorEmail;
        private String casdoorMobileNumber;

        public Builder casdoorUsername(String casdoorUsername) {
            this.casdoorUsername = casdoorUsername;
            return this;
        }

        public Builder casdoorPassword(String casdoorPassword) {
            this.casdoorPassword = casdoorPassword;
            return this;
        }

        public Builder casdoorEmail(String casdoorEmail) {
            this.casdoorEmail = casdoorEmail;
            return this;
        }

        public Builder casdoorMobileNumber(String casdoorMobileNumber) {
            this.casdoorMobileNumber = casdoorMobileNumber;
            return this;
        }

        public UserLiaison build() {
            return new UserLiaison(casdoorUsername, casdoorPassword, casdoorEmail, casdoorMobileNumber);
        }
    }




}
