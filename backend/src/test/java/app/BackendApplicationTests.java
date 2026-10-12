package app;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

@SpringBootTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:pawlytest;MODE=MySQL;DB_CLOSE_DELAY=-1",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "jwt.secret=987ac934d072458fa8127d94c62c33b89c4d91b786e3094a",
    "jwt.expiration=86400000"
})
@AutoConfigureMockMvc
class BackendApplicationTests {
    @Autowired private MockMvc mvc;

    @Test void contextLoads() { }

    @Test void protectedEndpointRejectsMissingAndInvalidJwt() throws Exception {
        mvc.perform(get("/api/test")).andExpect(status().isUnauthorized());
        mvc.perform(get("/api/test").header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isUnauthorized());
    }

    @Test void invalidRegistrationIsRejectedWithoutDetails() throws Exception {
        mvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"\",\"email\":\"not-an-email\",\"password\":\"short\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid request"));
    }

    @Test void failedLoginDoesNotRevealWhetherAccountExists() throws Exception {
        String body = "{\"email\":\"absent@example.test\",\"password\":\"not-a-valid-password\"}";
        mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid email or password"));
    }

    @Test void registrationResponseDoesNotExposePasswordHash() throws Exception {
        mvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Test Parent\",\"email\":\"test-parent@example.test\",\"password\":\"A-long-test-password-42\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test-parent@example.test"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }
}
