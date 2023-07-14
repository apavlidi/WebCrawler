package com.webcrawler.webcrawler;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webcrawler.webcrawler.service.WebCrawlerService;
import com.webcrawler.webcrawler.web.UrlResponse;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class WebCrawlerControllerTest {

  public static final String INVALID_URL = "invalid";
  public static final String VALID_URL = "https://monzo.com";
  public static final int VALID_LIMIT = 10;
  public static final int INVALID_LIMIT = -1;
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private WebCrawlerService crawlerService;

  @Test
  public void should_return_an_error_when_no_url() throws Exception {
    this.mockMvc.perform(get("/crawl"))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void should_return_an_error_when_invalid_url() throws Exception {
    this.mockMvc.perform(get("/crawl").param("url",INVALID_URL))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString(String.format("The url:%s provided is invalid",
            INVALID_URL))));
  }

  @Test
  public void should_return_errors_when_limit_is_less_than_1_and_url_invalid() throws Exception {
    mockMvc.perform(get("/crawl")
            .param("url", INVALID_URL)
            .param("limit", String.valueOf(INVALID_LIMIT)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("Limit has to be greater than 0")))
        .andExpect(content().string(containsString(String.format("The url:%s provided is invalid", INVALID_URL))));
  }

  @Test
  public void should_return_urls_visited() throws Exception {
    List<UrlResponse> urlResponse = new ArrayList<>(
        List.of(
            new UrlResponse("https://monzo.com", List.of("https://monzo.com/careers"))));

    ObjectMapper objectMapper = new ObjectMapper();
    String urlResponseJson = objectMapper.writeValueAsString(urlResponse);

    when(crawlerService.crawl(VALID_URL, VALID_LIMIT)).thenReturn(urlResponse);

    this.mockMvc.perform(get("/crawl")
            .param("url", VALID_URL)
            .param("limit", String.valueOf(VALID_LIMIT)))
        .andExpect(status().isOk())
        .andExpect(content().json(urlResponseJson));
  }
}
