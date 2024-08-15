package uk.anbu.spring.sample.petclinic.ui;

import java.util.List;

public record MenuBarItem(String link, String active, String title, String glyph, String text) {
	public static final List<MenuBarItem> MENU_ITEMS = List.of(
		new MenuBarItem("/", "home", "home page", "home", "Home"),
		new MenuBarItem("/owners/find", "owners", "find owners", "search", "Find owners"),
		new MenuBarItem("/vets.html", "vets", "veterinarians", "th-list", "Veterinarians"),
		new MenuBarItem("/oups", "error", "trigger a RuntimeException to see how it is handled", "exclamation-triangle", "Error")
	);
}
