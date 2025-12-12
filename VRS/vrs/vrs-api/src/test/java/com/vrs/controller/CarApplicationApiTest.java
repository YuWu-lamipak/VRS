package com.vrs.controller;

import com.alibaba.fastjson.JSONObject;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

/**
 * 车辆预约 API 接口测试
 * 直接测试运行中的 API 服务（端口 8601）
 * 不启动新的 Spring Boot 实例
 */
public class CarApplicationApiTest {

    @Before
    public void setUp() {
        // 使用运行中的 API 服务
        RestAssured.baseURI = "http://172.23.80.255";
        RestAssured.port = 8601;
        RestAssured.basePath = "/api/application";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    /**
     * 测试制卡接口 - 成功场景
     */
    @Test
    public void testProCard_Success() {
        // 构造请求体
        JSONObject innerJson = new JSONObject();
        innerJson.put("CardID", "5FD9DBC4");
        innerJson.put("CardType", "1");
        innerJson.put("TruckNo1", "赣C12345");
        innerJson.put("TruckNo2", "");
        innerJson.put("isUpdate", "0");
        innerJson.put("TruckType", "0");

        JSONObject requestBody = new JSONObject();
        requestBody.put("json", innerJson.toJSONString());

        given()
            .contentType("application/json;charset=UTF-8")
            .body(requestBody.toJSONString())
        .when()
            .post("/proCard")
        .then()
            .statusCode(200)
            .body("Code", notNullValue())
            .body("Message", notNullValue());
    }

    /**
     * 测试制卡接口 - 未预约场景
     */
    @Test
    public void testProCard_NotReserved() {
        JSONObject innerJson = new JSONObject();
        innerJson.put("CardID", "TEST1234");
        innerJson.put("CardType", "1");
        innerJson.put("TruckNo1", "未预约车牌");
        innerJson.put("TruckNo2", "");
        innerJson.put("isUpdate", "0");
        innerJson.put("TruckType", "0");

        JSONObject requestBody = new JSONObject();
        requestBody.put("json", innerJson.toJSONString());

        given()
            .contentType("application/json;charset=UTF-8")
            .body(requestBody.toJSONString())
        .when()
            .post("/proCard")
        .then()
            .statusCode(200)
            .body("Code", equalTo("1"))
            .body("Message", containsString("未预约"));
    }

    /**
     * 测试门卫首页数据统计
     */
    @Test
    public void testGuardStatistics() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/guard/statistics")
        .then()
            .statusCode(200)
            .body("code", equalTo(200))
            .log().ifValidationFails();
    }

    /**
     * 测试原因列表查询
     */
    @Test
    public void testGetReasonList() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/getReasonList")
        .then()
            .statusCode(200)
            .body("code", equalTo(200))
            .body("data", notNullValue())
            .log().ifValidationFails();
    }

    /**
     * 测试废料列表查询
     */
    @Test
    public void testGetScrapList() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/getScrapList")
        .then()
            .statusCode(200)
            .body("code", equalTo(200))
            .body("data", notNullValue())
            .log().ifValidationFails();
    }

    /**
     * 测试供应商列表查询
     */
    @Test
    public void testGetSupplierList() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/getSupplierList")
        .then()
            .statusCode(200)
            .body("code", equalTo(200))
            .body("data", notNullValue())
            .log().ifValidationFails();
    }

    /**
     * 测试查看详情 - 无效ID
     */
    @Test
    public void testGetDetail_InvalidId() {
        given()
            .contentType(ContentType.JSON)
            .queryParam("applicationId", "INVALID_ID")
        .when()
            .get("/detail")
        .then()
            .statusCode(200)
            .log().ifValidationFails();
    }
}
