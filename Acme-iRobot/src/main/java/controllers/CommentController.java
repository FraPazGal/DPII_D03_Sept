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
		ModelAndView res = new ModelAndView("comment/create");

		try {
			IRobot iRobot = this.iRobotService.findOne(iRobotId);
			Comment comment = this.commentService.createWithIRobot(iRobot);
			
			res.addObject("comment", comment);
			res.addObject("iRobot", iRobot);

		} catch (Throwable oops) {
			res = new ModelAndView("redirect:../iRobot/display.do?iRobotId=" + iRobotId);
		}
		return res;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Comment comment, BindingResult binding) {
		ModelAndView res;
		Comment toSave;

		try {
			toSave = this.commentService.reconstruct(comment, binding);

			if (binding.hasErrors()) {
				res = new ModelAndView("comment/create");
				res.addObject("comment", comment);
			} else {
				this.commentService.save(toSave);

				res = new ModelAndView("redirect:../iRobot/display.do?iRobotId=" + toSave.getIRobot().getId());
			}
		} catch (Throwable oops) {
			res = new ModelAndView("welcome/index");
		}
		return res;
	}
}
