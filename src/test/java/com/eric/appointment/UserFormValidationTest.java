package com.eric.appointment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class UserFormValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testValidUserForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/customers/new/customer")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("user", "test") //How??????????
                .param("id", "1")
                .param("userName", "ValidUser")
                .param("password", "ValidPass")
                .param("matchingPassword", "ValidPass")
                .param("firstName", "John")
                .param("lastName", "Doe")
                .param("email", "john@example.com")
                .param("mobile", "081234567890")
                .param("street", "123 Main St")
                .param("postcode", "12345")
                .param("city", "Example City"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().hasNoErrors());
    }

    @Test
    public void testInvalidUserForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/customers/new/customer")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("user", "test")
                .param("id", "1")
                .param("userName", "ValidUser")
                .param("password", "ValidPass")
                .param("matchingPassword", "ValidPass2")
                .param("firstName", "John")
                .param("lastName", "Doe")
                .param("email", "invalid-email")
                .param("mobile", "0123")
                .param("street", "short")
                .param("postcode", "1234")
                .param("city", ""))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().hasNoErrors());
    }
}
