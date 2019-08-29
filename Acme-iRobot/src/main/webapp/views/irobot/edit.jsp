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


<jstl:if test="${IRobot.id == 0 }">
	<h1><spring:message	code="irobot.create" /></h1>
</jstl:if>
<jstl:if test="${IRobot.id != 0 }">
	<h1><spring:message	code="irobot.title.edit" /></h1>
</jstl:if>
<form:form modelAttribute="IRobot" action="iRobot/edit.do" id="form">

	<fieldset style="width: 25%">
		<legend style="font-size: 21px">
			<spring:message code="irobot.legend" />
		</legend>
		<br/>

		<form:hidden path="id" />
		<acme:textbox code="irobot.title" path="title" size="100px" /><br/> 
		<acme:textarea code="irobot.description" path="description" cols="71px" rows="4"/><br/> 
		<acme:textbox code="irobot.price" path="price" size="20px" placeholder="price.placeholder"/><br/> 
	
	</fieldset>
	<br>
	
	<acme:submit code="mp.save" name="save" />&nbsp;
	<acme:cancel url="iRobot/list.do?range=mineND" code="mp.cancel" />
	<br />
</form:form>
