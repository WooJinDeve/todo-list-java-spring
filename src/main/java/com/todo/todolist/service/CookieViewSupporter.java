package com.todo.todolist.service;

import static java.lang.Long.parseLong;
import static org.springframework.util.StringUtils.hasText;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class CookieViewSupporter {

    //COOKIE SET : 2020-01-11/1&2020-01-11/2 ...
    private static final String DATE_DELIMITER = "&";
    private static final String KEY_VALUE_DELIMITER = "/";

    private static final int KEY_INDEX = 0;
    private static final int VALUE_INDEX = 1;


    public boolean isFirstView(final String log, final Long todoListId) {
        if (!hasText(log)) return true;
        final var logDB = extract(log);
        final var todayKey = getTodayKey();
        return !logDB.containsKey(todayKey) || !logDB.get(todayKey).contains(todoListId);
    }

    private Map<String, List<Long>> extract(final String logs){
        Map<String, List<Long>> logDB = new HashMap<>();
        Arrays.stream(logs.split(DATE_DELIMITER)).forEach(dateLog -> initializeLogs(logDB, dateLog));
        return logDB;
    }

    private void initializeLogs(final Map<String, List<Long>> logs,
                                final String dateLog){
        final String[] infos = dateLog.split(KEY_VALUE_DELIMITER);
        if (infos.length == 2 && hasText(infos[KEY_INDEX]) && hasText(infos[VALUE_INDEX])) {
            logs.put(infos[KEY_INDEX], logs.getOrDefault(infos[KEY_INDEX], new ArrayList<>()));
            logs.get(infos[KEY_INDEX]).add(parseLong(infos[VALUE_INDEX]));
        }
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
