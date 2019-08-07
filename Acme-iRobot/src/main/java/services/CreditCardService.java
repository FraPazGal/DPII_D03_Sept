package services;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

@Transactional
@Service
public class CreditCardService {
	
	
	// Other business methods -------------------------------
	
	public boolean checkIfExpired(Integer expirationMonth,
			Integer expirationYear) throws ParseException {

		boolean res = false;
		
		String expiration = ((expirationMonth.toString().length() == 1) ? "0"
				+ expirationMonth.toString() : expirationMonth.toString())
				+ ((expirationYear.toString().length() == 1) ? "0"
						+ expirationYear.toString() : expirationYear.toString());

		DateFormat date = new SimpleDateFormat("MMyy");

		Date expiryDate = date.parse(expiration);
		
		// Last day of the month in which the CC expires
		Calendar expDate = Calendar.getInstance();
		expDate.setTime(expiryDate);
		expDate.add(Calendar.MONTH, 1);
		expDate.add(Calendar.DAY_OF_MONTH, -1);

		// Date in 7 days
		Calendar nowPlus7 = Calendar.getInstance();
		nowPlus7.add(Calendar.DAY_OF_MONTH, 7);

		if (nowPlus7.getTime().after(expDate.getTime()))
			res = true;

		return res;
	}
}
