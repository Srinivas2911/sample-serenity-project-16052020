package com.lc.df.studentapp.testbase;

/*
Created by SP
*/

import io.restassured.RestAssured;
import org.junit.BeforeClass;

public class TestBase {


    @BeforeClass
    public static void init() {

        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/student";


    }

}
