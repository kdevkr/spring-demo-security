package kr.kdev.demo.api;

import kr.kdev.demo.config.ApiTestConfigurer;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
public class LoginApiTests extends ApiTestConfigurer {

    @Test
    public void TEST_000_formLogin() throws Exception {
        MvcResult mvcResult = mvc.perform(SecurityMockMvcRequestBuilders.formLogin().user("admin").password("password"))
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        int status = response.getStatus();
        Assert.assertEquals(302, status);
    }

    @Test
    public void TEST_000_formLoginInvalidPassword() throws Exception {
        mvc.perform(SecurityMockMvcRequestBuilders.formLogin().user("admin").password("admin"))
                .andExpect(SecurityMockMvcResultMatchers.unauthenticated());
    }

    @Test
    public void TEST_001_formLoginWithRole() throws Exception {
        mvc.perform(SecurityMockMvcRequestBuilders.formLogin().user("admin").password("password"))
                .andExpect(SecurityMockMvcResultMatchers.authenticated().withRoles("ADMIN"));
    }

    @Test
    public void TEST_002_logout() throws Exception {
        mvc.perform(SecurityMockMvcRequestBuilders.logout())
                .andDo(MockMvcResultHandlers.print());
    }
}
