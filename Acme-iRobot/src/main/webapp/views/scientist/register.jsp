<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<script>
	function checkPhone(msg) {
		var phone = document.getElementById("phoneNumber").value;
		var pattern = /^(((([+][1-9]{1}[0-9]{0,2}[\s]){0,1}([(][1-9]{1}[0-9]{0,2}[)][\s]){0,1})){0,1}([0-9]{4}){1}([0-9]{0,}))$/;
		var pat = pattern.test(phone);
		if (pat) {
			return true;
		} else {
			return confirm(msg);
		}
	}
</script>

<spring:message code="phone.confirmation" var="confirmTelephone" />
<security:authorize access="!isAuthenticated()">
	<form:form modelAttribute="userRegistrationForm" action="scientist/register.do"
		onsubmit="javascript: return checkPhone('${confirmTelephone}');">

		<!-- User Account Attributes -->
		<h1><spring:message	code="actor.register.scientist" /></h1>
		<fieldset style="width: 25%">
			<legend style="font-size: 21px">
				<spring:message code="actor.userAccount" />
			</legend>
			<br>

			<acme:textbox code="actor.username" path="username" size="40px"/><br/>
			<acme:textbox code="actor.password" path="password" password="true" size="40px"/><br/>
			<acme:textbox code="actor.passwordConfirmation" path="passwordConfirmation" password="true" size="40px"/><br/>
		</fieldset>
		<br />

		<!-- Actor Attributes -->
		<fieldset style="width: 25%">
			<legend style="font-size: 21px">
				<spring:message code="actor.personalData" />
			</legend>
			<br/>

			<acme:textbox code="actor.name" path="name" size="40px"/><br/>
			<acme:textbox code="actor.surname" path="surname" size="40px"/><br/>
			<acme:textbox code="actor.photo" path="photo" size="40px"/><br/>
			<acme:textbox code="actor.email" path="email" size="40px"/><br/>
			<acme:textbox code="actor.phone" path="phoneNumber" size="40px"/><br/>
			<acme:textbox code="actor.address" path="address" size="40px"/><br/>
			<acme:textbox code="actor.VATNumber" path="VATNumber" size="40px"/><br/>

		</fieldset>
		<br /><br>
		
		<fieldset style="width: 25%">
			<legend style="font-size: 21px">
				<spring:message code="mp.cc.creditCard" />
			</legend>
			<br/>
			
			<acme:textbox code="mp.cc.holder" path="holder" size="40px"/><br/>
			<acme:selectString items="${makers}" code="mp.cc.make" path="make"/><br/>
			<acme:textbox code="mp.cc.number" path="number" placeholder="ccnumber.placeholder" size="40px"/><br/>
			<acme:textbox code="mp.cc.expirationMonth" path="expirationMonth" placeholder="expirationMonth.placeholder" size="5px"/><br/>
			<acme:textbox code="mp.cc.expirationYear" path="expirationYear" placeholder="expirationMonth.placeholder" size="5px"/><br/>
			<acme:textbox code="mp.cc.CVV" path="CVV" placeholder="cvv.placeholder" size="5px"/><br/>
			
		</fieldset>
		<br />
		
		<fieldset>
			<legend style="font-size: 21px">
				<spring:message code="actor.terms" />
			</legend>

			<acme:checkbox code="actor.acceptTerms" path="termsAndConditions"/>
		</fieldset>
		<br />

		<!-- Buttons -->
		<acme:submit code="mp.save" name="save" />&nbsp;
		<acme:cancel url="/" code="mp.cancel" />

	</form:form>
</security:authorize>