package com.xos.smartchat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class ChatController {

    @RequestMapping("/chat")
    public String chatPage(Model model) {
        model.addAttribute("placeholder", "请用一句话准确描述您的问题");
        return "/chat";
    }

    @RequestMapping("/dataimport")
    public String dataimport(Model model) {
        model.addAttribute("placeholder", "请用一句话准确描述您的问题");
        return "/dataimport";
    }

}