package com.pan.pannet;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/** 
* 
* @author : panjie 
* @date : 2020/2/26  
*/


@Controller
public class IndexController {

    @GetMapping("/")
    public String index(){
        return "index";
    }
}
