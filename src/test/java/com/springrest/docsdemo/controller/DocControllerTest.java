package com.springrest.docsdemo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springrest.docsdemo.doc.User;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.HashMap;
import java.util.Map;

import static com.springrest.docsdemo.ApiDocumentUtils.getDocumentRequest;
import static com.springrest.docsdemo.ApiDocumentUtils.getDocumentResponse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 스프링 기본 컨텍스트를 사용 하기 위한 어노테이션
 */
@RunWith(SpringRunner.class)

/**
 * 기본적인 MockMvc 기능을 주입
 */
@WebMvcTest(DocController.class)

/**
 * RestDocs
 * .andDo(document(...)) 를 사용 하기 위한 어노테이션
 */
@AutoConfigureRestDocs

/**
 * 테스트 메서드를 알파벳 순서대로 사용한다.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DocControllerTest {

    @Rule
    private JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void A_addUser() throws Exception {
        User user = new User();
        user.setUsername("jihoon");
        user.setAge(30);
        Map<String, String> map = new HashMap<>();
        map.put("username", "jihoon");
        map.put("age", "30");
        ResultActions result =
                this.mockMvc.perform(
                        post("/user")
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .content(this.objectMapper.writeValueAsString(map))
                                .accept(MediaType.APPLICATION_JSON_UTF8)
                )
                        .andExpect(status().isOk());

        result
                .andDo(
                        document("save-user",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                requestFields(
                                        fieldWithPath("username").description("any"),
                                        fieldWithPath("age").description("나이")
                                ),
                                responseFields(
                                        fieldWithPath("idx").description("생성 된 고유 아이디"),
                                        fieldWithPath("username").description("유저이름"),
                                        fieldWithPath("age").description("나이"),
                                        fieldWithPath("createDate").description("생성 날짜"),
                                        fieldWithPath("updateDate").description("수정 날짜")
                                )
                        )
                );
    }

    @Test
    public void B_getUser() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(
                get("/user/{username}", "jihoon")
                        .accept(MediaType.APPLICATION_JSON_UTF8)
        );

        resultActions
                .andExpect(status().is(200))
                .andDo(
                        document("get-user",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                pathParameters(
                                        parameterWithName("username").description("사용자 이름")
                                ),
                                responseFields(
                                        fieldWithPath("idx").description("유저의 고유 아이디"),
                                        fieldWithPath("username").description("유저 이름"),
                                        fieldWithPath("age").type(JsonFieldType.NUMBER).description("나이"),
                                        fieldWithPath("createDate").type(JsonFieldType.STRING).description("생성 날짜"),
                                        fieldWithPath("updateDate").description("수정 날짜")
                                )
                        )
                );
    }

    @Test
    public void C_getUsers() throws Exception {
        ResultActions result = this.mockMvc.perform(
                get("/users")
        ).andExpect(status().isOk());

        result
                .andDo(
                        document("get-users",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                responseFields(
                                        fieldWithPath("[].idx").description("유저의 고유 아이디"),
                                        fieldWithPath("[].username").description("유저 이름"),
                                        fieldWithPath("[].age").description("나이"),
                                        fieldWithPath("[].createDate").description("생성 날짜"),
                                        fieldWithPath("[].updateDate").description("수정 날짜")
                                )
                        )
                );
    }

    @Test
    public void D_updateUser() throws Exception {
        ResultActions result = this.mockMvc.perform(
                put("/user/{idx}", 1L)
                .param("updateName", "홍길동")
                .param("updateAge", "20")
        ).andExpect(status().isOk());

        result
                .andDo(
                        document(
                                "update-user",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                pathParameters(
                                        parameterWithName("idx").description("변경 할 유저의 고유 ID")
                                ),
                                requestParameters(
                                        parameterWithName("updateName").description("유저 이름"),
                                        parameterWithName("updateAge").description("나이")
                                ),
                                responseFields(
                                        fieldWithPath("idx").description("유저의 고유 아이디"),
                                        fieldWithPath("username").description("유저 이름"),
                                        fieldWithPath("age").description("나이"),
                                        fieldWithPath("createDate").description("생성 날짜"),
                                        fieldWithPath("updateDate").description("수정 날짜")
                                )
                        )
                );
    }
}