package com.lc.df.studentapp.studentinfo;

/*
Created by SP
*/

import com.lc.df.studentapp.model.StudentPojo;
import com.lc.df.studentapp.testbase.TestBase;
import com.lc.df.studentapp.utils.TestUtils;
import io.restassured.http.ContentType;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Title;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.hasValue;
import static org.junit.Assert.assertThat;


// Running the test with Serenity Runner class for extracting reports
@RunWith(SerenityRunner.class)

// Fixing the order of the tests to run in ascending order
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class StudentCRUDTest extends TestBase {

    // variables defined
    static String firstName = "Roger" + TestUtils.getRandomValue();
    static String lastName = "Moore" + TestUtils.getRandomValue();
    static String email = "Roger" + TestUtils.getRandomValue() + "@gmail.com";
    static String programme = "Cinematography";

    static int studentId;

    //CRUD TEST - running the test cases with their titles.
    @Title("This test will create a new student record")
    @Test
    public void test001() {

        List<String> courses = new ArrayList<>();
        courses.add("Acting");
        courses.add("Photography");
        courses.add("Filming");

        StudentPojo studentPojo = new StudentPojo();
        studentPojo.setFirstName(firstName);
        studentPojo.setLastName(lastName);
        studentPojo.setEmail(email);
        studentPojo.setProgramme(programme);
        studentPojo.setCourses(courses);

        SerenityRest.rest()
                .given()
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .body(studentPojo)
                .post()
                .then()
                .log().all()
                .statusCode(201);
    }

    @Title("Verify if student was added to the record")
    @Test
    public void test002() {


        String str1 = "findAll{it.firstName=='";
        String str2 = "'}.get(0)";

        // mapping the String as Object  to find the value
        HashMap<String, Object> value = SerenityRest.rest()
                .given()
                .when()
                .get("/list")
                .then()
                .statusCode(200)
                .extract()
                .path(str1 + firstName + str2);
        assertThat(value, hasValue(firstName));
        studentId = (int) value.get("id");
        System.out.println("The student ID for the new student is : " + studentId);
    }

    @Title("Update the student info and verify the updated information")
    @Test
    public void test003() {

        List<String> courses = new ArrayList<>();
        courses.add("Acting");
        courses.add("Photography");
        courses.add("Filming");

        // making changes to the first name by adding _Amend to it
        firstName = firstName + "_Amend";
//        lastName = lastName+ "_Update";
//        email = "Change"+ email;

        StudentPojo studentPojo = new StudentPojo();
        studentPojo.setFirstName(firstName);
        studentPojo.setLastName(lastName);
        studentPojo.setEmail(email);
        studentPojo.setProgramme(programme);
        studentPojo.setCourses(courses);

        SerenityRest.rest()
                .given()
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .body(studentPojo)
                .put("/" + studentId)
                .then()
                .log().all()
                .statusCode(200);

        String str1 = "findAll{it.firstName=='";
        String str2 = "'}.get(0)";

        HashMap<String, Object> value = SerenityRest.rest()
                .given()
                .when()
                .get("/list")
                .then()
                .statusCode(200)
                .extract()
                .path(str1 + firstName + str2);
        assertThat(value, hasValue(firstName));
    }

    @Title("Delete the student record and verify if it is deleted")
    @Test
    public void test004() {

        SerenityRest.rest()
                .given()
                .when()
                .delete("/" + studentId)
                .then()
                .statusCode(204);

        SerenityRest.rest()
                .given()
                .when()
                .get("/" + studentId)
                .then()
                .statusCode(404);

    }
}

