package com.xos.smartchat.controller;

import com.xos.smartchat.params.AnswerParam;
import com.xos.smartchat.params.PromptsParam;
import com.xos.smartchat.service.ChatDataService;
import com.xos.web.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/chat-data", produces = MediaType.APPLICATION_JSON_VALUE)
public class ChatDataController {

    @Autowired
    private ChatDataService chatDataService;

    @PostMapping("/prompts")
    public Response prompts(@RequestBody PromptsParam param) {
        try {
            List<String> prompts = chatDataService.getPrompts(param.getQuestion());
            return Response.success(prompts);
        } catch (Throwable thr) {
            log.error(thr.getMessage(), thr);
            return Response.fail(thr.getMessage());
        }
    }

    @PostMapping("/answer")
    public Response answer(@RequestBody AnswerParam param) {
        try {
            String prompts = chatDataService.getAnswer(param.getQuestion());
            return Response.success(prompts);
        } catch (Throwable thr) {
            log.error(thr.getMessage(), thr);
            return Response.fail(thr.getMessage());
        }
    }
}
