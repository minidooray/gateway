package com.nhnacademy.minidooray.gateway.gateway.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class TestController {


    @GetMapping("/addmember")
    public String addMember(Model model){
        return "addmember";
    }
}
