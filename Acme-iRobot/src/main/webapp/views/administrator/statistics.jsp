<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<security:authorize access="hasRole('ADMIN')">

<jstl:choose>
	<jstl:when test="${errMsg ne null}">
		<p>
			<jstl:out value="${errMsg}"/>
		</p>
	</jstl:when>
	<jstl:otherwise>
		<h1><spring:message code="administrator.statistics" /></h1>	
			<table class="displayStyle" style="width:650px; line-height: 30px; font-size: 18px; " >
			
					<tr><td style="text-align: justify; font-size: 22px;font-weight: bold; ">
						<spring:message code="administrator.statsIRobotsPerScientist" />
					</td></tr>
					<jstl:choose>
						<jstl:when test="${statsIRobotsPerScientist[0] ne null}">
							<tr>
								<td style=""><spring:message code="administrator.statsIRobotsPerScientist.max" /></td>
								<td> ${statsIRobotsPerScientist[0]}</td>
							</tr>
							
							<tr>
								<td><spring:message code="administrator.statsIRobotsPerScientist.min" /></td>
								<td> ${statsIRobotsPerScientist[1]}</td>
							</tr>
							
							<tr>
								<td><spring:message code="administrator.statsIRobotsPerScientist.avg" /></td>
								<td> ${statsIRobotsPerScientist[2]}</td>
							</tr>
							
							<tr>
								<td><spring:message code="administrator.statsIRobotsPerScientist.stddev" /></td>
								<td> ${statsIRobotsPerScientist[3]}</td>
							</tr>
						</jstl:when>
						<jstl:otherwise>
						<tr><td><br></td></tr>
							<tr>
								<td><spring:message code="administrator.statistics.noData" /></td>
							</tr>
						</jstl:otherwise>
					</jstl:choose>
		
					<tr><td><br></td></tr>
					
					<tr><td style="text-align: justify; font-size: 22px;font-weight: bold; ">
						<spring:message code="administrator.top10ScientistByPurchases" />
					</td></tr>
					<jstl:choose>
						<jstl:when test="${not empty top10ScientistByPurchases}">
							<tr>
								<td> 
									<jstl:set var="cont" value="1"/>
									<jstl:forEach items="${top10ScientistByPurchases}" var="i" >
										<jstl:out value="${cont}"/>. 
										<jstl:out value="${i.name } ${i.surname}"/> <br>
										<jstl:set var="cont" value="${cont + 1}"/>
									</jstl:forEach>
								</td>
							</tr>
						</jstl:when>
						<jstl:otherwise>
						<tr><td><br></td></tr>
							<tr>
								<td><spring:message code="administrator.statistics.noData" /></td>
							</tr>
						</jstl:otherwise>
					</jstl:choose>
					
					<tr><td><br></td></tr>
					
					<tr><td style="text-align: justify; font-size: 22px;font-weight: bold; ">
						<spring:message code="administrator.top10BestSellingIRobots" />
					</td></tr>
					<jstl:choose>
						<jstl:when test="${not empty top10BestSellingIRobots}">
							<tr>
								<td> 
									<jstl:set var="cont" value="1"/>
									<jstl:forEach items="${top10BestSellingIRobots}" var="i" >
										<jstl:out value="${cont}"/>. 
										<jstl:out value="${i.title}"/> <br>
										<jstl:set var="cont" value="${cont + 1}"/>
									</jstl:forEach>
								</td>
							</tr>
						</jstl:when>
						<jstl:otherwise>
						<tr><td><br></td></tr>
							<tr>
								<td><spring:message code="administrator.statistics.noData" /></td>
							</tr>
						</jstl:otherwise>
					</jstl:choose>
					
					<tr><td><br></td></tr>
					
					<tr><td style="text-align: justify; font-size: 22px;font-weight: bold; ">
						<spring:message code="administrator.top10IRobotByFinders" />
					</td></tr>
					<jstl:choose>
						<jstl:when test="${not empty top10IRobotByFinders}">
							<tr>
								<td> 
									<jstl:set var="cont" value="1"/>
									<jstl:forEach items="${top10IRobotByFinders}" var="i" >
										<jstl:out value="${cont}"/>. 
										<jstl:out value="${i.title}"/> <br>
										<jstl:set var="cont" value="${cont + 1}"/>
									</jstl:forEach>
								</td>
							</tr>
						</jstl:when>
						<jstl:otherwise>
						<tr><td><br></td></tr>
							<tr>
								<td><spring:message code="administrator.statistics.noData" /></td>
							</tr>
						</jstl:otherwise>
					</jstl:choose>
					
					<tr><td><br></td></tr>
					
					<tr><td style="text-align: justify; font-size: 22px;font-weight: bold; ">
						<spring:message code="administrator.bottom10IRobotByFinders" />
					</td></tr>
					<jstl:choose>
						<jstl:when test="${not empty bottom10IRobotByFinders}">
							<tr>
								<td> 
									<jstl:set var="cont" value="1"/>
									<jstl:forEach items="${bottom10IRobotByFinders}" var="i" >
										<jstl:out value="${cont}"/>. 
										<jstl:out value="${i.title}"/> <br>
										<jstl:set var="cont" value="${cont + 1}"/>
									</jstl:forEach>
								</td>
							</tr>
						</jstl:when>
						<jstl:otherwise>
						<tr><td><br></td></tr>
							<tr>
								<td><spring:message code="administrator.statistics.noData" /></td>
							</tr>
						</jstl:otherwise>
					</jstl:choose>
					
					<tr><td><br></td></tr>
					
					<jstl:choose>
						<jstl:when test="${averageResultsPerFinder ne null}">
							<tr>
								<td style=""><strong><spring:message code="administrator.averageResultsPerFinder" /></strong></td>
								<td> ${averageResultsPerFinder}</td>
							</tr>
						</jstl:when>
						<jstl:otherwise>
						<tr><td><br></td></tr>
							<tr>
								<td><spring:message code="administrator.statistics.noData" /></td>
							</tr>
						</jstl:otherwise>
					</jstl:choose>
					
				</table>
		</jstl:otherwise>
	</jstl:choose>
</security:authorize>