package pt.codeflex.controllers;

import java.text.ParseException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import pt.codeflex.databasemodels.Language;


@Controller
public class TestsController {

	@Autowired
	private DatabaseController databaseController;

	@GetMapping("/stress")
	public void stressCompiler() {
		Optional<Language> language = databaseController.viewLanguageById(1);

		if (language.isPresent()) {
			for (int i = 0; i < 1000; i++) {
				try {
					System.out.println("Submitting number #" + i);
					databaseController.addSubmissions(1, 1, language.get().getName(), "aW1wb3J0IGphdmEuaW8uKjsNCmltcG9ydCBqYXZhLnV0aWwuKjsNCmltcG9ydCBqYXZhLnRleHQuKjsNCmltcG9ydCBqYXZhLm1hdGguKjsNCmltcG9ydCBqYXZhLnV0aWwucmVnZXguKjsNCg0KcHVibGljIGNsYXNzIFNvbHV0aW9uIHsNCg0KCXB1YmxpYyBzdGF0aWMgdm9pZCBtYWluKFN0cmluZ1tdIGFyZ3MpIHsNCgkJU2Nhbm5lciBpbiA9IG5ldyBTY2FubmVyKFN5c3RlbS5pbik7DQoJCWludCBuID0gaW4ubmV4dEludCgpOw0KCQlpbnQgc2NvcmVzW10gPSBuZXcgaW50W25dOw0KCQlmb3IgKGludCBpID0gMDsgaSA8IG47IGkrKykgew0KCQkJc2NvcmVzW2ldID0gaW4ubmV4dEludCgpOw0KCQl9DQoJCW1pbmltdW1EaXN0YW5jZXMobiwgc2NvcmVzKTsNCgl9DQoNCglzdGF0aWMgdm9pZCBtaW5pbXVtRGlzdGFuY2VzKGludCBuLCBpbnQgYXJyYXlbXSkgew0KCQlpbnQgbWluID0gSW50ZWdlci5NQVhfVkFMVUU7DQoJCWZvciAoaW50IGkgPSAwOyBpIDwgbi0xOyBpKyspIHsNCgkJCWZvcihpbnQgaiA9IGkrMTsgajxuOyBqKyspIHsNCgkJCQlpZihhcnJheVtpXSA9PSBhcnJheVtqXSl7DQoJCQkJCW1pbiA9IE1hdGgubWluKG1pbiwgIE1hdGguYWJzKGktaikpOw0KCQkJCX0NCgkJCX0NCgkJfQ0KCQlpZihtaW49PUludGVnZXIuTUFYX1ZBTFVFKSB7DQoJCQltaW4gPSAtMTsNCgkJfQ0KCQlTeXN0ZW0ub3V0LnByaW50bG4obWluKTsNCgl9DQoNCg0KfQ");
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	}

}