<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html >
    <head>
        <style type="text/css">.ng-animate.item:not(.left):not(.right){-webkit-transition:0s ease-in-out left;transition:0s ease-in-out left}</style><style type="text/css">@charset "UTF-8";[ng\:cloak],[ng-cloak],[data-ng-cloak],[x-ng-cloak],.ng-cloak,.x-ng-cloak,.ng-hide:not(.ng-hide-animate){display:none !important;}ng\:form{display:block;}</style>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="A directive for AngularJS providing a advanced visual search box">
    <meta name="author" content="Daniel Nauck">
    <link rel="icon" href="favicon.ico">

    <title>Angular Advanced Searchbox</title>

    <!-- Bootstrap core CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap-theme.min.css">

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css">

    <link rel="stylesheet" src="<c:url value='/static/css/angular-advanced-searchbox.min.css'/>">


    <script src="//code.jquery.com/jquery-2.1.1.min.js"></script><style type="text/css"></style>
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.15/angular.min.js"></script>
    <script src="https://rawgit.com/angular-ui/bootstrap/gh-pages/ui-bootstrap-tpls-0.13.0.min.js"></script>
    <script src="<c:url value='/static/js/angular-advanced-searchbox.js'/>"></script>


    <script type="text/javascript">
      var app = angular.module('myApp', ['ui.bootstrap', 'angular-advanced-searchbox']);


      app.controller('AppController', function($scope) {
        $scope.availableSearchParams = [
          { key: "name", name: "Name", placeholder: "Name..." },
          { key: "city", name: "City", placeholder: "City..." },
          { key: "country", name: "Country", placeholder: "Country..." },
          { key: "emailAddress", name: "E-Mail", placeholder: "E-Mail..." },
          { key: "job", name: "Job", placeholder: "Job..." }
        ];

        $scope.addPredefinedNameSearchParam = function(){
          $scope.searchParams.name = 'Max Mustermann';
        };

        $scope.loadPredefinedSearchParamSet = function(){
          $scope.searchParams = {
            name: "Max M.",
            job: "Boss"
          };
        };
      });
    </script>
  <style id="style-1-cropbar-clipper">/* Copyright 2014 Evernote Corporation. All rights reserved. */
.en-markup-crop-options {
    top: 18px !important;
    left: 50% !important;
    margin-left: -100px !important;
    width: 200px !important;
    border: 2px rgba(255,255,255,.38) solid !important;
    border-radius: 4px !important;
}

.en-markup-crop-options div div:first-of-type {
    margin-left: 0px !important;
}
</style></head>

  <body ng-app="myApp" lang="en" class="ng-scope"><div id="StayFocusd-infobar" style="display: none; top: 268px;">
    <img src="chrome-extension://laankejkbhbdhmipfmgcngdelahlfoji/common/img/eye_19x19_red.png">
    <span id="StayFocusd-infobar-msg"></span>
    <span id="StayFocusd-infobar-links">
        <a id="StayFocusd-infobar-never-show">hide forever</a>&nbsp;&nbsp;|&nbsp;&nbsp;
        <a id="StayFocusd-infobar-hide">hide once</a>
    </span>
</div>

    <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="#">Angular Advanced Searchbox</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">

        </div><!--/.navbar-collapse -->
      </div>
    </nav>

    <div class="container ng-scope" ng-controller="AppController">
      <div class="jumbotron">

        <h1>Angular Advanced Searchbox</h1>
        <p>A directive for AngularJS providing a advanced visual search box.</p>
        <p>
          <a class="btn btn-primary btn-lg" href="https://github.com/dnauck/angular-advanced-searchbox/archive/master.zip" role="button"><span class="fa fa-download"></span> Download</a>
          <a class="btn btn-default btn-lg" href="https://github.com/dnauck/angular-advanced-searchbox" role="button"><span class="fa fa-github"></span> Github</a>
        </p>

      </div>

      <div class="row">
        <div class="col-sm-12">
          <h2>Demo</h2>
          <p>Click on a suggested search parameter or use autocompletion feature and press 'Enter' to add new search parameter to your query. Use 'TAB', 'SHIFT+TAB', 'LEFT', 'RIGHT' or 'BACKSPACE' to navigate between search parameters.</p>
          <div class="advancedSearchBox ng-isolate-scope ng-valid" ng-class="{active:focus}" ng-init="focus = false" ng-click="!focus ? setSearchFocus = true : null" ng-model="searchParams" parameters="availableSearchParams" placeholder="Search...">
    <span ng-show="searchParams.length < 1 &amp;&amp; searchQuery.length === 0" class="search-icon glyphicon glyphicon-search ng-hide"></span>
    <a ng-href="" ng-show="searchParams.length > 0 || searchQuery.length > 0" ng-click="removeAll()" role="button" class="">
        <span class="remove-all-icon glyphicon glyphicon-trash"></span>
    </a>
    <div>
        <!-- ngRepeat: searchParam in searchParams --><div class="search-parameter ng-scope" ng-repeat="searchParam in searchParams">
            <a ng-href="" ng-click="removeSearchParam($index)" role="button">
                <span class="remove glyphicon glyphicon-trash"></span>
            </a>
            <div class="key ng-binding">Name:</div>
            <div class="value">
                <!-- ngIf: !searchParam.editMode --><span ng-if="!searchParam.editMode" ng-click="enterEditMode($index)" class="ng-binding ng-scope">Max Mustermann</span><!-- end ngIf: !searchParam.editMode -->
                <!-- ngIf: searchParam.editMode -->
            </div>
        </div><!-- end ngRepeat: searchParam in searchParams --><div class="search-parameter ng-scope" ng-repeat="searchParam in searchParams">
            <a ng-href="" ng-click="removeSearchParam($index)" role="button">
                <span class="remove glyphicon glyphicon-trash"></span>
            </a>
            <div class="key ng-binding">Job:</div>
            <div class="value">
                <!-- ngIf: !searchParam.editMode --><span ng-if="!searchParam.editMode" ng-click="enterEditMode($index)" class="ng-binding ng-scope">Boss</span><!-- end ngIf: !searchParam.editMode -->
                <!-- ngIf: searchParam.editMode -->
            </div>
        </div><!-- end ngRepeat: searchParam in searchParams -->
        <input name="searchbox" class="search-parameter-input ng-valid ng-isolate-scope ng-touched ng-dirty ng-valid-parse" type="text" nit-auto-size-input="" nit-set-focus="setSearchFocus" ng-keydown="keydown($event)" placeholder="Search..." ng-focus="focus = true" ng-blur="focus = false" typeahead-on-select="typeaheadOnSelect($item, $model, $label)" typeahead="parameter as parameter.name for parameter in parameters | filter:isUnsedParameter | filter:{name:$viewValue} | limitTo:8" ng-change="searchQueryChanged(searchQuery)" ng-model="searchQuery" aria-autocomplete="list" aria-expanded="false" aria-owns="typeahead-5-2043" style="max-width: 1120px; width: 37px;"><ul class="dropdown-menu ng-isolate-scope ng-hide" ng-show="isOpen()" ng-style="{top: position.top+'px', left: position.left+'px'}" style="display: block; top: 24px; left: 9px;" role="listbox" aria-hidden="true" typeahead-popup="" id="typeahead-5-2043" matches="matches" active="activeIdx" select="select(activeIdx)" query="query" position="position">
    <!-- ngRepeat: match in matches track by $index -->
</ul>
    </div>
    <div class="search-parameter-suggestions ng-hide" ng-show="parameters &amp;&amp; focus">
        <span class="title">Parameter Suggestions:</span>
        <!-- ngRepeat: param in parameters | filter:isUnsedParameter | limitTo:8 --><span class="search-parameter ng-binding ng-scope" ng-repeat="param in parameters | filter:isUnsedParameter | limitTo:8" ng-mousedown="addSearchParam(param)">City</span><!-- end ngRepeat: param in parameters | filter:isUnsedParameter | limitTo:8 --><span class="search-parameter ng-binding ng-scope" ng-repeat="param in parameters | filter:isUnsedParameter | limitTo:8" ng-mousedown="addSearchParam(param)">Country</span><!-- end ngRepeat: param in parameters | filter:isUnsedParameter | limitTo:8 --><span class="search-parameter ng-binding ng-scope" ng-repeat="param in parameters | filter:isUnsedParameter | limitTo:8" ng-mousedown="addSearchParam(param)">E-Mail</span><!-- end ngRepeat: param in parameters | filter:isUnsedParameter | limitTo:8 -->
    </div>
</div>
          <p>
            <strong>Output:</strong>
            </p><pre><code class="ng-binding">{"name":"Max Mustermann","job":"Boss"}</code></pre>
            The output model could be directly used as params object for Angular's $http API.
          <p></p>
          <p>
            <strong>Test:</strong> loading predefined search parameters via code:<br>
            <button class="btn btn-info" ng-click="addPredefinedNameSearchParam()">Add predefined "Name" Search Parameter</button>
            <button class="btn btn-info" ng-click="loadPredefinedSearchParamSet()">Load predefined Search Parameter Set</button>
          </p>
        </div>
        <div class="col-sm-12">
          <h2>Getting started</h2>
          <p>Define the available search parameters in your controller's code:</p>
          <p>
            </p><pre><code>$scope.availableSearchParams = [
  { key: "name", name: "Name", placeholder: "Name..." },
  { key: "city", name: "City", placeholder: "City..." },
  { key: "country", name: "Country", placeholder: "Country..." },
  { key: "emailAddress", name: "E-Mail", placeholder: "E-Mail..." },
  { key: "job", name: "Job", placeholder: "Job..." }
];</code></pre>
          <p></p>
          <p>Add the following AngularJS directive to your HTML:</p>
          <p>
            </p><pre><code>&lt;nit-advanced-searchbox ng-model="searchParams" parameters="availableSearchParams" placeholder="Search..."&gt;&lt;/nit-advanced-searchbox&gt;</code></pre>
          <p></p>
          <p><a class="btn btn-default" href="https://github.com/dnauck/angular-advanced-searchbox/blob/master/README.md" role="button">View Readme »</a></p>
       </div>
      </div>

      <hr>

      <footer>
        <p>© Nauck IT KG 2014, 2015</p>
      </footer>
    </div> <!-- /container -->
  

<div style="position: fixed; top: -9999px; left: 0px;"><span style="white-space: pre; font-size: 14px; font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif; font-weight: 400; font-style: normal; letter-spacing: 0px; text-transform: none; word-spacing: 0px; text-indent: 0px; box-sizing: border-box; border-left-width: 0px; border-right-width: 0px; border-left-style: none; border-right-style: none; padding-left: 0px; padding-right: 0px; margin-left: 0px; margin-right: 0px;">asdf</span></div><div style="position: fixed; top: -9999px; left: 0px;"><span style="white-space: pre; font-size: 14px; font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif; font-weight: 400; font-style: normal; letter-spacing: 0px; text-transform: none; word-spacing: 0px; text-indent: 0px; box-sizing: border-box; border-left-width: 2px; border-right-width: 2px; border-left-style: inset; border-right-style: inset; padding-left: 1px; padding-right: 1px; margin-left: 0px; margin-right: 0px;">Name...</span></div><div style="position: fixed; top: -9999px; left: 0px;"><span style="white-space: pre; font-size: 14px; font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif; font-weight: 400; font-style: normal; letter-spacing: 0px; text-transform: none; word-spacing: 0px; text-indent: 0px; box-sizing: border-box; border-left-width: 2px; border-right-width: 2px; border-left-style: inset; border-right-style: inset; padding-left: 1px; padding-right: 1px; margin-left: 0px; margin-right: 0px;">?????</span></div><div style="position: fixed; top: -9999px; left: 0px;"><span style="white-space: pre; font-size: 14px; font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif; font-weight: 400; font-style: normal; letter-spacing: 0px; text-transform: none; word-spacing: 0px; text-indent: 0px; box-sizing: border-box; border-left-width: 2px; border-right-width: 2px; border-left-style: inset; border-right-style: inset; padding-left: 1px; padding-right: 1px; margin-left: 0px; margin-right: 0px;">?????</span></div><div style="position: fixed; top: -9999px; left: 0px;"><span style="white-space: pre; font-size: 14px; font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif; font-weight: 400; font-style: normal; letter-spacing: 0px; text-transform: none; word-spacing: 0px; text-indent: 0px; box-sizing: border-box; border-left-width: 2px; border-right-width: 2px; border-left-style: inset; border-right-style: inset; padding-left: 1px; padding-right: 1px; margin-left: 0px; margin-right: 0px;">?????</span></div><div style="position: fixed; top: -9999px; left: 0px;"><span style="white-space: pre; font-size: 14px; font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif; font-weight: 400; font-style: normal; letter-spacing: 0px; text-transform: none; word-spacing: 0px; text-indent: 0px; box-sizing: border-box; border-left-width: 2px; border-right-width: 2px; border-left-style: inset; border-right-style: inset; padding-left: 1px; padding-right: 1px; margin-left: 0px; margin-right: 0px;">?????</span></div><div style="position: fixed; top: -9999px; left: 0px;"><span style="white-space: pre; font-size: 14px; font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif; font-weight: 400; font-style: normal; letter-spacing: 0px; text-transform: none; word-spacing: 0px; text-indent: 0px; box-sizing: border-box; border-left-width: 2px; border-right-width: 2px; border-left-style: inset; border-right-style: inset; padding-left: 1px; padding-right: 1px; margin-left: 0px; margin-right: 0px;">?????</span></div></body></html>