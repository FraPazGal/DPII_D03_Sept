<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>


<h1><spring:message	code="purchase.title.list" /></h1>
<display:table class="displaytag" name="purchases" pagesize="5" 
	requestURI="purchase/list.do" id="purchase">

	<display:column titleKey="purchase.irobot" sortable="true">
		<jstl:out value="${purchase.iRobot.title}" />
	</display:column>

	<display:column titleKey="irobot.price" sortable="true">
		<jstl:out value="${purchase.price}" /> &#8364;
	</display:column>
	
	<spring:message code="date.dateFormat" var="format" />
	<display:column titleKey="purchase.purchaseMoment" sortable="true">
		<fmt:formatDate pattern="${format }" value="${purchase.purchaseMoment}" />
	</display:column>
	
	<security:authorize access="hasRole('CUSTOMER')">
		<display:column>
			<a href="purchase/display.do?purchaseId=${purchase.id}"> <spring:message
					code="mp.display" />
			</a>
		</display:column>
	</security:authorize>
</display:table>
