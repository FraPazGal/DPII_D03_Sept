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
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>

<div>
	<a href="#"><img src="${banner}" alt="Acme-iRobot Co., Inc."
		style="margin-bottom: 0.5em; max-height: 250px;" /></a>
</div>

<div>
	<ul id="jMenu">
		<!-- Do not forget the "fNiv" class for the first level links !! -->

		
		<security:authorize access="permitAll">
			<li><a class="fNiv"><spring:message
							code="master.page.irobot" /></a>
					<ul>
						<li class="arrow"></li>
							<li><a href="iRobot/list.do"><spring:message
										code="master.page.irobot.list" /></a></li>
										
						<security:authorize access="hasRole('SCIENTIST')">
							<li><a href="iRobot/create.do"><spring:message
										code="master.page.irobot.new" /></a></li>
						</security:authorize>
						
						<security:authorize access="hasRole('CUSTOMER')">
							<li><a href="finder/search.do"><spring:message
										code="master.page.irobot.finder" /></a></li>
						</security:authorize>
						<security:authorize access="!hasRole('CUSTOMER')">
							<li><a href="finder/anon/search.do"><spring:message
										code="master.page.irobot.finder" /></a></li>
						</security:authorize>
					</ul></li>
					
			<li><a href="scientist/list.do"><spring:message
				code="master.page.scientist.list" /></a></li>
										
		</security:authorize>
		
		<security:authorize access="hasRole('ADMIN')">
			<!-- Register admin -->
			<li><a class="fNiv"><spring:message
						code="master.page.register" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="administrator/register.do"><spring:message
								code="master.page.register.admin" /></a></li>
				</ul></li>

		</security:authorize>
		
		<security:authorize access="hasRole('CUSTOMER')">
			<li><a href="purchase/list.do"><spring:message
								code="master.page.purchase" /></a></li>
		</security:authorize>
		

		<security:authorize access="isAnonymous()">
			<li><a class="fNiv"><spring:message
						code="master.page.singup" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="customer/register.do"><spring:message
								code="master.page.register.customer" /></a></li>
					<li><a href="scientist/register.do"><spring:message
								code="master.page.register.scientist" /></a></li>
				</ul></li>
				
			<li><a class="fNiv" href="security/login.do"><spring:message
						code="master.page.login" /></a></li>
		</security:authorize>
		
		<security:authorize access="hasRole('ADMIN')">
			
			<li><a href="statistics/display.do"><spring:message
						code="master.page.administrator.statistics" /></a></li>
						
			<li><a href="config/admin/display.do"><spring:message
									code="master.page.system" /></a></li>
		</security:authorize>
		
		<security:authorize access="isAuthenticated()">
			<li><a class="fNiv"> <spring:message
						code="master.page.profile" /> (<security:authentication
						property="principal.username" />)
			</a>
				<ul>
					<li class="arrow"></li>
					<security:authorize access="hasRole('ADMIN')">
						<li><a href="administrator/display.do"><spring:message
									code="master.page.actor.view" /></a></li>
						<li><a href="administrator/edit.do"><spring:message
									code="master.page.actor.edit" /></a></li>
						<li><a href="administrator/export.do"><spring:message
									code="master.page.export" /></a></li>
					</security:authorize>

					<security:authorize access="hasRole('CUSTOMER')">
						<li><a href="customer/display.do"><spring:message
									code="actor.view" /></a></li>
						<li><a href="customer/edit.do"><spring:message
									code="master.page.actor.edit" /></a></li>
						<li><a href="customer/export.do"><spring:message
									code="master.page.export" /></a></li>
					</security:authorize>

					<security:authorize access="hasRole('SCIENTIST')">
						<li><a href="scientist/display.do"><spring:message
									code="actor.view" /></a></li>
						<li><a href="scientist/edit.do"><spring:message
									code="master.page.actor.edit" /></a></li>
						<li><a href="scientist/export.do"><spring:message
									code="master.page.export" /></a></li>
					</security:authorize>
					
					<li><a href="j_spring_security_logout"><spring:message
								code="master.page.logout" /> </a></li>
				</ul></li>
		</security:authorize>
	</ul>
</div>

<div style="float: right;">

	<a href="?language=en"><img style="width: 20px; height: 15px"
		src="https://upload.wikimedia.org/wikipedia/en/thumb/a/ae/Flag_of_the_United_Kingdom.svg/1280px-Flag_of_the_United_Kingdom.svg.png"
		alt="EN"></a> <span>|</span> <a href="?language=es"><img
		style="width: 20px; height: 15px;"
		src="http://www.ahb.es/m/100150RES.jpg" alt="ES"></a>
</div>

<security:authorize access="isAuthenticated()">
	<jstl:if test="${breachNotification ne null }">
		<jstl:if test="${pageContext.response.locale.language == 'es'}">
			<h2>
				<strong style="color: red;"><jstl:out value="${breachNotification.get('Español')}"/></strong>
			</h2>
		</jstl:if>
		<jstl:if test="${pageContext.response.locale.language == 'en'}">
			<h2>
				<strong style="color: red;"> <jstl:out value="${breachNotification.get('English')}"/></strong>
			</h2>
		</jstl:if>
	</jstl:if>
</security:authorize>

