package com.ratz.greenbites.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.ratz.greenbites.entity.Post;
import com.ratz.greenbites.entity.Profile;
import com.ratz.greenbites.entity.Role;
import com.ratz.greenbites.entity.User;
import com.ratz.greenbites.repository.PostRepository;
import com.ratz.greenbites.repository.ProfileRepository;
import com.ratz.greenbites.repository.RoleRepository;
import com.ratz.greenbites.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PostControllerIntegrationTest {

    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private ObjectMapper objectMapper;
    private Post post;
    private User user;
    private String accessToken;

    @BeforeAll
    public void setup() throws Exception {

        insertRoles();

        user = createUser();
        post = createPost();

        accessToken = obtainAccessToken(user.getEmail(), "password");
    }


    @Test
    @Order(1)
    @DisplayName("When create post then success")
    public void whenCreatePost_thenSuccess() throws Exception {

        String newPostJson = "{\"content\":\"Test content\"}";
        MvcResult result = mockMvc.perform(post("/api/v1/posts")
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newPostJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value("Test content"))
                .andReturn();

        String response = result.getResponse().getContentAsString();
        Long createdPostId = JsonPath.parse(response).read("$.id", Long.class);

        assertTrue(postRepository.findById(createdPostId).isPresent());
        assertEquals("Test content", postRepository.findById(createdPostId).get().getContent());
    }

    @Test
    @Order(2)
    @DisplayName("When get post by id then success")
    public void whenGetPostById_thenSuccess() throws Exception {
        mockMvc.perform(get("/api/v1/posts/" + post.getId())
                        .header("Authorization", accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(post.getContent()));


        assertTrue(postRepository.findById(post.getId()).isPresent());
        assertEquals("content", postRepository.findById(post.getId()).get().getContent());
    }

    @Test
    @Order(3)
    @DisplayName("When update post then success")
    public void whenUpdatePost_thenSuccess() throws Exception {
        String updatedPostJson = "{\"content\":\"Updated content\"}";
        mockMvc.perform(put("/api/v1/posts/" + post.getId())
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedPostJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Updated content"));

        assertEquals("Updated content", postRepository.findById(post.getId()).get().getContent());
    }

    @Test
    @Order(4)
    @Transactional
    @DisplayName("When like post then success")
    public void whenLikePost_thenSuccess() throws Exception {
        mockMvc.perform(post("/api/v1/posts/" + post.getId() + "/like")
                        .header("Authorization", accessToken))
                .andExpect(status().isOk());

        Post likedPost = postRepository.findById(post.getId()).orElseThrow(() -> new AssertionError("Post not found"));
        assertTrue(likedPost.getLikedBy().stream().anyMatch(likingUser -> likingUser.getId().equals(user.getId())));
    }

    @Test
    @Order(5)
    @Transactional
    @DisplayName("When dislike post then success")
    public void whenDislikePost_thenSuccess() throws Exception {

        mockMvc.perform(post("/api/v1/posts/" + post.getId() + "/like")
                        .header("Authorization", accessToken))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/posts/" + post.getId() + "/like")
                        .header("Authorization", accessToken))
                .andExpect(status().isOk());

        Post dislikedPost = postRepository.findById(post.getId()).orElseThrow(() -> new AssertionError("Post not found"));
        assertFalse(dislikedPost.getLikedBy().stream().anyMatch(likingUser -> likingUser.getId().equals(user.getId())));
    }

    @Test
    @Order(6)
    @DisplayName("When delete post then success")
    public void whenDeletePost_thenSuccess() throws Exception {

        mockMvc.perform(delete("/api/v1/posts/" + post.getId())
                        .header("Authorization", accessToken))
                .andExpect(status().isOk());

        assertFalse(postRepository.existsById(post.getId()));
    }

    //create new user
    private User createUser() {
        User user = new User();
        user.setPassword(passwordEncoder.encode("password"));
        user.setEmail("teste@teste.com");
        user.setNotLocked(true);
        user.setEnabled(true);
        user.setUsingMfa(false);
        user.setRoles(Set.of(roleRepository.findByName("ROLE_USER").get()));
        userRepository.save(user);

        Profile profile = new Profile();
        profile.setFirstName("Test");
        profile.setLastName("Test");
        profile.setUser(user);
        user.setProfile(profile);

        profileRepository.save(profile);
        return user;
    }

    //create new post
    private Post createPost() {
        Post post = new Post();
        post.setImageUrls(List.of("url1", "url2"));
        post.setContent("content");
        post.setContent("content");
        post.setUser(user);
        postRepository.save(post);

        return post;
    }

    private String obtainAccessToken(String email, String password) throws Exception {
        Map<String, String> loginDetails = new HashMap<>();
        loginDetails.put("email", email);
        loginDetails.put("password", password);
        String body = objectMapper.writeValueAsString(loginDetails);

        MvcResult result = mockMvc.perform(post("/api/v1/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        return "Bearer " + JsonPath.read(responseContent, "$.data.access_token");
    }

    //insert roles
    private void insertRoles() {
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
}
