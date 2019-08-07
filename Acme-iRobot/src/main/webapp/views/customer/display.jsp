<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<security:authorize access="hasRole('CUSTOMER')">
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
				<jstl:out value="${customer.name}" />
			</div><br/>

			<div>
				<strong><spring:message code="actor.surname" />: </strong>
				<jstl:out value="${customer.surname}" />
			</div><br/>

			<div>
				<strong><spring:message code="actor.email" />: </strong>
				<jstl:out value="${customer.email}" />
			</div><br/>

			<div>
				<strong><spring:message code="actor.phone" />: </strong>
				<jstl:out value="${customer.phoneNumber}" />
			</div><br/>

			<div>
				<strong><spring:message code="actor.address" />: </strong>
				<jstl:out value="${customer.address}" />
			</div><br/>
			
			<div>
				<strong><spring:message code="actor.VATNumber" />: </strong>
				<jstl:out value="${customer.VATNumber}" />
			</div><br/>
		</div>

		<div style="float: right;">
			<img style="width: 200px; height: 200px" src="${customer.photo}"
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
					<jstl:out value="${customer.creditCard.holder}" />
				</div><br/>
	
				<div>
					<strong><spring:message code="mp.cc.make" />: </strong>
					<jstl:out value="${customer.creditCard.make}" />
				</div><br/>
	
				<div>
					<strong><spring:message code="mp.cc.number" />: </strong>
					<jstl:out value="${customer.creditCard.number}" />
				</div><br/>
	
				<div>
					<strong><spring:message code="mp.cc.expirationMonth" />: </strong>
					<jstl:out value="${customer.creditCard.expirationMonth}" />
				</div><br/>
	
				<div>
					<strong><spring:message code="mp.cc.expirationYear" />: </strong>
					<jstl:out value="${customer.creditCard.expirationYear}" />
				</div><br/>
				
				<div>
					<strong><spring:message code="mp.cc.CVV" />: </strong>
					<jstl:out value="${customer.creditCard.CVV}" />
				</div><br/>
			</div>
	
		</fieldset>
	</jstl:if>
	<br>
	<input type="button" name="back" value="<spring:message code="mp.edit" />" onclick="javascript: relativeRedir('/customer/edit.do')" />&nbsp;
	
	<input type="button" name="back" value="<spring:message code="mp.back" />" onclick="window.history.back()" />&nbsp;
	<br>
</security:authorize>