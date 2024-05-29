package com.akanwkasa.dcic.nssf.requests_and_response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * Author: Akankwasa Brian
 * Date: 1/4/2022
 * Time: 4:04 PM
 * Contacts : Email = donbrians@ymail.com, Mobile 00256778693362
 */

@Setter
@Getter
@NoArgsConstructor

public class DCICUserRoles implements Serializable {
    private String id;
    private String ukey;
    private String version;
    private String active;
    private String creation_date;
    private String deleted;
    private String modification_date;
    private String user_id;
    private String role_id;

}
