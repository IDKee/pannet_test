package com.pan.pannet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
*
* @author : panjie
* @date : 2020/2/26
*/

@Controller
public class TestController {

    @Autowired
    private HttpServletRequest request;

    @GetMapping("test")
    public String test(){
        HashMap map = new HashMap();
        map.put("name", "panjie");
        request.setAttribute("map", map);
        return "pannet/test";
    }
}
