package com.moderngas.restcontroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(name = "/")
@Controller
public class ViewController {

    @GetMapping("index")
    public String index(){
        return "index";
    }

    @GetMapping("admin/index")
    public String admin(){
        return "admin/index";
    }

    @GetMapping("profile/index")
    public String profile(){
        return "profile/index";
    }

    @GetMapping("management/index")
    public String management(){
        return "management/index";
    }
}
