<%-- 
    Document   : ProfileRepresent
    Created on : Dec 10, 2015, 11:51:29 PM
    Author     : william
--%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body ng-app="myApp" >
        <div ng-controller="AppController as ctrl">
                      <span ng-bind="searchParams.city"></span><br/><br/>  
      <span ng-bind="list"></span><br/><br/> 
      <span>{{list}}</span><br/><br/> 
        </div>
        <h1>Hello World!</h1>
    </body>
</html>
