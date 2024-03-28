package com.ratz.greenbites.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.ratz.greenbites.entity.*;
import com.ratz.greenbites.repository.PostRepository;
import com.ratz.greenbites.repository.ProfileRepository;
import com.ratz.greenbites.repository.RoleRepository;
import com.ratz.greenbites.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

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
    Post anotherUsersPost;
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

        anotherUsersPost = createAnotherUsersPost();
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
    @Order(14)
    @DisplayName("When delete post then success")
    public void whenDeletePost_thenSuccess() throws Exception {

        mockMvc.perform(delete("/api/v1/posts/" + post.getId())
                        .header("Authorization", accessToken))
                .andExpect(status().isOk());

        assertFalse(postRepository.existsById(post.getId()));
    }

    @Test
    @Order(7)
    @DisplayName("When create post without authorization then fail")
    public void whenCreatePostWithoutAuthorization_thenFail() throws Exception {
        String newPostJson = "{\"content\":\"Unauthorized post content\"}";
        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newPostJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(8)
    @DisplayName("When get posts by nonexistent user id then fail")
    public void whenGetPostsByNonexistentUserId_thenFail() throws Exception {
        mockMvc.perform(get("/api/v1/posts/user/" + 99L)
                        .header("Authorization", accessToken))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(9)
    @DisplayName("When update another user's post then fail")
    public void whenUpdateAnotherUsersPost_thenFail() throws Exception {


        String updatedPostJson = "{\"content\":\"Unauthorized update content\"}";
        mockMvc.perform(put("/api/v1/posts/" + anotherUsersPost.getId())
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedPostJson))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(10)
    @DisplayName("When delete another user's post then fail")
    public void whenDeleteAnotherUsersPost_thenFail() throws Exception {

        mockMvc.perform(delete("/api/v1/posts/" + anotherUsersPost.getId())
                        .header("Authorization", accessToken))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(11)
    @DisplayName("When create post with no content then fail")
    public void whenCreatePostWithNoContent_thenFail() throws Exception {
        String emptyContentPostJson = "{}";
        mockMvc.perform(post("/api/v1/posts")
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(emptyContentPostJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(12)
    @DisplayName("When add post to collection then success")
    public void whenAddPostToCollection_thenSuccess() throws Exception {
        Long collectionId = 1L;
        System.out.println("Post ID: " + post.getId());
        mockMvc.perform(post("/api/v1/posts/" + post.getId() + "/addToCollection/" + collectionId)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());


        MvcResult result = mockMvc.perform(get("/api/v1/collections/" + collectionId)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        assertTrue(responseContent.contains(post.getId().toString()));

    }

    @Test
    @Order(13)
    @DisplayName("When remove post from collection then success")
    public void whenRemovePostFromCollection_thenSuccess() throws Exception {
        long collectionId = 1L;
        System.out.println("Post ID: " + post.getId());
        mockMvc.perform(delete("/api/v1/posts/" + post.getId() + "/removeFromCollection/" + collectionId)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());

        MvcResult result = mockMvc.perform(get("/api/v1/collections/" + collectionId)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        assertTrue(responseContent.contains("postIds\":null"));
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


        Profile profile = new Profile();
        profile.setFirstName("Test");
        profile.setLastName("Test");
        profile.setUser(user);
        user.setProfile(profile);


        Collection collection = new Collection();
        collection.setName("Test collection");
        collection.setUser(user);
        user.setCollections(Set.of(collection));

        userRepository.save(user);
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

    //create another user's post
    private Post createAnotherUsersPost() {
        User anotherUser = new User();
        anotherUser.setPassword(passwordEncoder.encode("password"));
        anotherUser.setEmail("teste2@email.com");
        anotherUser.setNotLocked(true);
        anotherUser.setEnabled(true);
        anotherUser.setUsingMfa(false);
        anotherUser.setRoles(Set.of(roleRepository.findByName("ROLE_USER").get()));
        userRepository.save(anotherUser);

        Post anotherUsersPost = new Post();
        anotherUsersPost.setImageUrls(List.of("url1", "url2"));
        anotherUsersPost.setContent("content");
        anotherUsersPost.setUser(anotherUser);
        postRepository.save(anotherUsersPost);

        return anotherUsersPost;
    }
}
