<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<h1><spring:message	code="sysconfig.title.edit" /></h1>
<form:form action="config/admin/edit.do" modelAttribute="systemConfiguration" id="form">
<fieldset style="width: 35%">
	<legend style="font-size: 21px"> 
		<spring:message code="sysconfig.title" />
	</legend><br>

		<form:hidden path="id" />
		<form:hidden path="version" />

		<acme:textbox code="config.sysname" path="systemName" size="20px"/><br>
		<acme:textbox code="config.countryCode" path="countryCode" size="20px"/><br>
		<acme:textbox code="config.cache" path="timeResultsCached" size="20px"/><br>
		<acme:textbox code="config.maxResults" path="maxResults" size="20px"/><br>
		<acme:textbox code="config.banner" path="banner" size="80px"/><br>
		<acme:textbox code="config.makers" path="makers" size="80px"/><br>
		
		<strong><spring:message code="welcome.es" /></strong><br>
		<input type="text" name="welcomeES" id="welcomeES" size="100%"
			value="${welcome.get('Español')}" placeholder="<spring:message code='sysconfig.welcome.message.es' />">

		<br><br><strong><spring:message code="welcome.en" /></strong><br>
		<input type="text" name="welcomeEN" id="welcomeEN" size="100%" value="${welcome.get('English')}"
			placeholder="<spring:message code='sysconfig.welcome.message.en' />">
			
		<br><form:errors cssClass="error" path="welcomeMessage" /><br/><br>
		
		<strong><spring:message code="breach.es" /></strong><br>
		<input type="text" name="breachES" id="breachES" size="100%"
			value="${breach.get('Español')}" placeholder="<spring:message code='sysconfig.breach.message.es' />">

		<br><br><strong><spring:message code="breach.en" /></strong><br>
		<input type="text" name="breachEN" id="breachEN" size="100%" value="${breach.get('English')}"
			placeholder="<spring:message code='sysconfig.breach.message.en' />">
			
		<br><form:errors cssClass="error" path="breachNotification" /><br>

		<form:errors cssClass="error" code="${message}" />

		</fieldset>
		<br>
		<acme:submit code="mp.save" name="save"/>
		<acme:cancel code="mp.cancel" url="config/admin/display.do" />
</form:form>
