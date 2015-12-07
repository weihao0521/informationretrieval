<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html  ng-app="myApp" class="ng-cloak">
  <head>  
    <title>AngularJS $http Example</title>  
    <style>
      .username.ng-valid {
          background-color: lightgreen;
      }
      .username.ng-dirty.ng-invalid-required {
          background-color: red;
      }
      .username.ng-dirty.ng-invalid-minlength {
          background-color: yellow;
      }

      .email.ng-valid {
          background-color: lightgreen;
      }
      .email.ng-dirty.ng-invalid-required {
          background-color: red;
      }
      .email.ng-dirty.ng-invalid-email {
          background-color: yellow;
      }

    </style>
    <style type="text/css">.ng-animate.item:not(.left):not(.right){-webkit-transition:0s ease-in-out left;transition:0s ease-in-out left}</style>
    <style type="text/css">@charset "UTF-8";[ng\:cloak],[ng-cloak],[data-ng-cloak],[x-ng-cloak],.ng-cloak,.x-ng-cloak,.ng-hide:not(.ng-hide-animate){display:none !important;}ng\:form{display:block;}</style>
     <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css">
     <link href="<c:url value='/static/css/app.css' />" rel="stylesheet">
     <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap-theme.min.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css">
     <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap-theme.min.css">
     <link href="<c:url value='/static/css/angular-advanced-searchbox.min.css' />" rel="stylesheet">
  </head>
  <body>
      <div class="generic-container" ng-controller="AppController as ctrl">
          <label>Name :</label><input type="text" ng-model="ctrl.name" placeholder="Enter your name"/><br/><br/>  
 
    <span ng-bind="ctrl.message"></span> <span ng-bind="ctrl.name"></span><br/><br/>  
 
    <button ng-click="ctrl.changeMessage()"> Change Message </button>  
    <button ng-click="ctrl.resetMessage()"> Reset Message </button>  
          
      <script src="//code.jquery.com/jquery-2.1.1.min.js"></script>
      <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.5/angular.min.js"></script>
      <script src="https://rawgit.com/angular-ui/bootstrap/gh-pages/ui-bootstrap-tpls-0.13.0.min.js"></script>
      <script src="<c:url value='/static/js/app.js' />"></script>
      
      <!--<script src="<c:url value='/static/js/service/user_service.js' />"></script>-->
      <script src="<c:url value='/static/js/controller/app_controller.js' />"></script>
      <link rel="stylesheet" src="<c:url value='/static/css/angular-advanced-searchbox.min.css'/>">
      
      <script src="<c:url value='/static/js/angular-advanced-searchbox.js'/>"></script>
            <nit-advanced-searchbox
    ng-model="searchParams"
    parameters="availableSearchParams"
          placeholder="Search..." >
      </nit-advanced-searchbox>
   <span ng-bind="ctrl.query"></span><br/><br/>  
  <button class="btn btn-info" ng-click="addPredefinedNameSearchParam()">Add predefined "Name" Search Parameter</button>
  </div>
      </body>
  
</html>