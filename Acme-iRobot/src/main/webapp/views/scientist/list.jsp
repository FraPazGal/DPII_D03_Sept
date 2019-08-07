<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>


<security:authorize access="permitAll">

<h1><spring:message	code="scientist.title.list" /></h1>

	<display:table class="displaytag" name="scientists" pagesize="5" 
		requestURI="scientist/list.do" id="scientist">

		<display:column titleKey="actor.name" sortable="true">
			<jstl:out value="${scientist.name}" />
		</display:column>

		<display:column titleKey="actor.surname" sortable="true">
			<jstl:out value="${scientist.surname}" />
		</display:column>
		
		<display:column titleKey="actor.VATNumber" sortable="true">
			<jstl:out value="${scientist.VATNumber}" />
		</display:column>
		
		<display:column>
			<a href="scientist/display.do?id=${scientist.id}"> <spring:message
					code="mp.display" />
			</a>
		</display:column>
		
		<display:column>
			<a href="iRobot/list.do?scientistId=${scientist.id}"> <spring:message
					code="scientist.irobots" />
			</a>
		</display:column>
		
		
		
	</display:table>
	
</security:authorize>