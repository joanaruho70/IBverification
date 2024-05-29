package com.akanwkasa.dcic.nssf.requests_and_response;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * Author: Akankwasa Brian
 * Date: 1/3/2022
 * Time: 7:13 PM
 * Contacts : Email = donbrians@ymail.com, Mobile 00256778693362
 */

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class DCICUSerProfile implements Serializable {
    private String id;
    private String creation_date;
    private String deleted;
    private String modification_date;
    private String version;
    private Integer active;
    private String address;
    private String description;
    private String email;
    private String firstname;
    private String id_number;
    private String lastname;
    private String passwd;
    private String password_expiry_dATE;
    private String reattempts;
    //private String synchronized;
    private String telephone;
    private String user_editable;
    private String username;
    private String language_id;
    private String organization_id;
}
