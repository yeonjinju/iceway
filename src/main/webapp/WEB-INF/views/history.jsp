<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<title>ICEWAY</title>
<link rel="stylesheet" href="/header.css">
<link rel="stylesheet" href="/sidebar.css">
<link rel="stylesheet" href="/history.css">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css"
	rel="stylesheet">
</head>
	
	<style>
		#startButton{
			display: none;
		}
		#stopButton{
			display: none;
		}
		#resetButton{
			display: none;	
		}
		.historyIcon{
			background-color: #efeccd;
		}
		.statusIcon{
			background-color: #e5dc78;
		}

		body,
		ul,
		li {
			margin: 0;
			padding: 0;
			list-style: none;
			font-family: Arial, Helvetica, sans-serif;
			}

		body {
			background-color: #eef5fa;
			}

		.container{
			margin-left: 170px;
			margin-top: 10px;
			margin-bottom:10px;
			width: 87%;
			height: 770px;
			background-color: #ffffff;
			border-radius: 15px;
			position: absolute;
			z-index: 2;
		}

		a {
  			color: inherit;
  			text-decoration: none;
			}
	</style>
<body>
	<%@ include file="sidebar.jsp"%>
	<div class="container">
		<div class="productStatus">
			<div class="filter">
				<form action="admin/history" method="get">
					<p>
						<div class="result">
							<div class="inputIcon">
								<select name="condition" id="condition">
									<option value="">선택</option>
									<option value="name" ${strArray[1] eq 'name' ? 'selected' : ''}>이름</option>
									<option value="delivery_time"
										${strArray[1] eq 'delivery_time' ? 'selected' : ''}>배송 날짜</option>
									<option value="arrival_time"
										${strArray[1] eq 'arrival_time' ? 'selected' : ''}>도착 날짜</option>
								</select> 
								<button type="submit">
									<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-search" viewBox="0 0 16 16">
					  					<path d="M11.742 10.344a6.5 6.5 0 1 0-1.397 1.398h-.001c.03.04.062.078.098.115l3.85 3.85a1 1 0 0 0 1.415-1.414l-3.85-3.85a1.007 1.007 0 0 0-.115-.1zM12 6.5a5.5 5.5 0 1 1-11 0 5.5 5.5 0 0 1 11 0z"/>
									</svg>                    
								</button>
								<input type="text" name="keyword"  value="${strArray[0]}" />
								<button type="submit" class="noneDisplay"></button>
							</div>
						</div>
					</p>
			</form>
			<div class="refreshBtn">
				<a href="history"><svg
				  xmlns="http://www.w3.org/2000/svg" width="22" height="22"
				  fill="currentColor" class="bi bi-arrow-clockwise"
				  viewBox="0 0 16 16">
				  <path fill-rule="evenodd"
					d="M8 3a5 5 0 1 0 4.546 2.914.5.5 0 0 1 .908-.417A6 6 0 1 1 8 2v1z" />
				  <path
					d="M8 4.466V.534a.25.25 0 0 1 .41-.192l2.36 1.966c.12.1.12.284 0 .384L8.41 4.658A.25.25 0 0 1 8 4.466z" />
				  </svg>
				</a> 
				<c:if test="${not empty strArray[1]}">
				  <p>
					<strong>${intArray[4]}</strong> 개의 자료가 검색되었습니다.
				  </p>
				</c:if>
			  </div>
			</div>
		<table>
			<thead>
				<tr class="tableTitle">
					<th>번호</th>
					<th>고객명</th>
					<th>배송 상태</th>
					<th>리콜 유무</th>
					<th>배송 시간</th>
					<th>도착 시간</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="i" items="${list}" varStatus="loop">
					<tr class='tableContent'>
						<td>${i.id}</td>
						<td>${i.name}</td>
						<td>${i.delivery_status ? "배송완료" : "배송중"}</td>
						<td>${i.recall_status ? recallArray[loop.index] : "-"}</td>
						<td>${i.delivery_time}</td>
						<td>${i.arrival_time}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
			<div class="pagination">
				<ul>
					<c:if test="${intArray[1] ne 1}">
						<li><a
							href="admin/history?pageNum=${intArray[1]-1}&condition=${strArray[1]}&keyword=${strArray[0]}">prev</a></li>
					</c:if>
					<c:forEach var="i" begin="${intArray[1]}" end="${intArray[2]}">
						<li class="${intArray[0] eq i ? 'active' : ''}"><a
							href="admin/history?pageNum=${i}&condition=${strArray[1]}&keyword=${strArray[0]}">${i}</a></li>
					</c:forEach>
					<c:if test="${intArray[2] lt intArray[3]}">
						<li><a
							href="admin/history?pageNum=${intArray[2]+1}&condition=${strArray[1]}&keyword=${strArray[0]}">next</a></li>
					</c:if>
				</ul>
			</div>
		</div>
	</div>
</body>
</html>