package at.ac.fhstp.awp_bad.groupxx.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class HelloWorldEndpointTests {

    @Autowired
    private MockMvc mvc;

    @Test
    public void testGetHelloWorldPublicMessage() throws Exception{
        mvc.perform(get("/api/helloworld/hipublic")
                        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string(HelloWorldEndpoint.HI_PUBLIC_MESSAGE));

    }

    @Test
    public void testGetHelloWorldPrivateMessageWithoutJWT_shouldFailWith403() throws Exception{
        mvc.perform(get("/api/helloworld/hiprivate")
                        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().is(403));

    }

    @Test
    public void testGetHelloWorldPrivateMessageWithInvalidJWT_shouldFailWith403() throws Exception{
        String INVALID_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9." +
                "eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzIyODU5NTc4LCJuYW1lIjoiVGVzdHVzZXIifQ." +
                "ZZHrtBoA9SKIm19dps0RB_bA8EdLB_KTbGiQ3-ENc";

        mvc.perform(get("/api/helloworld/hiprivate")
                        .contentType(MediaType.TEXT_PLAIN)
                        .header("Authorization", INVALID_TOKEN))
                .andExpect(status().is(403));

    }

    @Test
    public void testGetHelloWorldPrivateMessageWithValidJWT() throws Exception{
        String VALID_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9." +
                "eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzIyODU5NTc4LCJuYW1lIjoiVGVzdHVzZXIifQ." +
                "ZZHrtBoA9SKIm19dps0RB_bA8EdLB_KTJsbGiQ3-ENc";

        mvc.perform(get("/api/helloworld/hiprivate")
                        .contentType(MediaType.TEXT_PLAIN)
                        .header("Authorization", VALID_TOKEN))
                .andExpect(status().isOk())
                .andExpect(content().string(HelloWorldEndpoint.HI_PRIVATE_MESSAGE));

    }
}
