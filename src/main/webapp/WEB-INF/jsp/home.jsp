<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<base href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/" />
<html lang="en">
<head>
<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
     <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
        <script type="text/javascript">

        function toggleCheckboxDiv(divId) {
          var x = document.getElementById(divId);
          if (x.style.display === "none") {
            x.style.display = "block";
          } else {
            x.style.display = "none";
          }

        }

          // Load the Visualization API and the corechart package.
          google.charts.load('current', {'packages':['corechart']});

          // Set a callback to run when the Google Visualization API is loaded.
          google.charts.setOnLoadCallback(drawChart);

          // Callback that creates and populates a data table,
          // instantiates the pie chart, passes in the data and
          // draws it.
          function drawChart() {

            // Create the data table.
            var data = new google.visualization.DataTable();
            var data1=parseFloat(document.getElementById('overlap').innerHTML);
            var data2=100-data1
            data.addColumn('string', 'Publisher');
            data.addColumn('number', 'Overlap');
            data.addRows([
              ['Overlap', data1],
              ['Others', data2]
            ]);

            // Set chart options
            var options = {'width':300,
                           'height':225};

            // Instantiate and draw our chart, passing in some options.
            var chart = new google.visualization.PieChart(document.getElementById('chart_div'));
            chart.draw(data, options);

            var chart = new google.visualization.PieChart(document.getElementById('chart_div_ESPN'));
            chart.draw(data, options);
          }

          function loadPage(){

            var pub = document.getElementById('pub').innerHTML;
            var overlap,x,y,z;
            z = document.getElementById("time_div");
            if(pub == "espn") {
                    overlap = document.getElementById('overlapESPN').innerHTML;
                    x = document.getElementById("chart_div_ESPN");
                     y = document.getElementById("overlapDivESPN");
                     if(overlap != ""){
                        x.style.display = "block";
                        y.style.display = "block";
                        z.style.display = "block";
                     } else {
                        x.style.display = "none";
                        y.style.display = "none";
                        z.style.display = "none";
                     }

             } else  if(pub == "cnbc") {
                     overlap = document.getElementById('overlap').innerHTML;
                    x = document.getElementById("chart_div");
                     y = document.getElementById("overlapDiv");
                     if(overlap != ""){
                        x.style.display = "block";
                        y.style.display = "block";
                        z.style.display = "block";
                     } else {
                        x.style.display = "none";
                        y.style.display = "none";
                        z.style.display = "none";
                     }
             } else {
             x = document.getElementById("chart_div");
                      y = document.getElementById("overlapDiv");
              x.style.display = "none";
                        y.style.display = "none";
                        z.style.display = "none";
                        x = document.getElementById("chart_div_ESPN");
                          y = document.getElementById("overlapDivESPN");
                  x.style.display = "none";
                                y.style.display = "none";
                                z.style.display = "none";
             }

          }
        </script>


	<!-- Access the bootstrap Css like this,
		Spring boot will handle the resource mapping automcatically -->
	<link rel="stylesheet" type="text/css" href="webjars/bootstrap/3.3.7/css/bootstrap.min.css" />

	<!-- 
	<spring:url value="/css/main.css" var="springCss" />
	<link href="${springCss}" rel="stylesheet" />
	 -->
	<c:url value="/css/main.css" var="jstlCss" />
	<link href="${jstlCss}" rel="stylesheet" />

</head>
<body onload="loadPage()">

	<nav class="navbar navbar-inverse">
		<div class="container">
			<div class="navbar-header">
				<a class="navbar-brand" href="#">Audience Merchandising</a>
			</div>
			<div id="navbar" class="collapse navbar-collapse">
				<ul class="nav navbar-nav">
					<li class="active"><a href="#">Home</a></li>
					<li><a href="#about">About</a></li>
				</ul>
			</div>
		</div>
	</nav>

	<div class="container" >

		<div class="starter-template">

		<form:form action="/" method="post" modelAttribute="publisher">
		<table>
                                			<tr><td>
			<img src="images/nike.png" width=150 height=75/> </td><td><h1>Audience Mall</h1></td></tr></table>

<br><b><font size="3">Total Profiles:</b> 100,000</font><br><br>
			<b><font size="4">Select Attributes</font></b>
			<table>
                        			<tr><td width=380>
             <form:label path="session" value="selected"/>
             <b><font size="3">Session Duration:</font></b> </td> <td>
                                                <form:label path="region"   value="selected"/>
                                                <b><font size="3">Region: </font></b></td>
             </tr><tr height=90><td ><div id="session-threshold" > <form:radiobutton path="timeRadio" value="min"/> Minimum Duration Spent in any one day: <form:input path="sessionTime" id="duration" size="4"/> Min.  <br>
             <form:radiobutton path="timeRadio" value="avg"/> Avg. Duration Spent per day: <form:input path="period" id="period" size="4"/> Min.</div></td>

             <td ><div id="region-area"  >
             <form:select name="state" id="state" class="form-control" multiple="multiple" path="regionList">
             <form:options items="${publisher.countries}"/>
             </form:select></div>

</td>
			</tr></table>

			<h2>Available Publishers</h2>
			<table ><tr height="226">
			<td><form:radiobutton value="cnbc" path="name"/>
                                 <a href="publisher"><img src="images/cnbc.png" width=160 height=120/></a> </td>

                <td><div id="overlapDiv" style="display:none">
                <b>Estimated Nike Audience: </b>${publisher.overlap}<br>
                <b>Estimated Nike Audience%: <label id="overlap">${publisher.overlapPercent}</label>%</b><br>
Time taken to compute: ${publisher.time}
                </div></td>
                <td><div id="chart_div"  style="display:none"></div></td>
			</tr><tr height="226">
			<td><form:radiobutton value="espn" path="name"/>
			            <img src="images/espn.jpg" width=150 height=130/></td>
			  <td><div id="overlapDivESPN" style="display:none">
                            <b>Estimated Nike Audience: </b>${publisher.overlap}<br>
                            <b>Estimated Nike Audience%: <label id="overlapESPN">${publisher.overlapPercent}</label>%</b><br>
Time taken to compute: ${publisher.time}
                            </div>                            </td>
                            <td><div id="chart_div_ESPN"  style="display:none"></div></td></tr>
			</table>
			<label id="pub" style="display:none">${publisher.name}</label>
			<br><br>
            <input type="submit" value="Submit" /><br>
            <div id="time_div" style="display:none"></div>
        </form:form>

		</div>

	</div>
	
	<script type="text/javascript" src="webjars/bootstrap/3.3.7/js/bootstrap.min.js"></script>

</body>

</html>