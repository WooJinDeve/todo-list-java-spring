package com.todo.todolist.service;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.StringUtils.hasText;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class CookieViewSupporter {

    //COOKIE SET : 2020-01-11/1&2020-01-11/2 ...
    private static final String DATE_DELIMITER = "&";
    private static final String KEY_VALUE_DELIMITER = "/";


    public boolean isFirstView(final String log, final Long todoListId) {
        if (!hasText(log)) return true;
        final var logDB = extract(log);
        final var todayKey = getTodayKey();
        return !logDB.containsKey(todayKey) || !logDB.get(todayKey).contains(todoListId);
    }

    private Map<String, List<Long>> extract(final String logs){
        return Arrays.stream(logs.split(DATE_DELIMITER))
                .map(log -> log.split(KEY_VALUE_DELIMITER))
                .collect(groupingBy(log -> log[0], mapping(log -> Long.parseLong(log[1]), toList())));
    }

    public String updateLog(final String log, final Long todoListId){
        if (!hasText(log)) return createLog(todoListId);
        if (isFirstView(log, todoListId)) return log + createLog(todoListId);
        return log;
    }

    private String createLog(Long todoListId) {
        return getTodayKey() + "/" + todoListId + "&";
    }

    private String getTodayKey() {
        final var now = LocalDateTime.now();
        return now.getYear() + "-" + now.getMonthValue() + "-" + now.getDayOfMonth();
    }
}
