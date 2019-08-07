
<%--
 * login.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<h1><spring:message code="finder.title" /></h1>
<spring:message code="date.dateFormat" var="format" /> 
<security:authorize access="hasRole('CUSTOMER')">
	
	<form:form action="finder/search.do" modelAttribute="finder" id="form">

		<form:hidden path="id" />
		<acme:textbox code="finder.keyWord" path="keyWord" size="50px" /><br/> <br/>
		<acme:textbox code="finder.maximumPrice" path="maximumPrice" size="50px" placeholder="price.placeholder"/><br/> <br/>
		<acme:textbox code="finder.minimumPrice" path="minimumPrice" size="50px" placeholder="price.placeholder"/><br/> <br/>
		<br>

		<input type="submit" name="save" id="save"
			value="<spring:message code="finder.showResults" />" />
		
	&#160;
		<jstl:if test="${finder.id!=0}">
			<input type="submit" name="delete" id="delete"
				value='<spring:message code="mp.clear"/>' />
		</jstl:if>

	</form:form>

	<jstl:if test="${not empty iRobots}">
		<h2><spring:message code="finder.results" /></h2>
		<display:table name="iRobots" id="row"
			requestURI="${requestURI}" pagesize="10" class="displaytag">

			<!-- Attributes-->

			<display:column titleKey="irobot.title" sortable="true">
				<jstl:out value="${row.title}" />
			</display:column>
			<display:column titleKey="irobot.ticker" sortable="true">
				<jstl:out value="${row.ticker}" />
			</display:column>
			<display:column titleKey="irobot.price" sortable="true">
				<jstl:out value="${row.price}" />
			</display:column>

			<!-- Action links -->

			<display:column>
				<a href="iRobot/display.do?iRobotId=${row.id}"> <spring:message
						code="mp.display" />
				</a>
			</display:column>
			<display:column>
				<a href="purchase/create.do?iRobotId=${irobot.id}"> <spring:message
						code="irobot.purchase" />
				</a>
			</display:column>
		
		</display:table>
	</jstl:if>
</security:authorize>

<security:authorize access="!hasRole('CUSTOMER')">

	<form>
		<b><spring:message code="finder.enterKey"/>&#160;</b> <input id="test"
			type="text" name="keyWord" size="20" />

		<script>
			var keyWord = "";
			document.getElementById("test").value = keyWord;
		</script>

		<input type="submit" value="Search" name="submit" />

	</form>

	<br>
	<br>
	<jstl:if test="${not empty iRobots}">
		<display:table name="iRobots" id="row"
			requestURI="finder/list.do" pagesize="10" class="displaytag">

			<!-- Attributes-->

			<display:column titleKey="irobot.title" sortable="true">
				<jstl:out value="${row.title}" />
			</display:column>
			<display:column titleKey="irobot.ticker" sortable="true">
				<jstl:out value="${row.ticker}" />
			</display:column>
			<display:column titleKey="irobot.price" sortable="true">
				<jstl:out value="${row.price}" />
			</display:column>

			<!-- Action links -->

			<display:column>
				<a href="iRobot/display.do?iRobotId=${row.id}"> <spring:message
						code="mp.display" />
				</a>
			</display:column>
		</display:table>
	</jstl:if>
</security:authorize>