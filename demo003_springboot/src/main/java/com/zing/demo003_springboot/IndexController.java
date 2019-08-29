package com.zing.demo003_springboot;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * create at     2019/8/29 4:32 下午
 *
 * @author zing
 * @version 0.0.1
 */
@Controller
public class IndexController {
    @RequestMapping({"/", "index", "index.html"})
    public String index(Model model) {
        return "statistics";
    }
}
