// MarkerEvent.js

// WebSocket 클라이언트와 마커 정보를 관리할 객체 선언
let stompClient = null; // WebSocket 클라이언트
let markers = {}; // 마커 객체를 저장할 객체
let map; // 카카오 맵 객체
let requestDataExecuted = false; // 서버로 데이터 요청을 한 번만 실행하기 위한 플래그 변수
let selectedMarkerData = null; // 마커 클릭 시 데이터를 저장할 객체
let selectedMarker = null; // 마커 클릭 시 클릭한 마커의 id값을 저장할 객체
let recallSelected = null; // 알림창 클릭 시 클릭한 알림의 id값을 저장할 객체

// WebSocket 연결 및 데이터 요청 함수
let connectAndRequestData = () => {
  let socket = new SockJS("/ws"); // WebSocket 연결을 위한 객체 생성
  stompClient = Stomp.over(socket); // Stomp.js를 사용하여 WebSocket 클라이언트 생성
  // WebSocket 연결 성공 시 실행되는 콜백 함수
  stompClient.connect({}, (frame) => {
    // '/topic/location' 주제를 구독하여 서버로부터 데이터 수신 시 실행되는 콜백 함수
    stompClient.subscribe("/topic/location", (response) => {
      let data = JSON.parse(response.body);
      // 배열형식의 데이터 처리
      updateMarkersFromArrayOfObjects(data);
      recallUpdate(data);
      dataSave(data);
      // 데이터가 업데이트될 때마다 selectedMarkerData 업데이트
      if (data.length > 0) {
        selectedMarkerData = data[selectedMarker - 1];
        displayMarkerInfo(selectedMarkerData); // 업데이트된 데이터로 정보 표시
      }
    });
    // requestDataExecuted 변수를 체크하여 한 번만 서버에 데이터 요청
    if (!requestDataExecuted) {
      requestDataExecuted = true;
      requestServerData();
    }
  });
};

// 배열 형식 데이터로부터 마커 업데이트 함수
let updateMarkersFromArrayOfObjects = (markerDataArray) => {
  if (!Array.isArray(markerDataArray)) return;
  for (let i = 0; i < markerDataArray.length; i++) {
    let newMarkerData = markerDataArray[i];
    if (!newMarkerData) continue;
    let delivery_id = newMarkerData.id;
    if (markers[delivery_id]) {
      markers[delivery_id].setMap(null); // 기존 마커 삭제
    }

    let markerColor = "blue"; // 기본 마커 색 (예: 파란색)
    // 배송 상태에 따라 마커 색 변경
    if (newMarkerData.delivery_status == 1) {
      markerColor = "black"; // 배송완료일 경우 검은색으로 변경
    }
    let marker = new kakao.maps.Marker({
      map: map,
      position: new kakao.maps.LatLng(
        newMarkerData.latitude,
        newMarkerData.longitude
      ),
      clickable: true, // 마커를 클릭했을 때 지도의 클릭 이벤트가 발생하지 않도록 설정합니다
      image: new kakao.maps.MarkerImage(
        `/images/${markerColor}.png`, // 마커 색에 따른 이미지 경로
        new kakao.maps.Size(30, 30) // 마커 이미지 크기 설정
      ),
    });
    // 마커에 클릭이벤트를 등록
    kakao.maps.event.addListener(marker, "click", function () {
      selectedMarkerData = newMarkerData; // 클릭한 마커의 데이터 저장
      selectedMarker = selectedMarkerData.id;
      recallSelected = selectedMarkerData.id;
      // 테이블에서 모든 행을 제거합니다.
      let tbody = document.querySelector(".productTable tbody");
      tbody.innerHTML = "";
      // 테이블을 만듭니다.
      displayMarkerInfo(newMarkerData);
    });
    // 새로운 마커 정보 저장
    markers[delivery_id] = marker;
  }
};

// 이미 추가된 id를 저장하는 배열
const addedIds = [];

// 연진
let recallUpdate = (data) => {
  const recallMessages = document.getElementById("recallMessages");

  // 기존에 추가된 id 목록을 복사합니다.
  const addedIdsCopy = [...addedIds];

  for (let i = 0; i < data.length; i++) {
    // data.id 및 data.recall_id가 모두 존재하는 경우
    if (data[i].id && data[i].recall_id) {
      // data[i].id가 아직 추가되지 않은 경우
      if (!addedIds.includes(data[i].id)) {
        const newMessage = document.createElement("li");
        newMessage.className = "alert alert-danger"; // 클래스 추가
        newMessage.setAttribute("role", "alert"); // role 속성 추가
        newMessage.innerHTML = `
                    ${data[i].recall_name}님 상품 온도 상승 알림
                `;
        recallMessages.appendChild(newMessage);

        addedIds.push(data[i].id);

        newMessage.addEventListener("click", function () {
          // 클릭 이벤트 핸들러 내에서 사용할 데이터를 별도의 변수에 저장
          let currentData = data[i];
          // selectedMarker, recallSelected 객체에 클릭한 id 데이터를 저장
          selectedMarker = currentData.id;
          recallSelected = currentData.id;
          let clickedMarker = markers[recallSelected];
          // 클릭한 데이터의 위도와 경도를 markers 배열에 있는 값을 가져와서 출력
          let latitude = clickedMarker.getPosition().getLat();
          let longitude = clickedMarker.getPosition().getLng();
          // latitude, longitude 값이 존재할경우
          if (latitude !== undefined && longitude !== undefined) {
            // clickedMarker 안에 있는 markers객체의 위도, 경도 값으로 .setCenter(이동) 시킴
            map.setCenter(new kakao.maps.LatLng(latitude, longitude));
            // 3레벨로 지도를 확대
            map.setLevel(3);
            // 클릭한 데이터를 테이블의 tbody로 추가
            displayMarkerInfo(currentData);
          }
        });
      }
    } else {
      // recall_id가 없거나 id가 없는 경우 해당 항목이 삭제된 것으로 간주하여 추가된 메시지를 삭제
      // addedIdsCopy 배열에서 해당 항목의 인덱스를 찾기
      const removedIndex = addedIdsCopy.indexOf(data[i].id);

      // 만약 해당 항목이 addedIdsCopy 배열에 존재한다면 (즉, 이전에 추가된 메시지라면)
      if (removedIndex !== -1) {
        // 추가된 메시지를 화면에서 제거
        const removedMessage = recallMessages.children[removedIndex];
        recallMessages.removeChild(removedMessage);
        // addedIds 배열에서도 해당 항목을 제거합니다.
        addedIds.splice(removedIndex, 1);
      }
    }
  }
};

// 테이블을 추가하는 함수
function displayMarkerInfo(markerData) {
  // 데이터가 유효하지 않은 경우 또는 id가 없는 경우
  if (
    !markerData ||
    markerData.id === null ||
    markerData.temperature === null
  ) {
    return;
  }
  // 테이블의 tbody를 선택
  let tbody = document.querySelector(".productTable tbody");

  // 새로운 행을 생성합니다.
  let newRow = document.createElement("tr");

  // 각 셀을 생성하고 데이터를 설정
  let idCell = document.createElement("td");
  idCell.textContent = markerData.customers_name;
  let nameCell = document.createElement("td");
  nameCell.textContent = markerData.product_name;
  let tempCell = document.createElement("td");
  tempCell.textContent = markerData.temperature;
  let buttonCell = document.createElement("td");

  // 버튼 요소를 생성
  let buttonElement = document.createElement("button");
  buttonElement.textContent = "회수"; // 버튼 텍스트 설정
  buttonElement.classList.add("recallBtn");
  if (markerData.temperature >= -10) {
    buttonElement.classList.add("blink-red");
    buttonElement.textContent = "회수 요망";
  }

  // 버튼을 셀에 추가
  buttonCell.appendChild(buttonElement);

  // 버튼 클릭 이벤트 리스너 추가
  buttonElement.addEventListener("click", function () {
    requestRecallUpdate();
    clearTable();
    map.setCenter(new kakao.maps.LatLng(37.50732, 127.03390));
    map.setLevel(6);
  });

  // 행에 각 셀을 추가
  newRow.appendChild(idCell);
  newRow.appendChild(nameCell);
  newRow.appendChild(tempCell);
  newRow.appendChild(buttonCell);

  // tbody에 새로운 행을 추가
  tbody.innerHTML = ""; // 기존 내용을 모두 지우고
  tbody.appendChild(newRow); // 새로운 데이터를 추가
}

// 테이블을 삭제하는 함수
function clearTable() {
  selectedMarker = null;
  selectedMarkerData = null;
  let tbody = document.querySelector(".productTable tbody");
  tbody.innerHTML = ""; // 테이블 내용을 모두 지웁니다.
}

// 데이터를 저장할 객체 초기화
let deliveryStatusTrue = null;
let deliveryStatusFalse = null;
let recallStatus = null;
let recallTotal = null;

function dataSave(data) {
  deliveryStatusTrue = 0;
  deliveryStatusFalse = 0;
  recallStatus = 0;
  recallTotal = 0;

  data.forEach((item) => {
    if (item.delivery_status === true) {
      deliveryStatusTrue++;
    } else if (item.delivery_status === false) {
      deliveryStatusFalse++;
    }
  });
  data.forEach((item) => {
    if (item.recall_status === true) {
      recallStatus++;
    } else if (item.recall_id !== null) {
      recallTotal++;
    }
  });
  // 숫자를 문자열로 변환하여 할당
  document.getElementById("delivery-in-transit").textContent =
    String(deliveryStatusFalse);
  document.getElementById("delivery-delivered").textContent =
    String(deliveryStatusTrue);
  document.getElementById("delivery-recall-warning").textContent =
    String(recallTotal);
  document.getElementById("delivery-recall-complete").textContent =
    String(recallStatus);

  return [deliveryStatusTrue, deliveryStatusFalse, recallStatus, recallTotal];
}

// 문서 로딩 완료 후 실행
document.addEventListener("DOMContentLoaded", () => {
  let mapContainer = document.getElementById("map");
  let mapOption = {
    center: new kakao.maps.LatLng(37.50732, 127.03390),
    level: 6,
  };
  map = new kakao.maps.Map(mapContainer, mapOption);
  connectAndRequestData(); // WebSocket 연결 및 데이터 요청
});

// 서버로 데이터 요청 함수
let requestServerData = () => {
  stompClient.send("/app/requestData", {}, JSON.stringify({}));
};

// 서버에 리콜 상태를 업데이트 요청 함수
let requestRecallUpdate = () => {
  stompClient.send(
    "/app/requestRecall",
    {},
    JSON.stringify({ recallSelected })
  );
};
