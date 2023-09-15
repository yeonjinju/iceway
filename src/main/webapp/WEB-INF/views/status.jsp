<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <link rel="stylesheet" href="/status.css">
    <link rel="stylesheet" href="/header.css">
    <link rel="stylesheet" href="/sidebar.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css ">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.5.0/Chart.min.js"></script>
    <!-- 카카오 맵 API를 로드하는 스크립트. -->
    <script type="text/javascript"
        src="//dapi.kakao.com/v2/maps/sdk.js?appkey=d60985de854147ffdf3a155671095138"></script>
    <!-- jQuery 라이브러리를 로드하는 스크립트. -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <!-- stomp.js 라이브러리를 로드하는 스크립트. -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
    <!-- sockjs-client 라이브러리를 로드하는 스크립트. -->
    <script src="/webjars/sockjs-client/1.5.1/sockjs.min.js"></script>
    <!-- MarkerEvent.js 파일을 로드하는 스크립트.(지도 위의 마커 이벤트 처리) -->
    <script src="/MarkerEvent.js"></script>
    <!-- MoveEvent.js 파일을 로드하는 스크립트.(지도 이동과 관련된 이벤트 처리에 관련된 코드) -->
    <script src="/MoveEvent.js"></script>
    
        
<body>
    <div class="header">
		<%@ include file="header.jsp"%>
	</div>
	<div class="side-bar">
		<%@ include file="sidebar.jsp"%>
	</div>
    <div class="content">
        <div class="alert">
            <div class="recallAlert">
                <!-- recall 알림 -->
                <h4>Recall Warning Notification</h4>
                <div class="recallMessagesContainer">
                    <ul id="recallMessages">
                        <!-- 이곳에 메시지가 추가됩니다. -->
                    </ul>
                </div>
            </div>
            <div class="delivery">
                <!-- <canvas id="doughnut-chart" width="200px" height="120px"></canvas> -->
                <div>
                    <p class="deliveryStatus">배송중</p>
                    <p id="delivery-in-transit" class="number"></p>
                </div>
                <div>
                    <p class="deliveryStatus">배송완료</p>
                    <p id="delivery-delivered" class="number"></p>
                </div>
                <div>
                    <p class="deliveryStatus">회수경고</p>
                    <p id="delivery-recall-warning" class="number"></p>
                </div>
                <div>
                    <p class="deliveryStatus">회수 완료</p>
                    <p id="delivery-recall-complete" class="number"></p>
                </div>
            </div>
            
        </div>
        <div class="mainContent">
            <!-- 지도를 표시할 div 입니다 -->
            <div id="map"></div>
            <div class="subcontent">
                <div class="chart">
                    <table class="productTable">
                        <thead>
                            <tr>
                                <td>주문자명</td>
                                <td>상품명</td>
                                <td>온도</td>
                                <td>회수버튼</td>
                            </tr>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</body>
</html>