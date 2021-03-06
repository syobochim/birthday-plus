package com.syobochim;

import com.fasterxml.jackson.databind.ObjectMapper;
import spark.ResponseTransformer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.get;
import static spark.SparkBase.port;
import static spark.SparkBase.staticFileLocation;

/**
 * @author syobochim
 */
public class BirthdayPlus {

    public static void main(String... args) {

        String port = System.getenv("PORT");
        if (port == null) {
            port = "4567";
        }
        port(Integer.valueOf(port));
        staticFileLocation("/public");

        get("/plus", (req, res) -> {
            int year = Integer.parseInt(req.queryParams("year"));
            int month = Integer.parseInt(req.queryParams("month"));
            int day = Integer.parseInt(req.queryParams("day"));
            LocalDate birthday = LocalDate.of(year, month, day);
            LocalDate plusDays = birthday.plusDays(10000);

            Map<String, Object> map = new HashMap<>();
            map.put("year", plusDays.getYear());
            map.put("month", plusDays.getMonthValue());
            map.put("day", plusDays.getDayOfMonth());
            map.put("yyyyMMdd", plusDays.format(DateTimeFormatter.ofPattern("yyyyMMdd")));

            return map;
        }, new JsonTransformer());
    }

    private static class JsonTransformer implements ResponseTransformer {
        private ObjectMapper mapper = new ObjectMapper();

        @Override
        public String render(Object model) throws Exception {
            return this.mapper.writeValueAsString(model);
        }
    }

}
