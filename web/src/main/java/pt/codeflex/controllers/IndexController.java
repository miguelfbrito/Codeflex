package pt.codeflex.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

	@RequestMapping(value = "/")
	public String index() {
		return "index";
	}

	@RequestMapping(value = "/bar")
	public String bar() {
		return "bar";
	}

	@RequestMapping(value = "/login")
	public String login() {
		return "Account/login";
		as
	}

}
