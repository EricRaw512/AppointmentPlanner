package com.eric.appointment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
public class ChangePasswordValidationTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void validPasswordValidation() throws Exception{
         mockMvc.perform(MockMvcRequestBuilders.post("/customers/new/customer")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("curPassword", "qwerty123"));
    }
}
