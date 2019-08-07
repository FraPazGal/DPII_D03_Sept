<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>

<security:authorize access="hasRole('CUSTOMER')">

	<h1><spring:message code="purchase.title.display" /></h1>
	<fieldset style="width: 30%">
		<legend style="font-size: 21px">
			<spring:message code="purchase.irobot" />
		</legend>
		<spring:message code="date.dateFormat" var="format" /> 
		<div style="float: left;">
		<br>
			<div>
				<strong><spring:message code="purchase.purchaseMoment" />: </strong>
				<fmt:formatDate pattern="${format }" value="${purchase.purchaseMoment}" />
			</div>
			<br />

			<div>
				<strong><spring:message code="irobot.title" />: </strong>
				<jstl:out value="${purchase.iRobot.title}" />
			</div>
			<br />
			
			<div>
				<strong><spring:message code="irobot.ticker" />: </strong>
				<jstl:out value="${purchase.iRobot.ticker}" />
			</div>
			<br />
			
			<div>
				<strong><spring:message code="irobot.price" />: </strong>
				<jstl:out value="${purchase.iRobot.price}" />
			</div>
			<br />
			
			<jstl:if test="${!purchase.iRobot.isDeleted}">
				<div>
					<a href="iRobot/display.do?iRobotId=${purchase.iRobot.id}"> 
						<spring:message	code="irobot.goto" />
					</a>
				</div>
				<br />
			</jstl:if>
		</div>
	</fieldset>
	<br><br>
	<fieldset style="width: 20%">
		<legend style="font-size: 21px">
			<spring:message code="purchase.title.creditcard.display" />
		</legend>
		<div style="float: left;">
		<br>
			<div>
				<strong><spring:message code="mp.cc.holder" />: </strong>
				<jstl:out value="${purchase.creditCard.holder}" />
			</div>
			<br />
			
			<div>
				<strong><spring:message code="mp.cc.make" />: </strong>
				<jstl:out value="${purchase.creditCard.make}" />
			</div>
			<br />
			
			<div>
				<strong><spring:message code="mp.cc.number" />: </strong>
				<jstl:out value="${purchase.creditCard.number}" />
			</div>
			<br />
			
			<div>
				<strong><spring:message code="mp.cc.expirationMonth" />: </strong>
				<jstl:out value="${purchase.creditCard.expirationMonth}" />
			</div>
			<br />
			
			<div>
				<strong><spring:message code="mp.cc.expirationYear" />: </strong>
				<jstl:out value="${purchase.creditCard.expirationYear}" />
			</div>
			<br />
			
			<div>
				<strong><spring:message code="mp.cc.CVV" />: </strong>
				<jstl:out value="${purchase.creditCard.CVV}" />
			</div>
			<br />
		</div>
	</fieldset>
	<br><br>
	
		<input type="button" name="back"
			value="<spring:message code="mp.back" />"
			onclick="window.history.back()" />
		
</security:authorize>