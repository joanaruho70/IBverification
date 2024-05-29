package com.akanwkasa.dcic.nssf.database;

/**
 * Created by IntelliJ IDEA.
 * Author: Akankwasa Brian
 * Date: 12/7/2020
 * Time: 2:01 PM
 * Contacts : Email = donbrians@ymail.com, Mobile 00256778693362
 */

public class DatabaseQueries {
    //get rvek details
    String travelDetails="SELECT prd.document_number        AS \"document_number\",\n" +
            "       cc.ukey                    AS \"ukey\",\n" +
            "       trav.TYPE                  AS \"travel_type\",\n" +
            "       trav.status                AS \"travel_status\",\n" +
            "       trav.inspection_point_type AS \"inspection_point_type\",\n" +
            "       trav.date_of_travel        AS \"date_of_travel\",\n" +
            "       site.ukey                  As \"point_of_exit\"\n" +
            "FROM   caliburn.pr_document_record prd\n" +
            "       left join caliburn.ca_country cc\n" +
            "              ON prd.issuing_country_id = cc.id\n" +
            "       left join caliburn.sb_travel_record trav\n" +
            "              ON trav.document_record_id = prd.id\n" +
            "       left join caliburn.ca_site site \n" +
            "              ON site.id = trav.site_id\n" +
            "WHERE  prd.document_number = '%s'\n" +
            "       AND cc.ukey = '%s'   \n" +
            "       AND trav.date_of_travel = (SELECT Max(trav.date_of_travel)\n" +
            "                                  FROM   caliburn.pr_document_record prd\n" +
            "                                         left join caliburn.ca_country cc\n" +
            "                                                ON prd.issuing_country_id =\n" +
            "                                                   cc.id\n" +
            "                                         left join caliburn.sb_travel_record\n" +
            "                                                   trav\n" +
            "                                                ON trav.document_record_id =\n" +
            "                                                   prd.id\n" +
            "                                  WHERE  prd.document_number = '%s'\n" +
            "                                         AND cc.ukey = '%s'\n" +
            "                                         AND trav.status = 'Accepted'\n" +
            "                                         AND trav.TYPE = 'Exit')";



    String previousPermits = "SELECT app.visalon_app_id     AS \"application_id\",\n" +
            "       app.visalon_app_type   AS \"application_type\",\n" +
            "       vd1.value              AS \"category\",\n" +
            "       vd2.value              AS \"sub_category\",\n" +
            "       app.application_status AS \"application_status\",\n" +
            "       pd.passport_number     AS \"passport_number\",\n" +
            "       cmtn5.name             AS \"issuing_country\",\n" +
            "       pd.date_of_issue       AS \"document_date_of_issue\",\n" +
            "       pd.date_of_expiry      AS \"document_date_of_expiry\",\n" +
            "       app.organization_code,\n" +
            "       CASE app.organization_code\n" +
            "         WHEN (SELECT vic.organization_code\n" +
            "               FROM   caliburn.vi_company vic\n" +
            "               WHERE  vic.organization_code = app.organization_code) THEN\n" +
            "         (SELECT vic.name\n" +
            "          FROM\n" +
            "       caliburn.vi_company vic\n" +
            "                                                                           WHERE\n" +
            "         vic.organization_code = app.organization_code)\n" +
            "         WHEN (SELECT vnp.organization_code\n" +
            "               FROM   caliburn.vi_non_profit_company vnp\n" +
            "               WHERE  vnp.organization_code = app.organization_code) THEN\n" +
            "         (SELECT vnp.name\n" +
            "          FROM\n" +
            "       caliburn.vi_non_profit_company vnp\n" +
            "                                                                           WHERE\n" +
            "         vnp.organization_code = app.organization_code)\n" +
            "         WHEN (SELECT vmad.organization_code\n" +
            "               FROM   caliburn.vi_ministry_agency_department vmad\n" +
            "               WHERE  vmad.organization_code = app.organization_code) THEN\n" +
            "         (SELECT vmad.name\n" +
            "          FROM\n" +
            "       caliburn.vi_ministry_agency_department vmad\n" +
            "         WHERE\n" +
            "         vmad.organization_code = app.organization_code)\n" +
            "         WHEN (SELECT vs.organization_code\n" +
            "               FROM   caliburn.vi_school vs\n" +
            "               WHERE  vs.organization_code = app.organization_code) THEN\n" +
            "         (SELECT vs.name\n" +
            "          FROM\n" +
            "       caliburn.vi_school vs\n" +
            "                                                                          WHERE\n" +
            "         vs.organization_code = app.organization_code)\n" +
            "       END                    AS \"name_of_employer\",\n" +
            "       app.resolution_date    AS \"permitdate_of_issue\",\n" +
            "       app.date_of_expiry     AS \"permitdate_of_expiry\"\n" +
            "FROM   caliburn.vi_visalon_person_data pd\n" +
            "       left join caliburn.ca_country cc\n" +
            "              ON pd.nationality_id = cc.id\n" +
            "       left join caliburn.vi_visalon_application app\n" +
            "              ON pd.id = app.visalon_person_data_id\n" +
            "       left join caliburn.ca_mo_translated_name cmtn2\n" +
            "              ON cmtn2.parent_id = pd.country_of_birth_id\n" +
            "       left join caliburn.ca_mo_translated_name cmtn5\n" +
            "              ON cmtn5.parent_id = pd.issuing_country_id\n" +
            "       left join caliburn.vi_descriptions VD1\n" +
            "              ON app.category = VD1.KEY\n" +
            "       left join caliburn.vi_descriptions VD2\n" +
            "              ON app.subcategory = VD2.KEY\n" +
            "WHERE  app.visalon_app_type IN ( 'ENTRY_PERMIT', 'CERTIFICATE_OF_RESIDENCE','STUDENT_PASS','DEPENDANT_PASS','CITIZENSHIP','VISITORS_PERMIT','SPECIAL_PASS' )\n" +
            "       AND pd.passport_number = '%s'\n" +
            "       AND cc.ukey = '%s'";

    String immigrationStatus = "SELECT pd.passport_number     AS \"passport_number\",\n" +
            "       cc.ukey                AS \"country_code\",\n" +
            "       pd.previous_passports  AS \"previous_passports\",\n" +
            "       pd.last_name           AS \"surname\",\n" +
            "       pd.first_name          AS \"given_name\",\n" +
            "       cag.ukey               AS \"gender\",\n" +
            "       Trunc(pd.birthday)     AS \"birthday\",\n" +
            "       pd.place_of_birth      AS \"place_of_birth\",\n" +
            "       cmtn2.NAME             AS \"country_of_birth\",\n" +
            "       cmtn5.NAME             AS \"issuing_country\",\n" +
            "       cmtn.NAME              AS \"nationality\",\n" +
            "       cmtn3.NAME             AS \"country_of_residence\",\n" +
            "       app.visalon_app_id     AS \"visalon_app_id\",\n" +
            "       app.visalon_app_type   AS \"visalon_app_type\",\n" +
            "       vd1.value              AS \"category\",\n" +
            "       vd2.value              AS \"subcategory\",\n" +
            "       CASE app.organization_code\n" +
            "         WHEN (SELECT vic.organization_code\n" +
            "               FROM   caliburn.vi_company vic\n" +
            "               WHERE  vic.organization_code = app.organization_code) THEN\n" +
            "         (SELECT vic.NAME\n" +
            "          FROM\n" +
            "       caliburn.vi_company vic\n" +
            "                                                                           WHERE\n" +
            "         vic.organization_code = app.organization_code)\n" +
            "         WHEN (SELECT vnp.organization_code\n" +
            "               FROM   caliburn.vi_non_profit_company vnp\n" +
            "               WHERE  vnp.organization_code = app.organization_code) THEN\n" +
            "         (SELECT vnp.NAME\n" +
            "          FROM\n" +
            "       caliburn.vi_non_profit_company vnp\n" +
            "                                                                           WHERE\n" +
            "         vnp.organization_code = app.organization_code)\n" +
            "         WHEN (SELECT vmad.organization_code\n" +
            "               FROM   caliburn.vi_ministry_agency_department vmad\n" +
            "               WHERE  vmad.organization_code = app.organization_code) THEN\n" +
            "         (SELECT vmad.NAME\n" +
            "          FROM\n" +
            "       caliburn.vi_ministry_agency_department vmad\n" +
            "         WHERE\n" +
            "         vmad.organization_code = app.organization_code)\n" +
            "         WHEN (SELECT vs.organization_code\n" +
            "               FROM   caliburn.vi_school vs\n" +
            "               WHERE  vs.organization_code = app.organization_code) THEN\n" +
            "         (SELECT vs.NAME\n" +
            "          FROM\n" +
            "       caliburn.vi_school vs\n" +
            "                                                                          WHERE\n" +
            "         vs.organization_code = app.organization_code)\n" +
            "       END                    AS \"name_of_employer\",\n" +
            "       vcpd.phone_number_work AS \"employer_telephone_number\",\n" +
            "       pd.spouse_name         AS \"spouse_name\",\n" +
            "       cb2.payload            AS \"signature\",\n" +
            "       cb.payload             AS \"photo\"\n" +
            "FROM   caliburn.vi_visalon_person_data pd\n" +
            "       LEFT JOIN caliburn.ca_country cc\n" +
            "              ON pd.nationality_id = cc.id\n" +
            "       LEFT JOIN caliburn.vi_visalon_application app\n" +
            "              ON pd.id = app.visalon_person_data_id\n" +
            "       LEFT JOIN caliburn.vi_entry_permit vep\n" +
            "              ON vep.id = app.id\n" +
            "       LEFT JOIN caliburn.vi_common_permit_data vcpd\n" +
            "              ON vcpd.id = app.id\n" +
            "       LEFT JOIN caliburn.ca_gender cag\n" +
            "              ON pd.gender_id = cag.id\n" +
            "       LEFT JOIN caliburn.ca_mo_translated_name cmtn\n" +
            "              ON cmtn.parent_id = pd.nationality_id\n" +
            "       LEFT JOIN caliburn.ca_mo_translated_name cmtn2\n" +
            "              ON cmtn2.parent_id = pd.country_of_birth_id\n" +
            "       LEFT JOIN caliburn.ca_mo_translated_name cmtn5\n" +
            "              ON cmtn5.parent_id = pd.issuing_country_id\n" +
            "       LEFT JOIN caliburn.ca_mo_translated_name cmtn3\n" +
            "              ON cmtn3.parent_id = vcpd.country_of_residence\n" +
            "       LEFT JOIN caliburn.ca_mo_translated_name cmtn4\n" +
            "              ON cmtn4.parent_id = vep.profession_id\n" +
            "       LEFT JOIN caliburn.vi_descriptions VD1\n" +
            "              ON app.category = VD1.KEY\n" +
            "       LEFT JOIN caliburn.vi_descriptions VD2\n" +
            "              ON app.category = VD2.KEY              \n" +
            "       LEFT JOIN caliburn.pr_person_record pr\n" +
            "              ON app.person_record_id = pr.id\n" +
            "       LEFT JOIN caliburn.pr_photo_record ph\n" +
            "              ON pr.photo_id = ph.id\n" +
            "       LEFT JOIN caliburn.ca_binary cb\n" +
            "              ON ph.binary_id = cb.id\n" +
            "       LEFT JOIN caliburn.ca_signature sig\n" +
            "              ON pr.signature_id = ph.id\n" +
            "       LEFT JOIN caliburn.ca_binary cb2\n" +
            "              ON sig.binary_id = cb2.id\n" +
            "WHERE  pd.passport_number = '%s'\n" +
            "       AND cc.ukey = '%s'\n" +
            "       AND app.date_of_expiry = (SELECT Max(app.date_of_expiry)\n" +
            "                                 FROM   caliburn.vi_visalon_application app\n" +
            "           LEFT JOIN caliburn.vi_visalon_person_data pd\n" +
            "                  ON pd.id = app.visalon_person_data_id\n" +
            "           LEFT JOIN caliburn.ca_country cc\n" +
            "                  ON pd.nationality_id = cc.id\n" +
            "                                 WHERE  pd.passport_number = '%s'\n" +
            "                                        AND cc.ukey = '%s')";


    //used by the getPrtmitInformation query to get person details
    String personInformation = "SELECT pd.id                      AS \"vi_visalon_application_row_id\",\n" +
            "       pd.passport_number         AS \"passport_number\",\n" +
            "       cc.ukey                    AS \"country_code\",\n" +
            "       app.visalon_app_id         AS \"application_id\",\n" +
            "       app.application_status     AS \"application_status\",\n" +
            "       app.resolution_date        AS \"approval_date\",\n" +
            "       app.date_of_expiry         AS \"date_of_expiry\",\n" +
            "       pd.last_name               AS \"surname\",\n" +
            "       pd.first_name              AS \"given_name\",\n" +
            "       cag.ukey                   AS \"gender\",\n" +
            "       Trunc(pd.birthday)         AS \"birthday\",\n" +
            "       cmtn.NAME                  AS \"nationality\",\n" +
            "       pd.place_of_birth          AS \"place_of_birth\",\n" +
            "       cmtn3.name                 AS \"country_of_residence\",\n" +
            "       vcpd.residential_address   AS \"residential_address\",\n" +
            "       app.visalon_app_type       AS \"application_type\",\n" +
            "       vd1.value                  AS \"category\",\n" +
            "       vd2.value                  AS \"subcategory\",       \n" +
            "       CASE app.organization_code\n" +
            "         WHEN (SELECT vic.organization_code\n" +
            "               FROM   caliburn.vi_company vic\n" +
            "               WHERE  vic.organization_code = app.organization_code) THEN\n" +
            "         (SELECT vic.NAME\n" +
            "          FROM\n" +
            "       caliburn.vi_company vic\n" +
            "                                                                           WHERE\n" +
            "         vic.organization_code = app.organization_code)\n" +
            "         WHEN (SELECT vnp.organization_code\n" +
            "               FROM   caliburn.vi_non_profit_company vnp\n" +
            "               WHERE  vnp.organization_code = app.organization_code) THEN\n" +
            "         (SELECT vnp.NAME\n" +
            "          FROM\n" +
            "       caliburn.vi_non_profit_company vnp\n" +
            "                                                                           WHERE\n" +
            "         vnp.organization_code = app.organization_code)\n" +
            "         WHEN (SELECT vmad.organization_code\n" +
            "               FROM   caliburn.vi_ministry_agency_department vmad\n" +
            "               WHERE  vmad.organization_code = app.organization_code) THEN\n" +
            "         (SELECT vmad.NAME\n" +
            "          FROM\n" +
            "       caliburn.vi_ministry_agency_department vmad\n" +
            "         WHERE\n" +
            "         vmad.organization_code = app.organization_code)\n" +
            "         WHEN (SELECT vs.organization_code\n" +
            "               FROM   caliburn.vi_school vs\n" +
            "               WHERE  vs.organization_code = app.organization_code) THEN\n" +
            "         (SELECT vs.NAME\n" +
            "          FROM\n" +
            "       caliburn.vi_school vs\n" +
            "                                            WHERE\n" +
            "         vs.organization_code = app.organization_code)\n" +
            "       END                        AS \"name_of_employer\",\n" +
            "       app.organization_code      AS \"organization_code\",\n" +
            "       vcpd.phone_number_personal AS \"telephone_number\",\n" +
            "       vep.occupation             AS \"occupation\",\n" +
            "       cmtn4.NAME                 AS \"profession\",\n" +
            "       pd.email                   AS \"email_address\",\n" +
            "       cb.payload                 AS \"photo\"\n" +
            "FROM   caliburn.vi_visalon_person_data pd\n" +
            "       LEFT JOIN caliburn.ca_country cc\n" +
            "              ON pd.nationality_id = cc.id\n" +
            "       LEFT JOIN caliburn.vi_visalon_application app\n" +
            "              ON pd.id = app.visalon_person_data_id\n" +
            "       LEFT JOIN caliburn.vi_entry_permit vep\n" +
            "              ON vep.id = app.id\n" +
            "       LEFT JOIN caliburn.vi_common_permit_data vcpd\n" +
            "              ON vcpd.id = app.id\n" +
            "       LEFT JOIN caliburn.ca_gender cag\n" +
            "              ON pd.gender_id = cag.id\n" +
            "       LEFT JOIN caliburn.ca_mo_translated_name cmtn\n" +
            "              ON cmtn.parent_id = pd.nationality_id\n" +
            "       LEFT JOIN caliburn.ca_mo_translated_name cmtn3\n" +
            "              ON cmtn3.parent_id = vcpd.country_of_residence\n" +
            "       LEFT JOIN caliburn.ca_mo_translated_name cmtn4\n" +
            "              ON cmtn4.parent_id = vep.profession_id\n" +
            "       LEFT JOIN caliburn.vi_descriptions VD1\n" +
            "              ON app.category = VD1.KEY\n" +
            "       LEFT JOIN caliburn.vi_descriptions VD2\n" +
            "              ON app.subcategory = VD2.KEY              \n" +
            "       LEFT JOIN caliburn.pr_person_record pr\n" +
            "              ON app.person_record_id = pr.id\n" +
            "       LEFT JOIN caliburn.pr_photo_record ph\n" +
            "              ON pr.photo_id = ph.id\n" +
            "       LEFT JOIN caliburn.ca_binary cb\n" +
            "              ON ph.binary_id = cb.id\n" +
            "       LEFT JOIN caliburn.pr_r_persr_hands prh\n" +
            "              ON pr.hands_record_id = prh.hands_record_id\n" +
            "WHERE  pd.passport_number = '%s'\n" +
            "       AND cc.ukey = '%s'\n" +
            "       AND app.date_of_expiry = (SELECT Max(app.date_of_expiry)\n" +
            "                                 FROM   caliburn.vi_visalon_application app\n" +
            "           LEFT JOIN caliburn.vi_visalon_person_data pd\n" +
            "                  ON pd.id = app.visalon_person_data_id\n" +
            "           LEFT JOIN caliburn.ca_country cc\n" +
            "                  ON pd.nationality_id = cc.id\n" +
            "                                 WHERE  pd.passport_number = '%s'\n" +
            "                                        AND cc.ukey = '%s')";



    public String getPersonInformation(String passportNumber, String issuingCountry) {
        return String.format(personInformation, passportNumber, issuingCountry, passportNumber, issuingCountry);
    }

    public String getImmigrationStatus(String passportNumber, String issuingCountry) {
        return String.format(immigrationStatus, passportNumber, issuingCountry, passportNumber, issuingCountry);
    }

    public String getPreviousPermits(String passportNumber, String issuingCountry) {
        return String.format(previousPermits, passportNumber, issuingCountry);
    }

    public String getTravelDetails(String passportNumber, String issuingCountry) {
        return String.format(travelDetails, passportNumber, issuingCountry, passportNumber, issuingCountry);
    }



    //#############use to retrieve the status of a visa
    public String retrieveVisaApplicationStatus(String applicationNumber){
        String query="select\n" +
                "viap.VISALON_APP_ID, viap.APPLICATION_STATUS,viap.VISALON_APP_TYPE,viap.RESOLUTION_DATE,(select vd1.value from CALIBURN.vi_descriptions VD1 where VD1.KEY=viap.CATEGORY) as CATEGORY,\n" +
                "(select vd2.value from CALIBURN.vi_descriptions VD2 where VD2.KEY=viap.SUBCATEGORY) as SUBCATEGORY, viap.CREATION_DATE, viap.DATE_OF_EXPIRY,\n" +
                "info.BIRTHDAY,info.FIRST_NAME, info.LAST_NAME,info.EMAIL,info.PASSPORT_NUMBER, info.PLACE_OF_ISSUE,\n" +
                "(select NAME from caliburn.CA_MO_TRANSLATED_NAME where PARENT_ID=info.ISSUING_COUNTRY_ID) as Issuing_country,\n" +
                "(select NAME from caliburn.CA_MO_TRANSLATED_NAME where PARENT_ID=info.NATIONALITY_ID) as Nationality \n" +
                "from caliburn.VI_VISALON_APPLICATION viap  left join  caliburn.VI_VISALON_PERSON_DATA info on info.ID=viap.visalon_person_data_id where  viap.VISALON_APP_ID='%s'";
        return String.format(query,applicationNumber);
    }

    //#############use to retrieve the status of a visa
    public String retrieveVisaApplicationStatusWithPassportNumber(String passportNumber, String issuingCountry){
        String query="select  viap.VISALON_APP_ID, viap.APPLICATION_STATUS,viap.VISALON_APP_TYPE,viap.RESOLUTION_DATE," +
                "(select vd1.value from CALIBURN.vi_descriptions VD1 where VD1.KEY=viap.CATEGORY) as CATEGORY, (select vd2.value from CALIBURN.vi_descriptions VD2 where VD2.KEY=viap.SUBCATEGORY) as SUBCATEGORY, viap.CREATION_DATE, viap.DATE_OF_EXPIRY, info.BIRTHDAY,info.FIRST_NAME, info.LAST_NAME,info.EMAIL,info.PASSPORT_NUMBER, info.PLACE_OF_ISSUE," +
                "(select NAME from caliburn.CA_MO_TRANSLATED_NAME where PARENT_ID=info.ISSUING_COUNTRY_ID) as Issuing_country,\n" +
                "(select NAME from caliburn.CA_MO_TRANSLATED_NAME where PARENT_ID=info.NATIONALITY_ID) as Nationality \n" +
                "from caliburn.VI_VISALON_APPLICATION viap left join  caliburn.VI_VISALON_PERSON_DATA info on info.ID=viap.VISALON_PERSON_DATA_ID  join caliburn.CA_COUNTRY cc on cc.ukey='%s' where  info.passport_number='%s'";
        return String.format(query,issuingCountry,passportNumber);
    }

}
