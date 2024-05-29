package com.akanwkasa.dcic.nssf.Core_Proccessing;

import com.akanwkasa.dcic.nssf.database.DatabaseQueries;
import com.akanwkasa.dcic.nssf.helpers.CommonOperations;
import com.akanwkasa.dcic.nssf.helpers.Response;
import com.akanwkasa.dcic.nssf.requests_and_response.FingerImages;
import com.akanwkasa.dcic.nssf.requests_and_response.PermitDetails;
import com.akanwkasa.dcic.nssf.requests_and_response.biometric_verification.FingerPrintBiometricVerificationRequest;
import com.akanwkasa.dcic.nssf.requests_and_response.biometric_verification.FingerPrintBiometricVerificationResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.machinezoo.sourceafis.FingerprintImage;
import com.machinezoo.sourceafis.FingerprintMatcher;
import com.machinezoo.sourceafis.FingerprintTemplate;
import org.apache.commons.io.FileUtils;
import org.jnbis.api.Jnbis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * Author: Akankwasa Brian
 * Date: 1/18/2023
 * Time: 2:31 PM
 * Contacts : Email = donbrians@ymail.com, Mobile 00256778693362
 */

@Component(value = "FingerPrintMatchingApplicationLogic")
public class FingerPrintMatchingApplicationLogic {
    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;
    private DatabaseQueries db_queries;
    private Logger logger = LoggerFactory.getLogger("offsite_biometric_verification");


    FingerPrintMatchingApplicationLogic(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = new ObjectMapper();
        db_queries = new DatabaseQueries();
    }

    public Response validateFingerprint(FingerPrintBiometricVerificationRequest request) {


        try {
            String issuingCountry = request.getIssuingcountry().toUpperCase();
            String documentNumber = request.getDocumentNumber();
            String documentType = request.getDocumentType();
            String documentTypeName = request.getDocumentType();
            String VI_VISALON_PERSON_DATA = null;

            String issuingCountryId = null;
            String nationalityId = null;
            String issuingCountryName = null;

            //confirm the document type and change it to passport
            documentTypeName = documentType.toLowerCase().contains("passport") ? "passport" : "id";//id means any document which is not a passport


            if (documentTypeName.equalsIgnoreCase("passport")) {
                //start with validating the issuing country
                String issuingCountryQuery = "select * from CALIBURN.CA_COUNTRY where UKEY='" + issuingCountry + "'";
                List<Map<String, Object>> countryResultSet = jdbcTemplate.queryForList(issuingCountryQuery);

                if (countryResultSet.size() == 0) {
                    return new Response(404, String.format("Passport Issuing Country with UKEY %s was not Found", issuingCountry));
                }

                //get the issuing country id
                issuingCountryId = countryResultSet.get(0).get("ID").toString();


                //since we have passport and the issuing country
                VI_VISALON_PERSON_DATA = "select * from CALIBURN.VI_VISALON_PERSON_DATA where PASSPORT_NUMBER ='" + documentNumber + "' and  ISSUING_COUNTRY_ID='" + issuingCountryId + "'";


                //anything that is not a passport
            } else {
                String queryFroVisaApplication = "select * from VI_VISALON_APPLICATION where VISALON_APP_ID='" + documentNumber + "'";
                List<Map<String, Object>> visaApplicationResultSet = jdbcTemplate.queryForList(queryFroVisaApplication);

                if (visaApplicationResultSet.size() == 0) {
                    return new Response(404, String.format("Application ( %s ) with Number %s was not found", documentTypeName, documentNumber));
                }


                //get the visalon _person _data from the visalon application result
                String visaLonPersonDataId = visaApplicationResultSet.get(0).get("VISALON_PERSON_DATA_ID").toString();
                VI_VISALON_PERSON_DATA = "select * from CALIBURN.VI_VISALON_PERSON_DATA where ID ='" + visaLonPersonDataId + "'";

            }

            //step one, search for the passport in that database
            List<Map<String, Object>> resultSet = jdbcTemplate.queryForList(VI_VISALON_PERSON_DATA);


            //if the person has been found
            if (resultSet.size() > 0) {
                Map personData = resultSet.get(0);
                String firstName = personData.get("first_name").toString();
                String lastName = personData.get("last_name").toString();
                String placeOfBirth = personData.get("place_of_birth").toString();
                String email = personData.get("email").toString();
                String passportPlaceOfIssue = personData.get("PLACE_OF_ISSUE").toString();
                String fingerType = request.getFingers().get(0).getFingerType();
                String suppliedFingerPrint = request.getFingers().get(0).getFingerPrint();
                nationalityId = personData.get("NATIONALITY_ID").toString();


                //document issuing country id -  fr non passports
                issuingCountryId = resultSet.get(0).get("ISSUING_COUNTRY_ID").toString();


                String nationality = null;


                //visa application id
                String visaApplicationQuery = "select * from CALIBURN.VI_VISALON_APPLICATION where VISALON_PERSON_DATA_ID='" + personData.get("ID") + "'  order by RESOLUTION_DATE desc";
                List<Map<String, Object>> VisaApplicationResultSet = jdbcTemplate.queryForList(visaApplicationQuery);


                //if the person is found with some visa applications
                if (VisaApplicationResultSet.size() > 0) {

                    //get active permit
                    PermitDetails activePermit = getActivePermit(personData.get("ID").toString());


                    if (activePermit != null) {
                        //get the active visa application which was approved
                        //visa application id
                        String permitApplication = "select * from caliburn.VI_VISALON_APPLICATION where VISALON_APP_ID='" + activePermit.getPermitNumber() + "'";
                        List<Map<String, Object>> permitApplicationResultSetList = jdbcTemplate.queryForList(permitApplication);
                        Map permitApplicationResultSet = permitApplicationResultSetList.get(0);

                        //we get the person record
                        Map pr_person_record_result = jdbcTemplate.queryForMap("select *  from caliburn.PR_PERSON_RECORD where ID='" + permitApplicationResultSet.get("PERSON_RECORD_ID") + "'");


                        //hands record id
                        String photoId = pr_person_record_result.get("PHOTO_ID").toString();


                        //get the photo id from select * from PR_PHOTO_RECORD where id='252127402'
                        List<Map<String, Object>> photoRecord = jdbcTemplate.queryForList("select * from PR_PHOTO_RECORD where ID='" + photoId + "'");
                        String photoRecordImageId = photoRecord.get(0).get("BINARY_ID").toString();


                        List<Map<String, Object>> photoMapList = jdbcTemplate.queryForList("select *  from caliburn.CA_BINARY where ID='" + photoRecordImageId + "'");
                        Object photo = photoMapList.size() > 0 ? photoMapList.get(0).get("PAYLOAD") : null;

                        String personImageFileName = request.getTransactionId() + "_person_image.JPEG";
                        byte[] personPhotoBytes = DatatypeConverter.parseBase64Binary(new CommonOperations().convertBlobToBase64String(photo));
                        File inputFile = new File(personImageFileName);

                        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(inputFile))) {
                            out.write(personPhotoBytes);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        byte[] Person_image_from_disk_encoded = FileUtils.readFileToByteArray(inputFile);
                        String decodedPersonImage = Base64.getEncoder().encodeToString(Person_image_from_disk_encoded);
                        inputFile.delete();


                        //get the list of finger prints from the fp_finger_prints table
                        List<Map<String, Object>> finger_prints = jdbcTemplate.queryForList("select fp.* from caliburn.FP_FINGERPRINT fp join caliburn.PR_R_PERSR_HANDS prh on prh.FINGERPRINT_ID=fp.id where prh.HANDS_RECORD_ID='" + pr_person_record_result.get("HANDS_RECORD_ID") + "'");
                        List<FingerImages> fingerImages = new ArrayList<>();

                        if (finger_prints.size() != 0) {
                            for (Map finger_print : finger_prints) {

                                if (finger_print.get("BINARY_ID") != null) {
                                    //select each individual image from the ca-binary table and match it to the finger print
                                    Map ca_binary_finger_blob = jdbcTemplate.queryForMap("select *  from caliburn.CA_BINARY where ID='" + finger_print.get("BINARY_ID") + "'");

                                    FingerImages images = new FingerImages();
                                    images.setFingerImage(new CommonOperations().convertBlobToBase64String(ca_binary_finger_blob.get("PAYLOAD")));
                                    images.setFingerType(finger_print.get("FINGER_TYPE").toString());
                                    images.setFingerPrintQuality(Integer.parseInt(finger_print.get("QUALITY").toString()));

                                    fingerImages.add(images);
                                }
                            }


                            //get the country name, remember if the visa or any travel document, then you must pick the country id from visa application
                            if (documentType.toLowerCase().contains("passport")) {
                                List<Map<String, Object>> country = jdbcTemplate.queryForList("select * from caliburn.CA_MO_TRANSLATED_NAME where PARENT_ID='" + issuingCountryId + "'");
                                issuingCountryName = country.get(0).get("NAME").toString();
                            } else {
                                issuingCountryName = "Uganda ( For Non Passport Documents )";
                            }


                            //get the nationality of the individual
                            List<Map<String, Object>> nationalityName = jdbcTemplate.queryForList("select * from caliburn.CA_MO_TRANSLATED_NAME where PARENT_ID='" + nationalityId + "'");
                            nationality = nationalityName.get(0).get("NAME").toString();


                            //we are doing are one to one search for the finger prints to save time
                            FingerImages fingerPrintToMatch = fingerImages.stream().filter(finger -> finger.getFingerType().equalsIgnoreCase(fingerType)).findAny().orElse(null);

                            if (fingerPrintToMatch != null) {
                                byte[] fbbytes = DatatypeConverter.parseBase64Binary(fingerPrintToMatch.getFingerImage());
                                File fingerPrintConvertedToPng = Jnbis.wsq().decode(fbbytes).toPng().asFile(request.getTransactionId() + ".png");

                                byte[] fingerPrintFromDatabaseBytes = FileUtils.readFileToByteArray(fingerPrintConvertedToPng);
                                String decodedPNGImage = Base64.getEncoder().encodeToString(fingerPrintFromDatabaseBytes);


                                //after looping all the images, now Match the finger prints with AFIS
                                byte[] fingerPrintFromDeviceBytes = DatatypeConverter.parseBase64Binary(suppliedFingerPrint);
//                                byte[] fingerPrintFromDatabaseBytes =DatatypeConverter.parseBase64Binary(fingerPrintToMatch.getFingerImage());
                                //byte[] fingerPrintFromDatabaseBytes = DatatypeConverter.parseBase64Binary(decodedPNGImage);


                                FingerprintTemplate fingerPrintFromDevice = new FingerprintTemplate(new FingerprintImage(fingerPrintFromDeviceBytes));
                                //FingerprintTemplate fingerPrintFromDatabase = new FingerprintTemplate(new FingerprintImage(fingerPrintFromDatabaseBytes));
                                FingerprintTemplate fingerPrintFromDatabase = new FingerprintTemplate(new FingerprintImage(fingerPrintFromDatabaseBytes));

                                FingerprintMatcher matcher = new FingerprintMatcher(fingerPrintFromDevice);
                                double similarity = matcher.match(fingerPrintFromDatabase);

                                //delete the file afterwards
                                if (fingerPrintConvertedToPng.delete()) {
                                    logger.info("{} | {} | {} | info | {} | 200 | File Delete Properly ", request.getTransactionId(), documentNumber, request.getIssuingcountry());

                                } else {
                                    logger.error("{} | {} | {} | info | {} | 500 | File  not Delete Properly ", request.getTransactionId(), documentNumber, request.getIssuingcountry());

                                }

                                //################now start the finger print matching proccess
                                FingerPrintBiometricVerificationResponse applicationResult = new FingerPrintBiometricVerificationResponse();
                                applicationResult.setApplicationNumber(activePermit.getPermitNumber().toString());
                                applicationResult.setApplicationStatus(translateVisaApplicationStatus(activePermit.getApplicationType().toString(), activePermit.getStatus().toString()));
                                applicationResult.setVisaExpirationDate(activePermit.getPermitDateOfExpiry().toString());

                                applicationResult.setPassportNumber(documentNumber);

                                //get these from peron_details
                                applicationResult.setNationality(nationality);
                                applicationResult.setDocumentPlaceOfIssue(issuingCountryName);


                                applicationResult.setDateOfBirth(activePermit.getBirthDay().toString());
                                applicationResult.setApplicationtype(activePermit.getApplicationType().toString());
                                applicationResult.setPhoto(decodedPersonImage);

                                applicationResult.setFullName(lastName + " " + firstName);
                                applicationResult.setPlaceOfBirth(placeOfBirth);
                                applicationResult.setEmail(email);
                                applicationResult.setMatchingScore(String.valueOf(similarity));

                                //return copies of the Finger prints for visual inspection
                                applicationResult.setProvidedFingerPrint(suppliedFingerPrint);
                                applicationResult.setFingerPrintFromDatabase(decodedPNGImage);
                                applicationResult.setFingerType(fingerType);

                                //the response after comparison
                                String similarityResponse = similarity > 50 ? "Match Found" : "Finger prints do no match!";
                                String similarityResponseDescription = similarity > 50 ? "Exact Match Found" : "Finger print supplied from device does not match the one in our system!";
                                int similarityResponseCode = similarity > 50 ? 0 : 400;
                                applicationResult.setMessage(similarityResponseDescription);


                                return new Response(similarityResponseCode, similarityResponse, applicationResult);
                            } else {
                                return new Response(404, String.format("No Finger Print matching %s was found in the database for comparison", fingerType));

                            }

                        } else {
                            return new Response(404, "No Finger Prints were found in the database for comparison");
                        }
                    } else {
                        return new Response(404, "Sorry, No active Permit/Visa was found for the supplied Document number : " + documentNumber);

                    }

                } else {
                    return new Response(404, "No Visa Applications were found");
                }


            } else {
                return new Response(404, String.format("Person with Document %s issued by %s was not found", documentNumber.toUpperCase(), issuingCountry));

            }

        } catch (Exception e) {
            e.printStackTrace();
            return new Response(500, e.getMessage());

        }

    }

    public Response validateDocumentNumber(FingerPrintBiometricVerificationRequest request) throws IOException {


        try {
            String issuingCountry = request.getIssuingcountry().toUpperCase();
            String documentNumber = request.getDocumentNumber();
            String documentType = request.getDocumentType();
            String documentTypeName = request.getDocumentType();
            String VI_VISALON_PERSON_DATA = null;

            String issuingCountryId = null;
            String nationalityId = null;
            String issuingCountryName = null;

            //confirm the document type and change it to passport
            documentTypeName = documentType.toLowerCase().contains("passport") ? "passport" : "id";//id means any document which is not a passport


            if (documentTypeName.equalsIgnoreCase("passport")) {
                //start with validating the issuing country
                String issuingCountryQuery = "select * from CALIBURN.CA_COUNTRY where UKEY='" + issuingCountry + "'";
                List<Map<String, Object>> countryResultSet = jdbcTemplate.queryForList(issuingCountryQuery);

                if (countryResultSet.size() == 0) {
                    return new Response(404, String.format("Passport Issuing Country with UKEY %s was not Found", issuingCountry));
                }

                //get the issuing country id
                issuingCountryId = countryResultSet.get(0).get("ID").toString();


                //since we have passport and the issuing country
                VI_VISALON_PERSON_DATA = "select * from CALIBURN.VI_VISALON_PERSON_DATA where PASSPORT_NUMBER ='" + documentNumber + "' and  ISSUING_COUNTRY_ID='" + issuingCountryId + "'";


                //anything that is not a passport
            } else {
                String queryFroVisaApplication = "select * from VI_VISALON_APPLICATION where VISALON_APP_ID='" + documentNumber + "'";
                List<Map<String, Object>> visaApplicationResultSet = jdbcTemplate.queryForList(queryFroVisaApplication);

                if (visaApplicationResultSet.size() == 0) {
                    return new Response(404, String.format("Application ( %s ) with Number %s was not found", documentTypeName, documentNumber));
                }


                //get the visalon _person _data from the visalon application result
                String visaLonPersonDataId = visaApplicationResultSet.get(0).get("VISALON_PERSON_DATA_ID").toString();
                VI_VISALON_PERSON_DATA = "select * from CALIBURN.VI_VISALON_PERSON_DATA where ID ='" + visaLonPersonDataId + "'";

            }

            //step one, search for the passport in that database
            List<Map<String, Object>> resultSet = jdbcTemplate.queryForList(VI_VISALON_PERSON_DATA);


            //if the person has been found
            if (resultSet.size() > 0) {
                Map personData = resultSet.get(0);
                String firstName = personData.get("first_name").toString();
                String lastName = personData.get("last_name").toString();
                String placeOfBirth = personData.get("place_of_birth").toString();
                String email = personData.get("email").toString();
                String passportPlaceOfIssue = personData.get("PLACE_OF_ISSUE").toString();
                nationalityId = personData.get("NATIONALITY_ID").toString();


                //document issuing country id -  fr non passports
                issuingCountryId = resultSet.get(0).get("ISSUING_COUNTRY_ID").toString();


                String nationality = null;


                //visa application id
                String visaApplicationQuery = "select * from CALIBURN.VI_VISALON_APPLICATION where VISALON_PERSON_DATA_ID='" + personData.get("ID") + "'  order by RESOLUTION_DATE desc";
                List<Map<String, Object>> VisaApplicationResultSet = jdbcTemplate.queryForList(visaApplicationQuery);


                //if the person is found with some visa applications
                if (VisaApplicationResultSet.size() > 0) {

                    //get active permit
                    PermitDetails activePermit = getActivePermit(personData.get("ID").toString());


                    if (activePermit != null) {
                        //get the active visa application which was approved
                        //visa application id
                        String permitApplication = "select * from caliburn.VI_VISALON_APPLICATION where VISALON_APP_ID='" + activePermit.getPermitNumber() + "'";
                        List<Map<String, Object>> permitApplicationResultSetList = jdbcTemplate.queryForList(permitApplication);
                        Map permitApplicationResultSet = permitApplicationResultSetList.get(0);

                        //we get the person record
                        Map pr_person_record_result = jdbcTemplate.queryForMap("select *  from caliburn.PR_PERSON_RECORD where ID='" + permitApplicationResultSet.get("PERSON_RECORD_ID") + "'");


                        //hands record id
                        String photoId = pr_person_record_result.get("PHOTO_ID").toString();


                        //get the photo id from select * from PR_PHOTO_RECORD where id='252127402'
                        List<Map<String, Object>> photoRecord = jdbcTemplate.queryForList("select * from PR_PHOTO_RECORD where ID='" + photoId + "'");
                        String photoRecordImageId = photoRecord.get(0).get("BINARY_ID").toString();


                        List<Map<String, Object>> photoMapList = jdbcTemplate.queryForList("select *  from caliburn.CA_BINARY where ID='" + photoRecordImageId + "'");
                        Object photo = photoMapList.size() > 0 ? photoMapList.get(0).get("PAYLOAD") : null;

                        String personImageFileName = request.getTransactionId() + "_person_image.JPEG";
                        byte[] personPhotoBytes = DatatypeConverter.parseBase64Binary(new CommonOperations().convertBlobToBase64String(photo));
                        File inputFile = new File(personImageFileName);

                        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(inputFile))) {
                            out.write(personPhotoBytes);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        byte[] Person_image_from_disk_encoded = FileUtils.readFileToByteArray(inputFile);
                        String decodedPersonImage = Base64.getEncoder().encodeToString(Person_image_from_disk_encoded);
                        inputFile.delete();


                        //get the country name, remember if the visa or any travel document, then you must pick the country id from visa application
                        if (documentType.toLowerCase().contains("passport")) {
                            List<Map<String, Object>> country = jdbcTemplate.queryForList("select * from caliburn.CA_MO_TRANSLATED_NAME where PARENT_ID='" + issuingCountryId + "'");
                            issuingCountryName = country.get(0).get("NAME").toString();
                        } else {
                            issuingCountryName = "Uganda ( For Non Passport Documents )";
                        }


                        //get the nationality of the individual
                        List<Map<String, Object>> nationalityName = jdbcTemplate.queryForList("select * from caliburn.CA_MO_TRANSLATED_NAME where PARENT_ID='" + nationalityId + "'");
                        nationality = nationalityName.get(0).get("NAME").toString();


                        //################now start the finger print matching proccess
                        FingerPrintBiometricVerificationResponse applicationResult = new FingerPrintBiometricVerificationResponse();
                        applicationResult.setApplicationNumber(activePermit.getPermitNumber().toString());
                        applicationResult.setApplicationStatus(translateVisaApplicationStatus(activePermit.getApplicationType().toString(), activePermit.getStatus().toString()));
                        applicationResult.setVisaExpirationDate(activePermit.getPermitDateOfExpiry().toString());

                        applicationResult.setPassportNumber(documentNumber);

                        //get these from peron_details
                        applicationResult.setNationality(nationality);
                        applicationResult.setDocumentPlaceOfIssue(issuingCountryName);


                        applicationResult.setDateOfBirth(activePermit.getBirthDay().toString());
                        applicationResult.setApplicationtype(activePermit.getApplicationType().toString());
                        applicationResult.setPhoto(decodedPersonImage);

                        applicationResult.setFullName(lastName + " " + firstName);
                        applicationResult.setPlaceOfBirth(placeOfBirth);
                        applicationResult.setEmail(email);

                        //return copies of the Finger prints for visual inspection
                        applicationResult.setProvidedFingerPrint(null);
                        applicationResult.setFingerType(null);

                        //the response after comparison
                        int similarity = 100;
                        String similarityResponse = similarity > 50 ? "Match Found" : "Person Details not found!";
                        int similarityResponseCode = similarity > 50 ? 0 : 400;
                        applicationResult.setMessage(similarityResponse);


                        return new Response(similarityResponseCode, similarityResponse, applicationResult);


                    } else {
                        return new Response(404, "Sorry, No active Permit/Visa was found for the supplied Document number : " + documentNumber);

                    }

                } else {
                    return new Response(404, "No Visa Applications were found");
                }


            } else {
                return new Response(404, String.format("Person with Document %s issued by %s was not found", documentNumber.toUpperCase(), issuingCountry));

            }

        } catch (Exception e) {
            e.printStackTrace();
            return new Response(500, e.getMessage());

        }

    }

    private PermitDetails getActivePermit(String VISALON_PERSON_DATA_ID) throws ParseException {

        //get the list of permits
        String previous_permits_query = "select viap.ID,viap.VISALON_APP_ID, viap.APPLICATION_STATUS,viap.VISALON_APP_TYPE,viap.RESOLUTION_DATE,(select vd1.value from CALIBURN.vi_descriptions VD1 where VD1.KEY=viap.CATEGORY) as CATEGORY," +
                "(select vd2.value from CALIBURN.vi_descriptions VD2 where VD2.KEY=viap.SUBCATEGORY) as SUBCATEGORY, viap.CREATION_DATE, viap.DATE_OF_EXPIRY," +
                "info.BIRTHDAY,info.FIRST_NAME, info.LAST_NAME,info.EMAIL,info.PASSPORT_NUMBER, info.PLACE_OF_ISSUE," +
                "(select country.UKEY from caliburn.CA_COUNTRY country  where country.ID=info.ISSUING_COUNTRY_ID) as passport_issuing_country," +
                "(select country.UKEY from caliburn.CA_COUNTRY country  where country.ID=info.NATIONALITY_ID) as nationality_id," +
                "(select CONCAT(concat(pay.AMOUNT, pay.CURRENCY), concat(pay.PAY_DATE, pay.PAYMENT_METHOD)) from caliburn.VI_PAYMENT pay where pay.VISALON_APPLICATION_ID=viap.ID fetch  first 1 row only) as payment" +
                " from caliburn.VI_VISALON_APPLICATION viap  left join  caliburn.VI_VISALON_PERSON_DATA info on info.ID=viap.visalon_person_data_id where  viap.VISALON_PERSON_DATA_ID='" + VISALON_PERSON_DATA_ID + "' order by viap.RESOLUTION_DATE desc";


        List<Map<String, Object>> previous_permits = jdbcTemplate.queryForList(previous_permits_query);

        List<PermitDetails> permitDetailsList = new ArrayList<>();

        for (Map permit : previous_permits) {
            //select each individual permits from the ca-binary table and match it to the finger print
            PermitDetails permitDetails = new PermitDetails();
            permitDetails.setApplicationType(permit.get("VISALON_APP_TYPE"));
            permitDetails.setCategory(permit.get("CATEGORY"));
            permitDetails.setSubcategory(permit.get("SUBCATEGORY"));
            permitDetails.setStatus(permit.get("APPLICATION_STATUS"));

            permitDetails.setOrganizationCode(permit.get("nationality_id"));
            permitDetails.setIssuingCountry(permit.get("passport_issuing_country"));

            permitDetails.setPermitDateOfExpiry(truncateDate(permit.get("DATE_OF_EXPIRY")));
            permitDetails.setPermitDateOfIssue(truncateDate(permit.get("CREATION_DATE")));

            permitDetails.setPermitNumber(permit.get("VISALON_APP_ID"));
            permitDetails.setPermitType(permit.get("VISALON_APP_TYPE"));
            permitDetails.setBirthDay(permit.get("BIRTHDAY"));
            permitDetailsList.add(permitDetails);
        }


        List<PermitDetails> returnedPermit = new ArrayList<>();

        permitDetailsList.forEach(permit -> {
            if (translateVisaApplicationStatus(permit.getApplicationType().toString(), permit.getStatus().toString()).equalsIgnoreCase("VALID")) {
                returnedPermit.add(permit);
            }
        });

        return returnedPermit.size() > 0 ? returnedPermit.get(0) : null;
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

    public Response findPersonByFingerPrintOnly(FingerPrintBiometricVerificationRequest request) {
        Response response = new Response(0, "Finger successfuly Matched");
        try {

            //select the fingerprints whose type is the one specified in the payload
            //i.e. if it is left, select all fingerprints which are left
            if (request.getFingers().size() < 1) {
                response.setCode(400);
                response.setMessage("List of Finger prints cannot be empty");
                return response;
            }
            String fingerType = request.getFingers().get(0).getFingerType().toUpperCase();
            List<Map<String, Object>> fingerprintsMatchingFingerType = jdbcTemplate.queryForList("select fp.FINGER_TYPE as finger_type, ca.PAYLOAD as finger_print , fp.BINARY_ID as fp_binary_id, fp.Id as FP_ENTRY_ID , ca.ID as binary_id from FP_FINGERPRINT fp join CA_BINARY ca on fp.BINARY_ID=ca.ID where fp.FINGER_TYPE='" + fingerType + "'");

            //if there is no finger print matching the specified finger type
            if (fingerprintsMatchingFingerType.size() < 1) {
                response.setCode(404);
                response.setMessage(String.format("No Fingerprint matching specified Finger type ( %s ) was found.", fingerType));
                return response;
            }

            //now lets fo the matching
            //the fingerprint from the request is static
            //after looping all the images, now Match the finger prints with AFIS
            byte[] fingerPrintFromRequestPayload = DatatypeConverter.parseBase64Binary(request.getFingers().get(0).getFingerPrint());
            FingerprintTemplate fingerPrintFromDevice = new FingerprintTemplate(new FingerprintImage(fingerPrintFromRequestPayload));


            String FP_ENTRY_ID = "";
            String fingerPrintMaxRelationResultID = "";
            String FPfingerPrintMaxRelationResultID = "";
            double fingerPrintMaxRelationResult = 0;


            //now instatiate the loop
            for (Map<String, Object> fingerPrintFromDatabase : fingerprintsMatchingFingerType) {
                String decodedStringFromDataBase = new CommonOperations().convertBlobToBase64String(fingerPrintFromDatabase.get("FINGER_PRINT"));
                byte[] bytesC = DatatypeConverter.parseBase64Binary(decodedStringFromDataBase);

                File fingerPrintFromDatabaseConvertedToPng = Jnbis.wsq().decode(bytesC).toPng().asFile(request.getTransactionId() + "_FROM_DB.png");
                byte[] fingerPrintFromDatabaseBytes = new byte[0];

                try {
                    fingerPrintFromDatabaseBytes = FileUtils.readFileToByteArray(fingerPrintFromDatabaseConvertedToPng);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //String decodedPNGImage = Base64.getEncoder().encodeToString(fingerPrintFromDatabaseBytes);
                FingerprintTemplate _fingerPrintFromDatabase = new FingerprintTemplate(new FingerprintImage(fingerPrintFromDatabaseBytes));

                FingerprintMatcher matcher = new FingerprintMatcher(fingerPrintFromDevice);
                double similarity = matcher.match(_fingerPrintFromDatabase);


                System.out.println(String.format("FP ENTRY | %s | BINARY ID | %s | FP BINARY ID | %s | SIMILARITY %s", fingerPrintFromDatabase.get("FP_ENTRY_ID"), fingerPrintFromDatabase.get("BINARY_ID"), fingerPrintFromDatabase.get("fp_binary_id"), String.valueOf(similarity)));


                if (similarity > fingerPrintMaxRelationResult) {
                    fingerPrintMaxRelationResult = similarity;
                    fingerPrintMaxRelationResultID = fingerPrintFromDatabase.get("BINARY_ID").toString();
                    FP_ENTRY_ID = fingerPrintFromDatabase.get("FP_ENTRY_ID").toString();
                    FPfingerPrintMaxRelationResultID = fingerPrintFromDatabase.get("fp_binary_id").toString();

                }


                //delete the file afterwards
                //remember to add the logging to confirm
                fingerPrintFromDatabaseConvertedToPng.delete();
            }

            //now we have the Matching finger print with the highest score of similarity so we can look it up by reverse engineering
            System.out.println(String.format("\n\nFP ENTRY | %s | BINARY ID | %s | FP BINARY ID | %s | SIMILARITY %s", FP_ENTRY_ID, fingerPrintMaxRelationResultID, FPfingerPrintMaxRelationResultID, fingerPrintMaxRelationResult));


            //now reverse the query to get the person hands records
            List<Map<String, Object>> personRecordMatchedList = jdbcTemplate.queryForList("select pr.* from caliburn.FP_FINGERPRINT fp join caliburn.PR_R_PERSR_HANDS prh on prh.FINGERPRINT_ID=fp.id where fp.id='" + fingerPrintMaxRelationResultID + "'");


            //if there is no finger print matching the specified finger type
            if (fingerprintsMatchingFingerType.size() < 1) {
                response.setCode(404);
                response.setMessage(String.format("Person Record information was not found matching Finger type ( %s ) was found.", fingerType));
                return response;
            }

            Map<String, Object> personRecordMatched=personRecordMatchedList.get(0);
            String permitApplication = "select * from caliburn.VI_VISALON_APPLICATION where VISALON_APP_ID=' /*+ activePermit.getPermitNumber() +*/ '";
            List<Map<String, Object>> permitApplicationResultSetList = jdbcTemplate.queryForList(permitApplication);
            Map permitApplicationResultSet = permitApplicationResultSetList.get(0);


        } catch (Exception e) {
            e.printStackTrace();
            response.setCode(500);
            response.setMessage(e.getMessage());
        }

        return response;
    }

    private void out(String title, Object input) {
        System.out.println(title + "\n\n" + input);
    }
}
