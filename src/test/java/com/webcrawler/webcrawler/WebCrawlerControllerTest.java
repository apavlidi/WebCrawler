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
    String url = "invalid";
    this.mockMvc.perform(get("/crawl?url=" + url))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString(String.format("The url:%s provided is invalid",
            url))));
  }

  @Test
  public void should_return_urls_visited() throws Exception {
    String url = "https://monzo.com";
    int limit = 10;
    List<UrlResponse> urlResponse = new ArrayList<>(
        List.of(
            new UrlResponse("https://monzo.com", List.of("https://monzo.com/careers"))));

    ObjectMapper objectMapper = new ObjectMapper();
    String urlResponseJson = objectMapper.writeValueAsString(urlResponse);

    when(crawlerService.crawl(url, limit)).thenReturn(urlResponse);

    this.mockMvc.perform(get("/crawl?url=" + url+"&limit="+limit))
        .andExpect(status().isOk())
        .andExpect(content().json(urlResponseJson));
  }
}
