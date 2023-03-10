package com.gyan.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.gyan.beans.Card;
import com.gyan.beans.Station;
import com.gyan.model.service.CardService;
import com.gyan.model.service.StationService;

@Controller
public class MetroSystemController {

	@Autowired
	private CardService cardService;
	@Autowired
	private StationService stationService;

	// home page
	@RequestMapping("/")
	public ModelAndView homePageController() {
		List<Station> stations = stationService.getStations();
		ModelAndView mv = new ModelAndView("home", "command", new Card());
		mv.addObject("stations", stations);
		return mv;
	}

	// user home page
	@RequestMapping("/userHome")
	public ModelAndView userHomePageController(String message) {
		return new ModelAndView("userHome", "message", message);
	}

	/*
	 * //user Registered page
	 * 
	 * @RequestMapping("/userRegistered") public ModelAndView
	 * userRegisteredPageController(Card card, String message) {
	 * 
	 * return new ModelAndView("userRegistered","message",message); }
	 */

	// new registration page
	@RequestMapping("/registerUser")
	public ModelAndView registerUserPageController() {
		return new ModelAndView("registerUser", "command", new Card());
	}

	// new registration check
	@RequestMapping("/registerationCheck")
	public ModelAndView registerUserController(@ModelAttribute("command") Card card) {
		int id = 0;
		if (card.getUserName() == null || card.getUserName().length() == 0)
			return new ModelAndView("userNotRegistered", "message", "Please input valid user name");
		try {
			id = cardService.registerUser(card.getUserName(), card.getBalance());
			card.setId(id);
		} catch (NumberFormatException e) {
			return new ModelAndView("userNotRegistered", "message", "Please input balance greater than 100");
		}
		if (id != 0)
			return new ModelAndView("userRegistered", "user", card);
		else
			return new ModelAndView("userNotRegistered", "message", "Please input balance greater than 100");
	}

	// login authorization
	@RequestMapping("/loginCheck")
	public ModelAndView loginUserController(@ModelAttribute("command") Card card, HttpSession session) {
		Card userCard = null;
		userCard = cardService.userLogin(card.getId());
		if (userCard != null) {
			session.setAttribute("user", userCard);
			return new ModelAndView("userHome", "user", userCard);
		} else
			return new ModelAndView("invalidUserId", "message", "Please input valid user ID or click forgot user ID");
	}

	// logout
	@RequestMapping("/logout")
	public ModelAndView logoutController(HttpSession session) {
		session.invalidate();
		return this.homePageController();
	}

	// Show user card balance
	@RequestMapping("/showBalance")
	public ModelAndView getBalanceController(HttpSession session) {
		Card card = (Card) session.getAttribute("user");
		double balance = cardService.viewBalance(card.getId());
		return this.userHomePageController("Balance : " + balance);
	}

	// add user card balance
	@RequestMapping("/inputBalance")
	public ModelAndView addBalancePageController(HttpSession session) {
		Card card = (Card) session.getAttribute("user");
		return new ModelAndView("addBalance", "command", card);
	}

	@RequestMapping("/addBalance")
	public ModelAndView addBalanceController(@RequestParam("balance") double balance, HttpSession session) {
		Card card = (Card) session.getAttribute("user");
		boolean addBalance = cardService.addCardBalance(card.getId(), balance);
		if (addBalance)
			return this.userHomePageController("Balance added Successfully");
		return this.userHomePageController("Something went wrong, contact to admin !");
	}

	// Add Station
	@RequestMapping("/inputStation")
	public ModelAndView addStationPageController() {
		return new ModelAndView("addStation");
	}

	@RequestMapping("/addStation")
	public ModelAndView addStationController(@RequestParam("name") String stationName) {
		Station station = stationService.addStation(stationName);
		System.out.println(station);
		System.out.println("Step 2");
		ModelAndView mv = new ModelAndView("addStation");
		System.out.println("Step 1");
		if (station != null) {
			mv.addObject("station", station);
			mv.addObject("message", "Congratulations, your station has been successfully added.");

		} else
			mv.addObject("message", "Something went wrong, contact to admin !");
		return mv;
	}

	@RequestMapping("/check")
	public ModelAndView userLoginController() {
		return new ModelAndView("demo");
	}
}
