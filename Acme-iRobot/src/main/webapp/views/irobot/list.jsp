<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>


<jstl:choose>
	<jstl:when test="${range eq null and scientistName eq null}">
		<h1><spring:message	code="irobot.list" /></h1>
	</jstl:when>
	<jstl:when test="${range eq null and scientistName ne null}">
		<h1><spring:message	code="irobot.scientist.list" /><jstl:out value="${scientistName}" /></h1>
	</jstl:when>
	<jstl:when test="${range == 'mineD' }">
		<h1><spring:message	code="irobot.irobot.list.decommissioned" /></h1>
	</jstl:when>
	<jstl:otherwise>
		<h1><spring:message	code="irobot.irobot.list.active" /></h1>
	</jstl:otherwise>
</jstl:choose>

<jstl:choose>
	<jstl:when test="${isPrincipal}">
	
		<display:table class="displaytag" name="iRobots" pagesize="5" 
			requestURI="iRobot/list.do" id="irobot" style="width:80%">

			<display:column titleKey="irobot.title" sortable="true">
				<jstl:out value="${irobot.title}" />
			</display:column>
			
			<display:column titleKey="irobot.ticker" sortable="true">
				<jstl:out value="${irobot.ticker}" />
			</display:column>
			
			<display:column titleKey="irobot.price" sortable="true">
				<jstl:out value="${irobot.price}" /> &#8364;
			</display:column>
			
			<jstl:if test="${irobot.isDecommissioned eq true}">
				<spring:message var="decommissioned" code='irobot.isDecommissioned.true' />
			</jstl:if>
			<jstl:if test="${irobot.isDecommissioned eq false}">
				<spring:message var="decommissioned" code='irobot.isDecommissioned.false' />
			</jstl:if>
			
			<display:column titleKey="irobot.isDecommissioned" sortable="true">
				<jstl:out value="${decommissioned}" />
			</display:column>
			
			<display:column>
				<a href="iRobot/display.do?iRobotId=${irobot.id}"> <spring:message
						code="mp.display" />
				</a>
			</display:column>
			
			<display:column>
				<a href="iRobot/edit.do?iRobotId=${irobot.id}"> <spring:message
						code="mp.edit" />
				</a>
			</display:column>
			
			<display:column>
				<a href="purchase/list.do?iRobotId=${irobot.id}"> <spring:message
						code="irobot.purchases" />
				</a>
			</display:column>
			
			<display:column>
				<jstl:if test="${irobot.isDecommissioned}">
					<a id="decommission"
						href="iRobot/action.do?action=activate&iRobotId=${irobot.id}">
						<spring:message code="irobot.activate" />
					</a>
				</jstl:if>
				<jstl:if test="${!irobot.isDecommissioned}">
					<a id="decommission"
						href="iRobot/action.do?action=decommission&iRobotId=${irobot.id}">
						<spring:message code="irobot.decommission" />
					</a>
				</jstl:if>
			</display:column>
			<jstl:if test="${irobot.isDecommissioned}">
				<display:column>
					<a id="decommission"
						href="iRobot/action.do?action=delete&iRobotId=${irobot.id}" 
						onclick="return confirm('<spring:message code="irobot.confirm.delete"/>')">
						<spring:message code="irobot.delete" />
					</a>
				</display:column>
			</jstl:if>
		</display:table>
		
		<input type="button"
			onclick="redirect: location.href = 'iRobot/create.do';"
			value="<spring:message code='irobot.create' />" />
			
	</jstl:when>
	<jstl:otherwise>
		<display:table class="displaytag" name="iRobots" pagesize="5" 
			requestURI="iRobot/list.do" id="irobot">

			<display:column titleKey="irobot.title" sortable="true">
				<jstl:out value="${irobot.title}" />
			</display:column>
			
			<display:column titleKey="irobot.ticker" sortable="true">
				<jstl:out value="${irobot.ticker}" />
			</display:column>
			
			<display:column titleKey="irobot.price" sortable="true">
				<jstl:out value="${irobot.price}" /> &#8364;
			</display:column>
			
			<display:column>
				<a href="iRobot/display.do?iRobotId=${irobot.id}"> <spring:message
						code="mp.display" />
				</a>
			</display:column>
			
			<display:column>
				<a href="scientist/display.do?id=${irobot.scientist.id}"> <spring:message
						code="irobot.seller" />
				</a>
			</display:column>
			
			<security:authorize access="hasRole('CUSTOMER')">
				<display:column>
					<a href="purchase/create.do?iRobotId=${irobot.id}"> <spring:message
							code="irobot.purchase" />
					</a>
				</display:column>
			</security:authorize>
		</display:table>
	</jstl:otherwise>
</jstl:choose>