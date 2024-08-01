package org.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class WeatherService {

    Logger logger = LoggerFactory.getLogger(WeatherService.class);

    @Value("${api.key}")
    private String apiKey;

    private String result = "";

    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    private final String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q={city}&appid={apiKey}";

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    public String getWeatherData(String city){
        logger.info("Get weather in " + city);
        RestTemplate restTemplate = restTemplateBuilder.build();
        String url = apiUrl.replace("{city}", city).replace("{apiKey}", apiKey);
        return restTemplate.getForObject(url, String.class);
    }

    public void startWeatherUpdates(String city) {
        scheduledExecutorService.scheduleAtFixedRate(() -> updateWeatherData(city), 0, 10, TimeUnit.SECONDS);
    }

    private void updateWeatherData(String city) {
        logger.info("Обновление данных о погоде...");

        RestTemplate restTemplate = restTemplateBuilder.build();
        String url = apiUrl.replace("{city}", "London").replace("{apiKey}", apiKey);

        String newResult = restTemplate.getForObject(url, String.class);

        if (!result.equals(newResult)){
            logger.info("Изменение: " + true);
            result = newResult;
            logger.info("Отправка сообщений пользователям с результатом");
            System.out.println(result);
        } else {
            logger.info("Изменение: " + false);
        }
    }
}
