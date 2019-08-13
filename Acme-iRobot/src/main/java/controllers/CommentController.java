package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CommentService;
import services.IRobotService;
import domain.Comment;
import domain.IRobot;

@Controller
@RequestMapping("/comment")
public class CommentController extends AbstractController {

	@Autowired
	private CommentService commentService;
	
	@Autowired
	private IRobotService iRobotService;

	// Creating comment

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam Integer iRobotId) {
		ModelAndView result = new ModelAndView("comment/create");

		try {
			IRobot iRobot = this.iRobotService.findOne(iRobotId);
			Comment comment = this.commentService.createWithIRobot(iRobot);
			
			result.addObject("comment", comment);
			result.addObject("iRobot", iRobot);

		} catch (Throwable oops) {
			result = new ModelAndView("redirect:../iRobot/display.do?iRobotId=" + iRobotId);
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Comment comment, BindingResult binding) {
		ModelAndView result = new ModelAndView("comment/create");
		Comment toSave;

		try {
			toSave = this.commentService.reconstruct(comment, binding);

			if (binding.hasErrors()) {
				result.addObject("comment", comment);
			} else {
				this.commentService.save(toSave);

				result = new ModelAndView("redirect:../iRobot/display.do?iRobotId=" + toSave.getIRobot().getId());
			}
		} catch (Throwable oops) {
			result = new ModelAndView("welcome/index");
		}
		return result;
	}
}
