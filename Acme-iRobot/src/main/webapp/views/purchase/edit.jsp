<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<security:authorize access="hasRole('CUSTOMER')">

	<h1><spring:message	code="purchase.title.create" /><i><jstl:out value="${purchaseForm.IRobot.title }"></jstl:out></i></h1>
	<h3><spring:message	code="purchase.price" /><jstl:out value="${purchaseForm.IRobot.price}"></jstl:out> &#8364; </h3><br>
	
	<form:form modelAttribute="purchaseForm" action="purchase/edit.do" id="form">
		<form:hidden path="IRobot"/>
		
		<jstl:if test="${!visible}">
			<jstl:if test="${customer.creditCard ne null}">
				<acme:submit code="purchase.savedCC" name="savedCC" />&nbsp;
			</jstl:if>
			<acme:submit code="purchase.otherCC" name="otherCC" />
		</jstl:if>
		
		<jstl:if test="${visible}">
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
			<br><br>
			<acme:submit code="mp.save" name="save" />&nbsp;
			<acme:cancel url="iRobot/list.do" code="mp.cancel" />
			<br/>
		</jstl:if>
	</form:form>
</security:authorize>