<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ICEWAY</title>
    <link rel="stylesheet" href="/header.css">
    <link rel="stylesheet" href="/status.css">
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
        <style>
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

            a {
            color: inherit;
            text-decoration: none;
            }
        </style>
<body>
	<%@ include file="sidebar.jsp"%>
    <div class="content">
        <div class="alert">
            <div class="recallAlert">
                <!-- recall 알림 -->
                <h4>
                    <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" class="bi bi-exclamation-triangle" viewBox="0 0 16 16">
                      <path d="M7.938 2.016A.13.13 0 0 1 8.002 2a.13.13 0 0 1 .063.016.146.146 0 0 1 .054.057l6.857 11.667c.036.06.035.124.002.183a.163.163 0 0 1-.054.06.116.116 0 0 1-.066.017H1.146a.115.115 0 0 1-.066-.017.163.163 0 0 1-.054-.06.176.176 0 0 1 .002-.183L7.884 2.073a.147.147 0 0 1 .054-.057zm1.044-.45a1.13 1.13 0 0 0-1.96 0L.165 13.233c-.457.778.091 1.767.98 1.767h13.713c.889 0 1.438-.99.98-1.767L8.982 1.566z"/>
                      <path d="M7.002 12a1 1 0 1 1 2 0 1 1 0 0 1-2 0zM7.1 5.995a.905.905 0 1 1 1.8 0l-.35 3.507a.552.552 0 0 1-1.1 0L7.1 5.995z"/>
                    </svg>
                    Recall Warning Notification
                  </h4>
                <div class="recallMessagesContainer">
                    <ul id="recallMessages">
                        <!-- 이곳에 메시지가 추가됩니다. -->
                    </ul>
                </div>
            </div>
            <div class="delivery">
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