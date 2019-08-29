package controllers;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Collection;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import services.CommentService;
import services.FinderService;
import services.IRobotService;
import services.PurchaseService;
import services.UtilityService;
import domain.Administrator;
import domain.Comment;
import domain.Customer;
import domain.IRobot;
import domain.Purchase;
import domain.Scientist;

@Controller
public class ExportDataController extends AbstractController {

	@Autowired
	private UtilityService utilityService;
	
	@Autowired
	private FinderService finderService;
	
	@Autowired
	private PurchaseService purchaseService;
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private IRobotService iRobotService;

	@RequestMapping(value = "administrator/export.do", method = RequestMethod.GET)
	public @ResponseBody void downloadFileAdministrator(HttpServletResponse resp) {
		String downloadFileName = "dataUser";
		String res;

		Administrator actor = (Administrator) this.utilityService.findByPrincipal();

		res = "Data of your user account:";
		res += "\r\n\r\n";
		res += "Name: " + actor.getName() + " \r\n" + "Surname: "
				+ actor.getSurname() + " \r\n" 
				+ " \r\n" + "Photo: " + actor.getPhoto() + " \r\n" + "Email: "
				+ actor.getEmail() + " \r\n" + "Phone Number: "
				+ actor.getPhoneNumber() + " \r\n" + "Address: "
				+ actor.getAddress() + " \r\n" + " \r\n" + "\r\n";
		
		res += "\r\n\r\n";
		res += "----------------------------------------";
		res += "\r\n\r\n";
		
		res += "Comments:";
		res += "\r\n\r\n";
		Collection<Comment> comments = this.commentService.findCommentByActorId(actor.getId());
		for (Comment comment : comments) {
			res += "Comment: " + "\r\n\r\n";
			res += "Published Date: " + comment.getPublishedDate()+ "\r\n\r\n";
			res += "Title: " + comment.getTitle()+ "\r\n\r\n";
			res += "Body: " + comment.getBody()+ "\r\n\r\n";
			res += "iRobot: " + comment.getIRobot().getTitle()+ "\r\n\r\n";
			res += "-----------";
			res += "\r\n\r\n";
		}
		
		res += "\r\n\r\n";
		res += "----------------------------------------";
		
		String downloadStringContent = res; // implement this
		try {
			OutputStream out = resp.getOutputStream();
			resp.setContentType("text/html; charset=utf-8");
			resp.addHeader("Content-Disposition", "attachment; filename=\""
					+ downloadFileName + "\"");
			out.write(downloadStringContent.getBytes(Charset.forName("UTF-8")));
			out.flush();
			out.close();
		} catch (IOException e) {
		}
	}

	@RequestMapping(value = "customer/export.do", method = RequestMethod.GET)
	public @ResponseBody void downloadFileRookie(HttpServletResponse resp) {
		String downloadFileName = "dataUser";
		String res;

		Customer customer = (Customer) this.utilityService.findUserByPrincipal();

		res = "Data of your user account:";
		res += "\r\n\r\n";
		res += "Name: " + customer.getName() + " \r\n" + "Surname: "
				+ customer.getSurname() + " \r\n" + "VAT:" + customer.getVATNumber()
				+ " \r\n" + "Photo: " + customer.getPhoto() + " \r\n" + "Email: "
				+ customer.getEmail() + " \r\n" + "Phone Number: "
				+ customer.getPhoneNumber() + " \r\n" + "Address: "
				+ customer.getAddress() + " \r\n" + " \r\n" + "\r\n"
				
				+ "Credit Card:" + "\r\n" + "Holder:"
				+ customer.getCreditCard().getHolder() + "\r\n" +
				"Make:" + customer.getCreditCard().getMake() + "\r\n" + "Number:"
				+ customer.getCreditCard().getNumber() + "\r\n"
				+ "Date expiration:"
				+ customer.getCreditCard().getExpirationMonth() + "/"
				+ customer.getCreditCard().getExpirationYear() + "\r\n" + "CVV:"
				+ customer.getCreditCard().getCVV();
		
		res += "\r\n\r\n";
		res += "----------------------------------------";
		res += "\r\n\r\n";
		
		res += "Finder:";
		res += "\r\n";
		res += "Results in last search:" + "\r\n" 
				+ this.finderService.findFinderByCustomerId(customer.getId()).getResults()
				+ "\r\n\r\n";

		res += "\r\n\r\n";
		res += "----------------------------------------";
		res += "\r\n\r\n";

		res += "Purchases:";
		res += "\r\n\r\n";
		Collection<Purchase> purchases = this.purchaseService.purchasesPerCustomer(customer.getId());
		for (Purchase purchase : purchases) {
			res += "Purchase: " + "\r\n\r\n";
			res += "Purchase moment: " + purchase.getPurchaseMoment()+ "\r\n\r\n";
			res += "iRobot: " + purchase.getiRobot().getTitle() + "\r\n\r\n"
			+ "Credit Card:" + "\r\n" + "Holder:"
			+ purchase.getCreditCard().getHolder() + "\r\n" +
			"Make:" + purchase.getCreditCard().getMake() + "\r\n" + "Number:"
			+ purchase.getCreditCard().getNumber() + "\r\n"
			+ "Date expiration:"
			+ purchase.getCreditCard().getExpirationMonth() + "/"
			+ purchase.getCreditCard().getExpirationYear() + "\r\n" + "CVV:"
			+ purchase.getCreditCard().getCVV();
			
			res += "\r\n\r\n";
			res += "-----------";
			res += "\r\n\r\n";
		}
		
		res += "\r\n\r\n";
		res += "----------------------------------------";
		res += "\r\n\r\n";
		
		res += "Comments:";
		res += "\r\n\r\n";
		Collection<Comment> comments = this.commentService.findCommentByActorId(customer.getId());
		for (Comment comment : comments) {
			res += "Comment: " + "\r\n\r\n";
			res += "Published Date: " + comment.getPublishedDate()+ "\r\n\r\n";
			res += "Title: " + comment.getTitle()+ "\r\n\r\n";
			res += "Body: " + comment.getBody()+ "\r\n\r\n";
			res += "iRobot: " + comment.getIRobot().getTitle();
			
			res += "\r\n\r\n";
			res += "-----------";
			res += "\r\n\r\n";
		}

		String downloadStringContent = res;
		try {
			OutputStream out = resp.getOutputStream();
			resp.setContentType("text/html; charset=utf-8");
			resp.addHeader("Content-Disposition", "attachment; filename=\""
					+ downloadFileName + "\"");
			out.write(downloadStringContent.getBytes(Charset.forName("UTF-8")));
			out.flush();
			out.close();
		} catch (IOException e) {
		}
	}

	@RequestMapping(value = "scientist/export.do", method = RequestMethod.GET)
	public @ResponseBody
	void downloadFileCompany(HttpServletResponse resp) {
		String downloadFileName = "dataUser";
		String res;
		
		Scientist scientist = (Scientist) this.utilityService.findUserByPrincipal();

		res = "Data of your user account:";
		res += "\r\n\r\n";
		res += "Name: " + scientist.getName() + " \r\n" + "Surname: "
				+ scientist.getSurname() + " \r\n" + "VAT:" + scientist.getVATNumber()
				+ " \r\n" + "Photo: " + scientist.getPhoto() + " \r\n" + "Email: "
				+ scientist.getEmail() + " \r\n" + "Phone Number: "
				+ scientist.getPhoneNumber() + " \r\n" + "Address: "
				+ scientist.getAddress() + " \r\n" + " \r\n" + "\r\n"
				
				+ "Credit Card:" + "\r\n" + "Holder:"
				+ scientist.getCreditCard().getHolder() + "\r\n" +
				"Make:" + scientist.getCreditCard().getMake() + "\r\n" + "Number:"
				+ scientist.getCreditCard().getNumber() + "\r\n"
				+ "Date expiration:"
				+ scientist.getCreditCard().getExpirationMonth() + "/"
				+ scientist.getCreditCard().getExpirationYear() + "\r\n" + "CVV:"
				+ scientist.getCreditCard().getCVV();
		
		res += "\r\n\r\n";
		res += "----------------------------------------";
		res += "\r\n\r\n";

		res += "iRobots:";
		res += "\r\n\r\n";
		Collection<IRobot> iRobots = this.iRobotService.findIRobotsMine(scientist.getId());
		for (IRobot iRobot : iRobots) {
			res += "iRobot: " + "\r\n\r\n";
			res += "Title: " + iRobot.getTitle()+ "\r\n\r\n";
			res += "Ticker: " + iRobot.getTicker()+ "\r\n\r\n";
			res += "Description: " + iRobot.getDescription()+ "\r\n\r\n";
			res += "Price: " + iRobot.getPrice()+ "\r\n\r\n";
			res += "Decommissioned: " + iRobot.getIsDecommissioned()+ "\r\n\r\n";
			res += "Deleted: " + iRobot.getIsDeleted()+ "\r\n\r\n";
			res += "-----------";
			res += "\r\n\r\n";
		}
		
		res += "\r\n\r\n";
		res += "----------------------------------------";
		res += "\r\n\r\n";
		
		res += "Comments:";
		res += "\r\n\r\n";
		Collection<Comment> comments = this.commentService.findCommentByActorId(scientist.getId());
		for (Comment comment : comments) {
			res += "Comment: " + "\r\n\r\n";
			res += "Published Date: " + comment.getPublishedDate()+ "\r\n\r\n";
			res += "Title: " + comment.getTitle()+ "\r\n\r\n";
			res += "Body: " + comment.getBody()+ "\r\n\r\n";
			res += "iRobot: " + comment.getIRobot().getTitle()+ "\r\n\r\n";
			res += "-----------";
			res += "\r\n\r\n";
		}

		String downloadStringContent = res;
		try {
			OutputStream out = resp.getOutputStream();
			resp.setContentType("text/html; charset=utf-8");
			resp.addHeader("Content-Disposition", "attachment; filename=\""
					+ downloadFileName + "\"");
			out.write(downloadStringContent.getBytes(Charset.forName("UTF-8")));
			out.flush();
			out.close();
		} catch (IOException e) {
		}
	}
}
