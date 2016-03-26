<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"  "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Error</title>
	</head>
	<body>
		<center>
			<h2>Sorry, the email was not sent because of the following error:</h2>
			<h3>${exception.message}</h3>
		</center>
		<ul>
		    <li>Exception: <c:out value="${requestScope['javax.servlet.error.exception']}" /></li>
		    <li>Exception type: <c:out value="${requestScope['javax.servlet.error.exception_type']}" /></li>
		    <li>Exception message: <c:out value="${requestScope['javax.servlet.error.message']}" /></li>
		    <li>Request URI: <c:out value="${requestScope['javax.servlet.error.request_uri']}" /></li>
		    <li>Servlet name: <c:out value="${requestScope['javax.servlet.error.servlet_name']}" /></li>
		    <li>Status code: <c:out value="${requestScope['javax.servlet.error.status_code']}" /></li>
	    	<li>Stack trace: 
	    		<c:forEach var="trace" items="${pageContext.exception.stackTrace}">
					<p>${trace}</p>
				</c:forEach>
			</li>
		</ul>
	</body>
</html>