<%--
 * header.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>

<div>
	<ul id="jSubMenu">
		
		<security:authorize access="hasRole('SCIENTIST')">
				<li><a href="iRobot/list.do"><spring:message
								code="irobot.list" /></a></li>
			
				<li><a class="fNiv"><spring:message
						code="irobot.irobot.list.scientist" /></a>
				<ul>
					<li><a href="iRobot/list.do?range=mineND"><spring:message
								code="irobot.irobot.list.active" /></a></li>
								
					<li><a href="iRobot/list.do?range=mineD"><spring:message
								code="irobot.irobot.list.decomissioned" /></a></li>

				</ul></li>
				
		</security:authorize>
	</ul>
</div>
