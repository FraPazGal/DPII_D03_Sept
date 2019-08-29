package controllers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CommentService;
import services.IRobotService;
import services.ScientistService;
import services.UtilityService;
import domain.Actor;
import domain.Comment;
import domain.IRobot;
import domain.Scientist;

@Controller
@RequestMapping("/iRobot")
public class IRobotController extends AbstractController {

	/* Services */

	@Autowired
	private UtilityService utilityService;

	@Autowired
	private IRobotService iRobotService;

	@Autowired
	private CommentService commentService;
	
	@Autowired
	private ScientistService scientistService;

	/* Display */
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int iRobotId) {
		ModelAndView result = new ModelAndView("iRobot/display");
		IRobot iRobot = null;
		boolean isPrincipal = false;
		Actor principal;
		Collection<Comment> comments;
		Map<String, String> titleCat = new HashMap<>();

		try {
			comments = this.commentService.getCommentsOfIRobot(iRobotId);
			try {
				principal = this.utilityService.findByPrincipal();
				if (this.iRobotService.findOne(iRobotId).getScientist().equals(principal)) {
					iRobot = this.iRobotService.findOne(iRobotId);
					Assert.isTrue(!iRobot.getIsDeleted(), "not.allowed");
					isPrincipal = true;
				} else {
					iRobot = this.iRobotService.findOneToDisplay(iRobotId);
				}
			} catch (final Throwable oops) {
				iRobot = this.iRobotService.findOneToDisplay(iRobotId);
			}
			result.addObject("iRobot", iRobot);
			result.addObject("titleCat", titleCat);
			result.addObject("isPrincipal", isPrincipal);
			result.addObject("comments", comments);
			result.addObject("requestURI", "iRobot/display.do?iRobotId=" + iRobotId);
			
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
		}
		return result;
	}

	/* Listing */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(
			@RequestParam(required = false) final String range,
			@RequestParam(required = false) final Integer scientistId) {
		ModelAndView result = new ModelAndView("iRobot/list");
		Collection<IRobot> iRobots = null;
		Actor principal = null;
		boolean isPrincipal = false;
		boolean listConf = false;

		try {
			try {
				principal = this.utilityService.findByPrincipal();
				if(this.utilityService.checkAuthority(principal, "SCIENTIST"))
					listConf = true;
			} catch (final Throwable oops) {}
			if(range == null && scientistId == null) {
				iRobots = this.iRobotService.findIRobotsNotDecommissioned();
			} else if (range != null){
				Assert.isTrue(this.utilityService.checkAuthority(principal, "SCIENTIST"), "not.allowed");
				isPrincipal = true;
				
				switch (range) {
				case "mineND":
					iRobots = this.iRobotService.findIRobotsNotDecommissionedAndMine(principal.getId());
					break;

				case "mineD":
					iRobots = this.iRobotService.findIRobotsDecommissionedAndMine(principal.getId());
					break;
				}
			} else if (scientistId != null) {
				iRobots = this.iRobotService.findIRobotsNotDecommissionedAndMine(scientistId);
				Scientist scientist = this.scientistService.findOne(scientistId);
				result.addObject("listSpc", true);
				result.addObject("scientistName", scientist.getName() + " " + scientist.getSurname());
			}else {
				Assert.notEmpty(iRobots);
			}
			
			result.addObject("iRobots", iRobots);
			result.addObject("listConf", listConf);
			result.addObject("isPrincipal", isPrincipal);
			result.addObject("range", range);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
		}
		return result;
	}

	/* Create */
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		try {
			final IRobot iRobot = this.iRobotService.create();

			result = this.createEditModelAndView(iRobot);

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
		}
		return result;
	}

	/* Edit */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int iRobotId) {
		ModelAndView result;
		try {
			IRobot iRobot = this.iRobotService.findOne(iRobotId);
			Assert.notNull(iRobot, "not.allowed");

			Actor principal = this.utilityService.findByPrincipal();
			Assert.isTrue(iRobot.getScientist().equals((Scientist) principal), "not.allowed");

			result = this.createEditModelAndView(iRobot);

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
		}
		return result;
	}

	/* Save */
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(IRobot IRobot, BindingResult binding) {
		ModelAndView result = new ModelAndView("iRobot/edit");

		try {
			IRobot toSave = this.iRobotService.reconstruct(IRobot, binding);
			if (binding.hasErrors()) {

				result.addObject("IRobot", IRobot);
			} else
				try {
					this.iRobotService.save(toSave);
					result = new ModelAndView("redirect:list.do?range=mineND");

				} catch (final Throwable oops) {
					result.addObject("IRobot", toSave);
					result.addObject("errMsg", oops.getMessage());
				}
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(IRobot, oops.getMessage());
		}
		return result;
	}
	
	/* (De)Commission or delete an iRobot */
	@RequestMapping(value = "/action", method = RequestMethod.GET)
	public ModelAndView actionsEnrolments(@RequestParam final String action, @RequestParam final int iRobotId) {
		ModelAndView result = new ModelAndView("redirect:/iRobot/list.do");

		try {
			Actor principal = this.utilityService.findByPrincipal();
			IRobot iRobot = this.iRobotService.findOne(iRobotId);
			Assert.isTrue(iRobot.getScientist().equals((Scientist) principal), "not.allowed");

			if (action.equals("activate")) {
				this.iRobotService.activate(iRobot);
				result = new ModelAndView("redirect:/iRobot/list.do?range=mineND");

			} else if (action.equals("decommission")) {

				this.iRobotService.decommission(iRobot);
				result = new ModelAndView("redirect:/iRobot/list.do?range=mineD");
				
			} else if(action.equals("delete")) {
				this.iRobotService.deleteIRobot(iRobot);
				result = new ModelAndView("redirect:/iRobot/list.do?range=mineD");
			}
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
		}
		return result;
	}

	/* Delete */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int iRobotId) {
		ModelAndView result = new ModelAndView("redirect:list.do?catalog=unpublished");
		try {
			final IRobot iRobot = this.iRobotService.findOne(iRobotId);
			this.iRobotService.delete(iRobot);

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
		}
		return result;
	}

	/* Ancillary methods */
	protected ModelAndView createEditModelAndView(final IRobot iRobot) {
		return this.createEditModelAndView(iRobot, null);
	}

	protected ModelAndView createEditModelAndView(final IRobot IRobot, final String messageCode) {
		ModelAndView result = new ModelAndView("iRobot/edit");
		result.addObject("IRobot", IRobot);
		result.addObject("errMsg", messageCode);

		return result;
	}
}
