<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<form:form action="comment/edit.do" modelAttribute="comment" id="form">
<h1><spring:message	code="comment.title.create" /><i><jstl:out value="${comment.IRobot.title }"></jstl:out></i></h1>
<fieldset style="width: 20%">
	<legend style="font-size: 21px">
		<spring:message code="comment.legend" />
	</legend>
	<br>

		<form:hidden path="IRobot" />
		<acme:textbox code="comment.title" path="title" size="113px"/><br>
		<acme:textarea code="comment.body" path="body" cols="80px" rows="4"/><br>
		
</fieldset>
<br>
		<acme:submit code="mp.save" name="save" />&nbsp;
		<acme:cancel code="mp.cancel" url="iRobot/display.do?iRobotId=${comment.IRobot.id}" />
</form:form>