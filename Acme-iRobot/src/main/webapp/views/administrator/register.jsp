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
<security:authorize access="hasRole('ADMIN')">
	<form:form modelAttribute="actorRegistrationForm"
		action="administrator/register.do"
		onsubmit="javascript: return checkPhone('${confirmTelephone}');">

		<!-- User Account Attributes -->
		<h1><spring:message	code="actor.register.admin" /></h1>
		<fieldset style="width: 25%">
			<legend style="font-size: 21px">
				<spring:message code="actor.userAccount" />
			</legend>
			<br>

			<acme:textbox code="actor.username" path="username" size="40px"/><br/>
			<acme:textbox code="actor.password" path="password" password="true" size="40px"/><br/>
			
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
			
			<form:errors path="phoneNumber" cssClass="error" />
			<strong><form:label path="phoneNumber">
					<spring:message code="actor.phone" />
				</form:label></strong>
			<form:input path="phoneNumber" size="40px"/>
			<br/><br>
		
			<acme:textbox code="actor.address" path="address" size="40px"/><br/>
		</fieldset>
		<br /><br>


		<!-- Buttons -->
		<acme:submit code="mp.save" name="save" />&nbsp;
		<acme:cancel url="/" code="mp.cancel" />

	</form:form>
</security:authorize>