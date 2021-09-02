package norman.dough.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomePage {
    private static final Logger LOGGER = LoggerFactory.getLogger(HomePage.class);

    @GetMapping("/")
    public String loadHomePage() {
        return "index";
    }
}
