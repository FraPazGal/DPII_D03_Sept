<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<security:authorize access="permitAll">
<jstl:choose>
	<jstl:when test="${isPrincipal}">
		<!-- Actor Attributes -->
		<h1><spring:message	code="actor.view" /></h1>
		<fieldset style="width: 35%">
			<legend style="font-size: 21px">
				<spring:message code="actor.personalData" />
			</legend>
			<br>
			<div style="float: left;">
				<div>
					<strong><spring:message code="actor.name" />: </strong>
					<jstl:out value="${scientist.name}" />
				</div><br/>

				<div>
					<strong><spring:message code="actor.surname" />: </strong>
					<jstl:out value="${scientist.surname}" />
				</div><br/>

				<div>
					<strong><spring:message code="actor.email" />: </strong>
					<jstl:out value="${scientist.email}" />
				</div><br/>

				<div>
					<strong><spring:message code="actor.phone" />: </strong>
					<jstl:out value="${scientist.phoneNumber}" />
				</div><br/>

				<div>
					<strong><spring:message code="actor.address" />: </strong>
					<jstl:out value="${scientist.address}" />
				</div><br/>
				
				<div>
					<strong><spring:message code="actor.VATNumber" />: </strong>
					<jstl:out value="${scientist.VATNumber}" />
				</div><br/>
			</div>

			<div style="float: right;">
				<img style="width: 200px; height: 200px" src="${scientist.photo}"
					alt="User photo">
			</div>
		</fieldset>
		
		<jstl:if test="${customer.creditCard ne null }">
		
			<br><br>
			<fieldset style="width: 20%">
				<legend style="font-size: 21px">
					<spring:message code="mp.cc.creditCard" />
				</legend>
				<br>
				<div style="float: left;">
					<div>
						<strong><spring:message code="mp.cc.holder" />: </strong>
						<jstl:out value="${scientist.creditCard.holder}" />
					</div><br/>
	
					<div>
						<strong><spring:message code="mp.cc.make" />: </strong>
						<jstl:out value="${scientist.creditCard.make}" />
					</div><br/>
	
					<div>
						<strong><spring:message code="mp.cc.number" />: </strong>
						<jstl:out value="${scientist.creditCard.number}" />
					</div><br/>
	
					<div>
						<strong><spring:message code="mp.cc.expirationMonth" />: </strong>
						<jstl:out value="${scientist.creditCard.expirationMonth}" />
					</div><br/>
	
					<div>
						<strong><spring:message code="mp.cc.expirationYear" />: </strong>
						<jstl:out value="${scientist.creditCard.expirationYear}" />
					</div><br/>
					
					<div>
						<strong><spring:message code="mp.cc.CVV" />: </strong>
						<jstl:out value="${scientist.creditCard.CVV}" />
					</div><br/>
				</div>
	
			</fieldset>
		</jstl:if>
		<br>
	</jstl:when>
	<jstl:otherwise>
		<!-- Actor Attributes -->
		<br><br>
		<fieldset style="width: 35%">
			<legend style="font-size: 21px">
				<spring:message code="actor.personalData" />
			</legend>
			<br>
			<div style="float: left;">
				<div>
					<strong><spring:message code="actor.name" />: </strong>
					<jstl:out value="${scientist.name}" />
				</div><br/>

				<div>
					<strong><spring:message code="actor.surname" />: </strong>
					<jstl:out value="${scientist.surname}" />
				</div><br/>

				<div>
					<strong><spring:message code="actor.email" />: </strong>
					<jstl:out value="${scientist.email}" />
				</div><br/>

				<div>
					<strong><spring:message code="actor.phone" />: </strong>
					<jstl:out value="${scientist.phoneNumber}" />
				</div><br/>

				<div>
					<strong><spring:message code="actor.address" />: </strong>
					<jstl:out value="${scientist.address}" />
				</div><br>
				
				<div>
					<strong><spring:message code="actor.VATNumber" />: </strong>
					<jstl:out value="${scientist.VATNumber}" />
				</div><br/>
			</div>

			<div style="float: right;">
				<img style="width: 200px; height: 200px" src="${scientist.photo}"
					alt="User photo">
			</div>

		</fieldset>
		<br>
	</jstl:otherwise>
</jstl:choose>

	<jstl:if test="${not empty iRobots}">
		<h2>
			<strong><spring:message code="irobot.list" /></strong>
		</h2>
	
		<display:table style="width: 80%" class="displaytag"
			name="iRobots" pagesize="5"
			requestURI="scientist/display.do?scientistId=${scientist.id}"
			id="irobot">
	
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
	
		</display:table>
	</jstl:if>

	<jstl:if test="${isPrincipal }">
		<input type="button" name="edit" value="<spring:message code="mp.edit" />" onclick="javascript: relativeRedir('/scientist/edit.do')" />&nbsp;
	</jstl:if>
	<input type="button" name="back" value="<spring:message code="mp.back" />" onclick="window.history.back()" />
	<br>
</security:authorize>