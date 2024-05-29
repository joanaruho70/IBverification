package com.akanwkasa.dcic.nssf.Core_Proccessing;

import com.akanwkasa.dcic.nssf.database.DatabaseQueries;
import com.akanwkasa.dcic.nssf.helpers.CommonOperations;
import com.akanwkasa.dcic.nssf.helpers.Response;
import com.akanwkasa.dcic.nssf.requests_and_response.*;
import com.akanwkasa.dcic.nssf.requests_and_response.visaapplicationstatus.VisaApplicationStatusRequest;
import com.akanwkasa.dcic.nssf.requests_and_response.visaapplicationstatus.VisaApplicationStatusResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * Author: Akankwasa Brian
 * Date: 12/7/2020
 * Time: 2:02 PM
 * Contacts : Email = donbrians@ymail.com, Mobile 00256778693362
 */

@Component(value = "ApplicationCoreProccessing")
@Slf4j
public class ApplicationCoreProcessing {
    private DatabaseQueries db_queries;
    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public ApplicationCoreProcessing(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        db_queries = new DatabaseQueries();
        this.objectMapper = new ObjectMapper();
    }

    //get the permit information of a person
    public Object getPermitInformation(String PassportNumber, String issuingCountry) throws ParseException, UnsupportedEncodingException, SQLException {
        try {
            String get_peron_information_database_query = db_queries.getPersonInformation(PassportNumber, issuingCountry);
            List<Map<String, Object>> resultSet = jdbcTemplate.queryForList(get_peron_information_database_query);

            if (resultSet.size() > 0) {
                Map row = resultSet.get(0);
                String vi_visalon_application_row_id = row.get("vi_visalon_application_row_id").toString();

                //confirm that the visa_lon application id is not null before you can proceed
                if (vi_visalon_application_row_id != null) {
                    String vi_visalon_application_query = "select *  from caliburn.VI_VISALON_APPLICATION where visalon_person_data_id=" + vi_visalon_application_row_id;

                    Map vi_visalon_application_result = jdbcTemplate.queryForMap(vi_visalon_application_query);

                    //get the person record details from the PR_PERSON_RECORD table
                    Map pr_person_record_result = jdbcTemplate.queryForMap("select *  from caliburn.PR_PERSON_RECORD where id='" + vi_visalon_application_result.get("person_record_id") + "'");

                    //get the list of finger prints from the fp_finger_prints table
                    List<Map<String, Object>> finger_prints = jdbcTemplate.queryForList("select fp.* from caliburn.FP_FINGERPRINT fp join caliburn.PR_R_PERSR_HANDS prh on prh.FINGERPRINT_ID=fp.id where prh.HANDS_RECORD_ID='" + pr_person_record_result.get("hands_record_id") + "'");

                    List<FingerImages> fingerImages = new ArrayList<>();

                    for (Map finger_print : finger_prints) {
                        if (finger_print.get("BINARY_ID") != null) {
                            //select each individual image from the ca-binary table and match it to the finger print
                            Map ca_binary_finger_blob = jdbcTemplate.queryForMap("select *  from caliburn.CA_BINARY where ID='" + finger_print.get("BINARY_ID") + "'");

                            FingerImages images = new FingerImages();
                            images.setFingerImage(new CommonOperations().convertBlobToBase64String(ca_binary_finger_blob.get("PAYLOAD")));
//                            images.setFingerImage(ca_binary_finger_blob.get("PAYLOAD"));
                            images.setFingerType(finger_print.get("FINGER_TYPE").toString());
                            images.setFingerPrintQuality(Integer.parseInt(finger_print.get("QUALITY").toString()));

                            fingerImages.add(images);
                        }
                    }

                    //now we build the person data record
                    PersonData personData = new PersonData();
                    personData.setFingerImages(fingerImages);
                    personData.setFirstName(row.get("surname").toString());
                    personData.setSurName(row.get("given_name").toString());
                    personData.setGender(row.get("gender").toString());
                    personData.setBirthday(truncateDate(row.get("birthday")));
                    personData.setNationality((String) row.get("nationality"));
                    personData.setPlaceOfBirth((String) row.get("place_of_birth"));
                    personData.setCountryOfResidence(String.valueOf(row.get("country_of_residence")));
                    personData.setResidentialAddress((String) row.get("residential_address"));
                    personData.setPhotoImage(row.get("photo"));
                    personData.setEmail((String) row.get("email_address"));
                    personData.setPhoneNumberPersonal((String) row.get("telephone_number"));
                    personData.setOrganizationCode((String) row.get("organization_code"));
                    personData.setDocumentDateOfExpiry(truncateDate(row.get("date_of_expiry")));
                    personData.setPermitNumber(String.valueOf(row.get("application_id")));
                    personData.setDocumentDateOfIssue(truncateDate(row.get("approval_date")));
                    personData.setNameOfEmployer(String.valueOf(row.get("name_of_employer")));
                    personData.setOccupation(String.valueOf(row.get("occupation")));
                    personData.setProfession(String.valueOf(row.get("profession")));

                    //build the permit information response in general
                    GetPermitInformationResponse permitInformationResponse = new GetPermitInformationResponse();
                    permitInformationResponse.setPersonData(personData);
                    permitInformationResponse.setApplicationType((String) row.get("application_type"));
                    permitInformationResponse.setCategory((String) row.get("category"));
                    permitInformationResponse.setSubcategory((String) row.get("subcategory"));

                    return permitInformationResponse;
                } else {
                    return null;
                }

            } else {
                return null;
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            throw exception;
        }
    }

    //immigration status of a person also showing the permit information of the same person
    public Object getImmigrationstatus(String PassportNumber, String issuingCountry) throws ParseException {
        try {
            String get_immigration_status_database_query = db_queries.getImmigrationStatus(PassportNumber, issuingCountry);
            List<Map<String, Object>> resultSet = jdbcTemplate.queryForList(get_immigration_status_database_query);

            if (resultSet.size() > 0) {
                Map row = resultSet.get(0);
                String visalon_app_id = row.get("visalon_app_id").toString();

                //confirm that the visa_lon application id is not null before you can proceed
                if (visalon_app_id != null) {

                    //now we build the person data record
                    GetImmigrationStatusResponse immigrationStatusResponse = new GetImmigrationStatusResponse();
                    immigrationStatusResponse.setSurName(row.get("surname"));
                    immigrationStatusResponse.setFirstName(row.get("given_name"));
                    immigrationStatusResponse.setGender(row.get("gender"));
                    immigrationStatusResponse.setDateOfBirth(truncateDate(row.get("birthday")));
                    immigrationStatusResponse.setCategory(row.get("category"));
                    immigrationStatusResponse.setSpouseName(row.get("spouse_name"));
                    immigrationStatusResponse.setCountryOfBirth(row.get("country_of_birth"));
                    immigrationStatusResponse.setCountryOfResidence(row.get("country_of_residence"));
                    immigrationStatusResponse.setNationality(row.get("nationality"));
                    immigrationStatusResponse.setPhotoImage(row.get("photo"));
                    immigrationStatusResponse.setSignatureImage(row.get("signature"));
                    immigrationStatusResponse.setPlaceOfBirth(row.get("place_of_birth"));
                    immigrationStatusResponse.setPreviousPassports(row.get("previous_passports"));
                    immigrationStatusResponse.setEmployerTelephoneNumber(row.get("employer_telephone_number"));
                    immigrationStatusResponse.setNameOfEmployer(row.get("name_of_employer")); //already added
                    immigrationStatusResponse.setPermitNumber(row.get("visalon_app_id"));//return---permit_number and not passport number
                    immigrationStatusResponse.setApplicationType(row.get("visalon_app_type"));//return---permit_number and not passport number


                    //get the list of permits
                    String previous_permits_query = db_queries.getPreviousPermits(PassportNumber, issuingCountry);
                    List<Map<String, Object>> previous_permits = jdbcTemplate.queryForList(previous_permits_query);

                    List<PermitDetails> permitDetailsList = new ArrayList<>();

                    for (Map permit : previous_permits) {
                        //select each individual permits from the ca-binary table and match it to the finger print
                        PermitDetails permitDetails = new PermitDetails();
                        permitDetails.setApplicationType(permit.get("application_type"));
                        permitDetails.setCategory(permit.get("category"));
                        permitDetails.setSubcategory(permit.get("sub_category"));
                        permitDetails.setStatus(permit.get("application_status"));
                        permitDetails.setDocumentDateOfIssue(truncateDate(permit.get("document_date_of_issue")));
                        permitDetails.setDocumentDateOfExpiry(truncateDate(permit.get("document_date_of_expiry")));
                        permitDetails.setOrganizationCode(permit.get("organization_code"));
                        permitDetails.setIssuingCountry(permit.get("issuing_country"));

                        permitDetails.setPermitDateOfExpiry(truncateDate(permit.get("permitdate_of_expiry")));
                        permitDetails.setPermitDateOfIssue(truncateDate(permit.get("permitdate_of_issue")));

                        permitDetails.setPermitNumber(permit.get("application_id"));
                        permitDetails.setPermitType(permit.get("application_type"));
                        permitDetailsList.add(permitDetails);
                    }

                    immigrationStatusResponse.setPermits(permitDetailsList);


                    //set the travel details to the immigration response
                    Map<String, Object> travelDetails;
                    List<Map<String, Object>> travelDetailsList = jdbcTemplate.queryForList(db_queries.getTravelDetails(PassportNumber, issuingCountry));

                    if (travelDetailsList.size() > 0) {
                        //pick the first record in the array and iterate
                        travelDetails = travelDetailsList.get(0);
                        immigrationStatusResponse.setTravelDate(truncateDate(travelDetails.get("date_of_travel")));
                        immigrationStatusResponse.setPointOfExit(travelDetails.getOrDefault("point_of_exit", null));
                        immigrationStatusResponse.setTravelStatus(travelDetails.getOrDefault("travel_status", null));
                    }

                    return immigrationStatusResponse;
                } else {
                    return null;
                }

            } else {
                return null;
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            throw exception;
        }
    }


    // this just indicates the travel details of the client
    public Object getTravelStatus(String PassportNumber, String issuingCountry) throws Exception {
        try {
            String getTravelDetails = db_queries.getTravelDetails(PassportNumber, issuingCountry);
            List<Map<String, Object>> resultSet = jdbcTemplate.queryForList(getTravelDetails);

            Map<String, Object> travelStatusMap = null;
            HaseftCountryResponse haseftCountryResponse = new HaseftCountryResponse();
            if (resultSet.size() > 0) {
                travelStatusMap = resultSet.get(0);
                haseftCountryResponse.setTravelDate(truncateDate(travelStatusMap.get("date_of_travel")));
                haseftCountryResponse.setPointOfExit(travelStatusMap.getOrDefault("point_of_exit", null));
                haseftCountryResponse.setTravelStatus(travelStatusMap.getOrDefault("travel_status", null));
                haseftCountryResponse.setTravelType(travelStatusMap.getOrDefault("travel_type", null));

                return haseftCountryResponse;
            } else {
                return null;
            }


        } catch (Exception exception) {
            exception.printStackTrace();
            throw exception;
        }
    }

    private String truncateDate(Object date) throws ParseException {
        if (date != null) {
            Date d = (Date) date;

            String pattern = "yyyy-MM-dd";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            //simpleDateFormat.parse(date.toString()).toString();
            return simpleDateFormat.format(d);
        }

        return null;
    }

    public String login(String username, String password) throws JsonProcessingException {
        String hashPassword = hashPassword(password);
        String query = String.format("select * from caliburn.CA_USER where USERNAME='%s' and PASSWD='%s'", username, hashPassword);
        List<Map<String, Object>> resultSet = jdbcTemplate.queryForList(query);

        if (resultSet.size() == 0) {
            return null;
        } else {
            return new ObjectMapper().writeValueAsString(resultSet.get(0));
        }

    }

    private String hashPassword(String password) {

        MessageDigest sha;
        try {
            sha = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException var5) {
            throw new RuntimeException(var5);
        }

        byte[] hash = sha.digest(password.getBytes());

        return Base64.getEncoder().encodeToString(hash);
    }

    public boolean getUserRoles(String id) throws JsonProcessingException {
        //get the user roles to confirm is user is allowed to use the system on not
        String rolesQuery = String.format("select * from ca_role roles join CA_R_USER_ROLE  role_ids on  role_ids.role_id=roles.id  where role_ids.USER_ID='%s'", id);
        List<Map<String, Object>> roles = jdbcTemplate.queryForList(rolesQuery);
        boolean allowedRoleFound = false;

        if (roles.size() > 0) {

            for (int i = 0; i < roles.size(); i++) {
                DCICUserRoles dcicUserRoles = this.objectMapper.readValue(this.objectMapper.writeValueAsString(roles.get(i)).toLowerCase(), DCICUserRoles.class);
                if (dcicUserRoles.getUkey().equalsIgnoreCase("PERSON_INFORMATION_INTEGRATOR")) {
                    allowedRoleFound = true;
                    break;
                }
            }
        }

        return allowedRoleFound;
    }


    //############ this function is use to return the visa status from the database and return it to the web portal
    public Response checkVisaApplicationStatus(VisaApplicationStatusRequest input) throws JsonProcessingException {
        try {
            VisaApplicationStatusRequest request = input;//= this.objectMapper.readValue(input, VisaApplicationStatusRequest.class);

            String query;
            String documentStatementToDisplay;


            //if the document type is equal to application, covers all permits else it is considered a passport
            if(request.getDocumentType().equalsIgnoreCase("application")) {
                documentStatementToDisplay="Application with id "+request.getApplicationId();
                query=this.db_queries.retrieveVisaApplicationStatus(request.getApplicationId());
            }else{
                documentStatementToDisplay="Passport with Number "+request.getPassport()+ " Issued by "+request.getIssuingCountry();

                query=this.db_queries.retrieveVisaApplicationStatusWithPassportNumber(request.getPassport(), request.getIssuingCountry());
            }

            List<Map<String, Object>> resultSet = jdbcTemplate.queryForList(query);

            VisaApplicationStatusResponse response = new VisaApplicationStatusResponse();
            if (resultSet.size() > 0) {
                Map<String, Object> resultFromDatabase = resultSet.get(0);

                response.setApplicationId(resultFromDatabase.getOrDefault("VISALON_APP_ID", ""));
                response.setApplicationStatus(resultFromDatabase.getOrDefault("APPLICATION_STATUS", ""));
                response.setApplicationType(resultFromDatabase.getOrDefault("VISALON_APP_TYPE", ""));

                //translate the application status to a more meaningful status that can be understood by the airlines
                response.setApplicationStatus(translateVisaApplicationStatus(response.getApplicationType().toString(), response.getApplicationStatus().toString()));

                response.setCategory(resultFromDatabase.getOrDefault("CATEGORY", ""));

                response.setCreationDate(parseDate(resultFromDatabase.getOrDefault("CREATION_DATE", "")));
                response.setExpiryDate(parseDate(resultFromDatabase.getOrDefault("DATE_OF_EXPIRY", "")));
                response.setVisaIssueDate(parseDate(resultFromDatabase.getOrDefault("RESOLUTION_DATE", "")));
                response.setSubcategory(resultFromDatabase.getOrDefault("SUBCATEGORY", ""));
                response.setPaymentStatus(resultFromDatabase.getOrDefault("EPAYMENT_STATUS", ""));
                response.setBirthDay(parseDate(resultFromDatabase.getOrDefault("BIRTHDAY", "")));
                response.setFirstName(resultFromDatabase.getOrDefault("FIRST_NAME", ""));
                response.setLastName(resultFromDatabase.getOrDefault("LAST_NAME", ""));
                response.setEmail(resultFromDatabase.getOrDefault("EMAIL", ""));
                response.setPassportNumber(resultFromDatabase.getOrDefault("PASSPORT_NUMBER", ""));
                response.setPassportIssuingCountry(resultFromDatabase.getOrDefault("PLACE_OF_ISSUE", ""));
                response.setVisaIssuingCountry(resultFromDatabase.getOrDefault("Issuing_country", ""));
                response.setNationality(resultFromDatabase.getOrDefault("Nationality", ""));
                response.setCountryOfBirth(resultFromDatabase.getOrDefault("Nationality", ""));
                response.setPaymentDetails(resultFromDatabase.getOrDefault("PAYMENT", ""));
                response.setReasonForCancellation(resultFromDatabase.getOrDefault("REASON_FOR_CANCELLING", ""));
            }

            return new Response(resultSet.size() == 0 ? 404 : 0, (resultSet.size() == 0 ? documentStatementToDisplay + " not found" : "Success"), resultSet.size() != 0 ? response : null);
        } catch (Exception e) {
            log.error("request|check _visa_status|" + input.getApplicationId() + "|" + input.getPassport() + e.getMessage());
            e.printStackTrace();
            return new Response(500, "Internal Server Error");
        }

    }

    private String translateVisaApplicationStatus(String applicationType, String status) {

        String[] in_progress_statuses;
        String[] valid;
        String[] invalid;

        if (applicationType.equalsIgnoreCase("visa")) {

            if (status.toLowerCase().contains("progress") || status.toLowerCase().contains("waiting")) {
                return "In PROGRESS";
            }

            in_progress_statuses = new String[]{"DEFERRED", "EPAYMENT", "WAITING_APPROVAL", "WAITING_PRE_PROCESSING", "WAITING_PROCESSING", "WAITING_REFERRAL"};
            valid = new String[]{"APPROVED", "COLLECTED", "COLECTED"};
            invalid = new String[]{"REJECTED", "USED", "EXPIRED", "CANCELLED"};
        } else {

            in_progress_statuses = new String[]{"APPROVAL_IN_PROGRESS", "????????_IN_PROGRESS", "WAITING_REFERRAL", "WAITING_APPROVAL_PRINCIPAL",
                    "WAITING_PROCESSING", "WAITING_PRE_PROCESSING", "DEFERRED", "DEFERED", "PROCESSING_IN_PROGRESS", "EPAYMENT", "EPAYMNET", "WAITING_NCIC_BOARD",
                    "WAITING_APPROVAL", "WAITING_PRE_PROCESSING", "WAITING_PROCESSING", "WAITING_REFERRAL"};

            valid = new String[]{"APPROVED", "COLLECTED", "COLECTED", "WAITING_COLLECTION", "WAITING_PERSONALIZATION"};
            invalid = new String[]{"REJECTED", "USED", "EXPIRED"};
        }

        if (Arrays.asList(in_progress_statuses).contains(status)) {
            return "IN PROGRESS";

        } else if (Arrays.asList(valid).contains(status)) {
            return "VALID";

        } else if (Arrays.asList(invalid).contains(status)) {
            return "INVALID";
        }

        return "STATUS UN KNOWN";

    }

    private String parseDate(Object date) {
        if (date != null && date.toString().equalsIgnoreCase("")) return "";

        //Date createdOn = new Date(Long.parseLong(date.toString()));
        return date == null ? "" : date.toString().split(" ")[0];

    }
}
