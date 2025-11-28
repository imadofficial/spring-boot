package org.imadsnetwork.fsmgnt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller //Vertelt java om dit klasse gebruikt moet worden als Controller
public class HomeController {
    @RequestMapping("/")
    public String Index(){
        return "index.html";
    }
}
