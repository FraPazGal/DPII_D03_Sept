package controllers;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.FinderService;
import domain.Finder;
import domain.IRobot;

@Controller
@RequestMapping("/finder")
public class FinderController extends AbstractController {

	/* Services */
	
	@Autowired
	private FinderService finderService;

	/* Clear finder */
	@RequestMapping(value = "/search", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Finder finder, final BindingResult binding) {
		ModelAndView result = new ModelAndView("finder/search");
		try {
			this.finderService.delete(finder);
			
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(finder, oops.getMessage());
		}
		return result;
	}

	/* Search as a registered user */
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView search() {
		ModelAndView result = new ModelAndView("finder/search");
		
		try {
			Finder finder = this.finderService.checkIfExpired();
			
			result.addObject("finder", finder);
			result.addObject("iRobots", finder.getResults());
			
		} catch (Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
		}
		result.addObject("requestUri", "finder/search.do");
		return result;
	}
	
	/* Anon search */
	@RequestMapping(value = "/anon/search", method = RequestMethod.GET)
	public ModelAndView searchAnon(@RequestParam (required = false) String keyWord) {
		ModelAndView result = new ModelAndView("finder/anon/search");
		
		try {
			result.addObject("iRobots", this.finderService.searchAnon(keyWord));
			result.addObject("requestUri", "finder/anon/search.do");
			
		} catch (Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
		}
		return result;
		
	}

	/* Save search */
	@RequestMapping(value = "/search", method = RequestMethod.POST, params = "save")
	public ModelAndView search(final Finder finder, BindingResult binding) {
		ModelAndView result = new ModelAndView("finder/search");
		Collection<IRobot> iRobots = new ArrayList<>();
		
		try {
			Finder aux = this.finderService.reconstruct(finder, binding);
			
			if (binding.hasErrors()) {
				result = this.createEditModelAndView(finder);

			} else {
				iRobots = this.finderService.search(aux);
			}
		} catch (Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
		}
		result.addObject("iRobots", iRobots);
		
		return result;
	}

	/* Ancillary methods */
	protected ModelAndView createEditModelAndView(final Finder finder) {
		return this.createEditModelAndView(finder, null);
	}

	protected ModelAndView createEditModelAndView(final Finder finder, final String messageCode) {
		ModelAndView result = new ModelAndView("finder/search");
		final Collection<IRobot> iRobots;
		iRobots = finder.getResults();

		result.addObject("errMsg", messageCode);
		result.addObject("finder", finder);
		result.addObject("iRobots", iRobots);

		return result;
	}

}