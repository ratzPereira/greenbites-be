package com.ratz.greenbites.controller;

import com.ratz.greenbites.entity.Role;
import com.ratz.greenbites.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    private final String registerUrl = "/api/v1/user/register";
    private final String loginUrl = "/api/v1/user/login";
    UUID uuid = UUID.randomUUID();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    public void setup() {

        if (roleRepository.findByName("ROLE_USER").isEmpty()) {
            Role userRole = new Role();
            userRole.setName("ROLE_USER");
            userRole.setPermission("ACCESS_USER");
            roleRepository.save(userRole);
        }

        if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
            Role adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            adminRole.setPermission("ACCESS_ADMIN");
            roleRepository.save(adminRole);
        }
    }

    @Test
    public void whenRegisterUser_thenSuccess() throws Exception {


        String userJson = "{\"email\":\"" + uuid + "@example.com\",\"password\":\"123456\",\"firstName\":\"testUser\",\"lastName\":\"testUser\"}";


        mockMvc.perform(post(registerUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.user.email").value(uuid + "@example.com"));
    }


    @Test
    public void whenRegisterUserWithoutEmail_thenFail() throws Exception {
        String userJson = "{\"password\":\"123456\",\"username\":\"testUser\"}";

        mockMvc.perform(post(registerUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void whenLoginUser_thenSuccess() throws Exception {

        whenRegisterUser_thenSuccess();

        String loginJson = "{\"email\":\"" + uuid + "@example.com\",\"password\":\"123456\"}";

        mockMvc.perform(post(loginUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.user.email").value(uuid + "@example.com"))
                .andExpect(jsonPath("$.data.access_token").isNotEmpty());
    }


    @Test
    public void whenLoginUserWithWrongPassword_thenFail() throws Exception {

        whenRegisterUser_thenSuccess();

        String loginJson = "{\"email\":\"test@example.com\",\"password\":\"wrongPassword\"}";

        mockMvc.perform(post(loginUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenRegisterUserWithoutPassword_thenFail() throws Exception {
        String userJsonWithoutPassword = "{\"email\":\"" + UUID.randomUUID() + "@example.com\",\"firstName\":\"testUser\",\"lastName\":\"testUser\"}";

        mockMvc.perform(post(registerUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJsonWithoutPassword))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenLoginUserWithNonExistentEmail_thenFail() throws Exception {
        String nonExistentLoginJson = "{\"email\":\"nonexistent" + uuid + "@example.com\",\"password\":\"123456\"}";

        mockMvc.perform(post(loginUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(nonExistentLoginJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenRegisterUserWithExistingEmail_thenFail() throws Exception {
        
        whenRegisterUser_thenSuccess();


        String duplicateUserJson = "{\"email\":\"" + uuid + "@example.com\",\"password\":\"123456\",\"firstName\":\"testUser\",\"lastName\":\"testUser\"}";

        mockMvc.perform(post(registerUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(duplicateUserJson))
                .andExpect(status().isBadRequest());
    }
}
