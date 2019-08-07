<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<security:authorize access="permitAll">

	<h1><jstl:out value="${iRobot.title}" /></h1>
	<fieldset style="width: 30%">
	<legend style="font-size: 21px">
		<spring:message code="irobot.details" />
	</legend>
	<spring:message code="date.dateFormat" var="format" /> 
	<div style="float: left;">
	<br>
		<div>
			<strong><spring:message code="irobot.description" />: </strong><br><br>
			<jstl:out value="${iRobot.description}" />
		</div>
		<br />
		
		<div>
			<strong><spring:message code="irobot.ticker" />: </strong>
			<jstl:out value="${iRobot.ticker}" />
		</div>
		<br />
		
		<div>
			<strong><spring:message code="irobot.price" />: </strong>
			<jstl:out value="${iRobot.price}" />
		</div>
		<br />
		
		<jstl:if test="${iRobot.isDecomissioned eq true}">
			<spring:message var="status" code='irobot.isDecomissioned.true' />
		</jstl:if>
		<jstl:if test="${iRobot.isDecomissioned eq false}">
			<spring:message var="status" code='irobot.isDecomissioned.false' />
		</jstl:if>
		
		<jstl:if test="${isPrincipal }">
			<div>
				<strong><spring:message code="irobot.isDecomissioned" />: </strong>
				<jstl:out value="${status}" />
			</div>
			<br />
		</jstl:if>
		</div>
	</fieldset>

	<jstl:if test="${not empty comments}">
		<h2>
			<strong><spring:message code="comments" /></strong>
		</h2>
	
		<display:table style="width: 80%" class="displaytag"
			name="comments" pagesize="5"
			requestURI="iRobot/display.do?iRobotId=${iRobot.id}"
			id="comment">
	
			<display:column titleKey="comment.title">
				<jstl:out value="${comment.title}" />
			</display:column>
	
			<display:column titleKey="comment.body">
				<jstl:out value="${comment.body}" />
			</display:column>
	
			<display:column titleKey="comment.author">
				<jstl:out value="${comment.author}" />
			</display:column>
	
			<display:column titleKey="comment.publishedDate">
				<jstl:out value="${comment.publishedDate}" />
			</display:column>
	
		</display:table>
	</jstl:if>
			
	<security:authorize access="hasAnyRole('CUSTOMER', 'SCIENTIST')">
		<br>
		<input type="button"
			onclick="redirect: location.href = 'comment/create.do?iRobotId=${iRobot.id}';"
			value="<spring:message code='comment.create' />" /><br>
	</security:authorize>
	<br>
	<input type="button" name="back"
		value="<spring:message code="mp.back" />"
		onclick="window.history.back()" />

</security:authorize>
