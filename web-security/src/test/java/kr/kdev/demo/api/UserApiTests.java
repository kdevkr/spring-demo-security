package kr.kdev.demo.api;

import kr.kdev.demo.config.ApiTestConfigurer;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
public class UserApiTests extends ApiTestConfigurer {

    @WithMockUser(roles = "ADMIN")
    @Test
    public void TEST_000_currentUser() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/users/me"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        Assert.assertEquals(200, response.getStatus());

        String content = response.getContentAsString();
        LOG.debug("content : {}", content);
    }

    @Test
    public void TEST_001_currentUserWithUser() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/users/me")
                .with(SecurityMockMvcRequestPostProcessors.user("admin").password("password")))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        Assert.assertEquals(200, response.getStatus());

        String content = response.getContentAsString();
        LOG.debug("content : {}", content);
    }

}
