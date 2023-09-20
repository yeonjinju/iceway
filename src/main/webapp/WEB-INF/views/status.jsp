<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>배송 현황</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"/>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css "/>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.5.0/Chart.min.js"></script>
    <!-- 카카오 맵 API를 로드하는 스크립트. -->
    <script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=d60985de854147ffdf3a155671095138"></script>
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
        /* content */
        body, ul, li {margin: 0; padding: 0; list-style: none; font-family: Arial, Helvetica, sans-serif;}
        body {background-color: #eef5fa;}
        a {color: inherit; text-decoration: none;}
        .content {margin-left: 190px; margin-top: 10px; margin-bottom: 10px; width: 87%; height: 790px; background-color: rgb(255, 255, 255); border-radius: 15px; position: absolute; z-index: 2; box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.2);}
        .mainContent {height: 800px; width: 75%; float: left;}
        .alert {height: 800px; width: 25%; float: right; text-align: center;}
        #map {width: 98%; height: 80%; margin: 20px; background-color: white; border-radius: 15px; box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.2);}
        .subcontent {display: flex; height: 95px; margin-left: 20px;}
        .chart {width: 100%; text-align: center; border-radius: 15px; color: #a2a1a1; background-color: #dbedfb; overflow: auto; box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.2);}
        tr {display: flex; justify-content: space-around; align-items: center; height: 36px;}
        th, td {border-radius: 5px; width: 25%; color: #708ebd;}
        th {color: white; background-color: #708ebd;}
        .productTable {width: 98%; margin: 10px;}
        .recallBtn {border: 0; outline: 0; background: #ffffff; border-color: #ffffff; border-radius: 10px; color: #a2a1a1;}
        .blink-red {animation: blink 1s infinite;}
        @keyframes blink {0% {background-color: rgb(248, 177, 177);} 50% {background-color: white;} 100% {background-color: rgb(248, 177, 177);}}
        .consumer {width: 33.3%; height: 100%;}
        .recallBox {width: 100%; height: 57%; margin-left: 10px; text-align: center; border-radius: 15px; color: #a2a1a1; background-color: #dbedfb;}
        .recallBox h4 {color: #707070; font-size: 15px;}
        .recallAlert {width: 100%; height: 58%; border-radius: 15px; color: #a2a1a1; background-color: #dbedfb;box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.2);}
        .recallAlert h4 {color: #708ebd; text-align: center; font-size: 18px; padding-top: 10px;}
        .recallAlert p {font-size: 12px; text-align: center; margin-bottom: 5px;}
        .recallMessagesContainer {max-height: 380px; overflow-y: auto;}
        .recallMessagesContainer ul {display: flex; flex-wrap: wrap; padding-left: 0; list-style: none;}
        .recallMessagesContainer li {cursor: pointer; font-size: 14px; margin-right: 10px; margin-bottom: 10px; width: 350px; height: 53.3px;}
        #recallMessages {padding-left: 20px;}
        .recallMessagesContainer li:hover {background-color: #f2b5bb;}
        .delivery {width: 100%; height: 38%; margin-top: 20px; border-radius: 15px; text-align: center; display: grid; grid-template-columns: repeat(2, 1fr); grid-gap: 10px; grid-row-gap: 10px;}
        .delivery div {border: 1px solid #fdf3f3; padding: 10px; text-align: center; width: 100%; height: 100%; color: #708ebd; background-color: #dbedfb; display: flex; flex-direction: column; align-items: center; justify-content: center; border-radius: 15px; box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.2);}
        .deliveryStatus {font-size: 18px;}
        .number {font-size: 30px; font-weight: bold;}
        /* side-bar */
        .logoIcon {width: 130px; margin-top: 40px; margin-left: 5px;}
        a {color: inherit; text-decoration: none;}
        .side-bar {display: flex; flex-direction: column; position: absolute; width: 250px; height: 790px; z-index: 1; margin-bottom: 20px; margin-left: 15px; padding: 20px; top: 10px; border-radius: 20px;}
        .side-bar > a:first-child {font-size: 10px; margin-bottom: 100px;}
        .side-bar ul li a {display: flex; margin: auto; align-items: center;}
        .side-bar ul li a span {color: #ffffff;}
        .side-bar ul li a svg {margin-bottom: 20px; margin-top: 15px; margin-left: 25px; fill: #584843;}
        .side-bar ul li a p {margin-top: 18px; margin-left: 8px; font-size: 17px; color: #584843; text-shadow: 0px 1px 0.5px #342a27;}
        .historyIcon {width: 210px; border-radius: 30px; background-color: #efeccd; box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.2); margin-left: 0px;}
        .statusIcon {width: 210px; margin-top: 20px; border-radius: 30px; background-color: #efeccd; color: #584843; box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.2);}
        #startButton {background-color: #efeccd; text-align: center; border: none; margin-top: 20px; height: 65px; border-radius: 30px; box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.2);}
        #startButton p {text-align: left; margin-left: 20px; margin-top: 15px; font-size: 18px; color: #584843; text-shadow: 0px 1px 0.5px #342a27;}
        #stopButton {background-color: #efeccd; border: none; margin-top: 20px; height: 65px; border-radius: 30px; box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.2);}
        #stopButton p {text-align: left; margin-left: 20px; margin-top: 15px; font-size: 18px; color: #584843; text-shadow: 0px 1px 0.5px #342a27;}
        #resetButton {background-color: #efeccd; border: none; margin-top: 20px; height: 65px; border-radius: 30px; box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.2);}
        #resetButton p {text-align: left; margin-left: 20px; margin-top: 15px; font-size: 18px; color: #584843; text-shadow: 0px 1px 0.5px #342a27;}
    </style>
</head>
<body>
    <div class="side-bar">
        <a href="status">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 59.39 14.76" class="logoIcon">
                <defs>
                    <linearGradient id="a" x1="19.21" x2="68.61" y1="17.39" y2="17.39" gradientUnits="userSpaceOnUse">
                        <stop offset="0" stop-color="#fff"/>
                        <stop offset="1"/>
                    </linearGradient>
                </defs>
                <path d="m7.87 19.6 2 2a1.11 1.11 0 0 0 1.55 0l5.89-5.9a2.88 2.88 0 0 0 .58-.8 4.63 4.63 0 0 0-.59-4.74 4.59 4.59 0 0 0-4.89-.5 2.12 2.12 0 0 0-.59.44L5.89 16a1.13 1.13 0 0 0 0 1.58Z" style="fill: #584843" transform="translate(-3 -9.13)"/>
                <path d="M.58 13.57a1.37 1.37 0 0 0 1.18 1.19.81.81 0 0 0 .69-.2l3-3-.84-.84-.85-.85-3 3a.78.78 0 0 0-.18.7ZM14.75 13.57a1.37 1.37 0 0 1-1.18 1.19.81.81 0 0 1-.69-.2l-3-3 .84-.84.83-.83 3 3a.76.76 0 0 1 .2.68Z" class="b"/>
                <path d="m6.31 7.45 5.37-5.36M8.05 9.18l5.36-5.36" class="c"/>
                <path d="m13.46 19.6-2 2a1.11 1.11 0 0 1-1.55 0L4 15.69a2.88 2.88 0 0 1-.58-.8A4.63 4.63 0 0 1 4 10.15a4.59 4.59 0 0 1 4.89-.5 2.12 2.12 0 0 1 .59.44L15.44 16a1.13 1.13 0 0 1 0 1.58Z" style="fill: #ff9c92" transform="translate(-3 -9.13)"/>
                <path d="M9.62 7.84 4.26 2.47M7.89 9.57 2.52 4.21" class="c" />
                <path d="M18.88 3.63h1.94l-1.29 7.3h-1.94ZM25.9 4.93c-.12-.24-.4-.36-.85-.36a1.32 1.32 0 0 0-.62.14 1.22 1.22 0 0 0-.46.49 3.67 3.67 0 0 0-.34.87c-.1.35-.19.79-.29 1.31a11 11 0 0 0-.16 1.34 2.17 2.17 0 0 0 .09.79.6.6 0 0 0 .33.38 1.35 1.35 0 0 0 .53.1 2 2 0 0 0 .49-.07.92.92 0 0 0 .43-.27 1.74 1.74 0 0 0 .35-.56 4.06 4.06 0 0 0 .27-.95h1.94a5.16 5.16 0 0 1-.32 1.12 2.69 2.69 0 0 1-.61.93 2.86 2.86 0 0 1-1.05.63 4.84 4.84 0 0 1-1.59.22 4.27 4.27 0 0 1-1.7-.27 1.66 1.66 0 0 1-.86-.76 2.53 2.53 0 0 1-.23-1.2 10.06 10.06 0 0 1 .17-1.53 11.84 11.84 0 0 1 .37-1.53 3.32 3.32 0 0 1 .66-1.2 2.84 2.84 0 0 1 1.13-.79 5 5 0 0 1 1.79-.28 3.91 3.91 0 0 1 1.59.26 1.75 1.75 0 0 1 .79.65 1.66 1.66 0 0 1 .22.85 5.7 5.7 0 0 1-.08.85H26a1.92 1.92 0 0 0-.1-1.16ZM34.69 4.87h-3.44l-.31 1.72h3.24L34 7.76h-3.27l-.35 2H34l-.21 1.21h-5.56l1.28-7.3h5.39ZM37.3 3.63l.12 5.4 2.09-5.4h1.86l.19 5.4 2-5.4h1.86l-3 7.3h-2.15l-.15-5.3-2 5.3h-2.21l-.47-7.3ZM50.3 3.63l1.27 7.3h-2l-.17-1.55h-2.6l-.71 1.55h-2l3.84-7.3Zm-1.06 4.55-.35-3.31-1.51 3.29ZM55.11 6.45 57 3.63h2.1l-3.28 4.42-.5 2.88h-2l.51-2.88-1.72-4.42h2.19Z" class="e"/>
                <text style=" font-size: 10.38px; fill: #a06060; font-family: HelveticaNeue-CondensedBold, Helvetica Neue; font-weight: 700;" transform="matrix(1.3 0 -.17 .98 16.82 10.92)">ICE
                    <tspan x="13.25" y="0" style="letter-spacing: -0.02em">W</tspan>
                    <tspan x="20.96" y="0" style="letter-spacing: -0.05em">A</tspan>
                    <tspan x="26.16" y="0">Y</tspan>
                </text>
            </svg>
        </a>
        <ul>
            <div class="historyIcon">
                <li>
                    <a href="/admin/status">
                        <svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" fill="currentColor" class="bi bi-geo-alt-fill" viewBox="0 0 16 16">
                            <path d="M8 16s6-5.686 6-10A6 6 0 0 0 2 6c0 4.314 6 10 6 10zm0-7a3 3 0 1 1 0-6 3 3 0 0 1 0 6z"/>
                        </svg>
                        <p>STATUS</p>
                    </a>
                </li>
            </div>
            <div class="statusIcon">
                <li>
                    <a href="/admin/history">
                        <svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" fill="currentColor" class="bi bi-cart-fill" viewBox="0 0 16 16">
                            <path d="M0 1.5A.5.5 0 0 1 .5 1H2a.5.5 0 0 1 .485.379L2.89 3H14.5a.5.5 0 0 1 .491.592l-1.5 8A.5.5 0 0 1 13 12H4a.5.5 0 0 1-.491-.408L2.01 3.607 1.61 2H.5a.5.5 0 0 1-.5-.5zM5 12a2 2 0 1 0 0 4 2 2 0 0 0 0-4zm7 0a2 2 0 1 0 0 4 2 2 0 0 0 0-4zm-7 1a1 1 0 1 1 0 2 1 1 0 0 1 0-2zm7 0a1 1 0 1 1 0 2 1 1 0 0 1 0-2z"/>
                        </svg>
                        <p>HISTORY</p>
                    </a>
                </li>
            </div>
        </ul>
        <button id="startButton">
            <p>
                <svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" fill="currentColor" class="bi bi-play-fill" viewBox="0 0 16 16">
                    <path d="m11.596 8.697-6.363 3.692c-.54.313-1.233-.066-1.233-.697V4.308c0-.63.692-1.01 1.233-.696l6.363 3.692a.802.802 0 0 1 0 1.393z"/>
                </svg>
                START
            </p>
        </button>
        <button id="stopButton">
            <p>
                <svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" fill="currentColor" class="bi bi-stop-fill" viewBox="0 0 16 16">
                    <path d="M5 3.5h6A1.5 1.5 0 0 1 12.5 5v6a1.5 1.5 0 0 1-1.5 1.5H5A1.5 1.5 0 0 1 3.5 11V5A1.5 1.5 0 0 1 5 3.5z"/>
                </svg>
                STOP
            </p>
        </button>
        <button id="resetButton">
            <p>
                <svg xmlns="http://www.w3.org/2000/svg" width="26" height="26" fill="currentColor" class="bi bi-arrow-clockwise" viewBox="0 0 16 16">
                    <path fill-rule="evenodd" d="M8 3a5 5 0 1 0 4.546 2.914.5.5 0 0 1 .908-.417A6 6 0 1 1 8 2v1z"/>
                    <path d="M8 4.466V.534a.25.25 0 0 1 .41-.192l2.36 1.966c.12.1.12.284 0 .384L8.41 4.658A.25.25 0 0 1 8 4.466z"/>
                </svg>
                RESET
            </p>
        </button>
    </div>
    <div class="content">
        <div class="alert">
            <div class="recallAlert">
                <!-- recall 알림 -->
                <h4>
                    <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" class="bi bi-exclamation-triangle" viewBox="0 0 16 16">
                        <path d="M7.938 2.016A.13.13 0 0 1 8.002 2a.13.13 0 0 1 .063.016.146.146 0 0 1 .054.057l6.857 11.667c.036.06.035.124.002.183a.163.163 0 0 1-.054.06.116.116 0 0 1-.066.017H1.146a.115.115 0 0 1-.066-.017.163.163 0 0 1-.054-.06.176.176 0 0 1 .002-.183L7.884 2.073a.147.147 0 0 1 .054-.057zm1.044-.45a1.13 1.13 0 0 0-1.96 0L.165 13.233c-.457.778.091 1.767.98 1.767h13.713c.889 0 1.438-.99.98-1.767L8.982 1.566z"/>
                        <path d="M7.002 12a1 1 0 1 1 2 0 1 1 0 0 1-2 0zM7.1 5.995a.905.905 0 1 1 1.8 0l-.35 3.507a.552.552 0 0 1-1.1 0L7.1 5.995z"/>
                    </svg> Recall Warning Notification
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
                        <tbody></tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</body>
<script>
    document.getElementById("startButton").addEventListener("click", function () {
        fetch("/admin/startScheduler", {
            method: "POST",
        })
        .then((response) => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error("Server response wasn't OK");
            }
        })
        .then((data) => console.log(data))
        .catch((error) => console.error("Error:", error));
    });

    document.getElementById("stopButton").addEventListener("click", function () {
        fetch("/admin/stopScheduler", {
            method: "POST",
        })
        .then((response) => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error("Server response wasn't OK");
            }
        })
        .then((data) => console.log(data))
        .catch((error) => console.error("Error:", error));
    });

    document.getElementById("resetButton").addEventListener("click", function () {
        fetch("/admin/resetScheduler", {
            method: "POST", // GET에서 POST로 변경
        }).then((response) => {
            if (response.ok) {
                // 페이지를 새로고침하거나 다른 액션을 취합니다.
                window.location.reload();
            } else {
                // 에러 메시지를 표시합니다.
                alert("Reset 실패");
            }
        });
    });
</script>
</html>