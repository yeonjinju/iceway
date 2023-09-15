/**
 * 맵을 이동시키는 함수입니다.
 * @param {Number} lat - 위도 변화량
 * @param {Number} lng - 경도 변화량
 */
const moveMap = (lat, lng) => {
  let currentCenter = map.getCenter();
  let newCenter = new kakao.maps.LatLng(
    currentCenter.getLat() + lat,
    currentCenter.getLng() + lng
  );
  map.panTo(newCenter);
};

/**
 * 맵의 레벨을 변경하는 함수입니다.
 * @param {Number} levelChange - 레벨 변화량
 */
const changeMapLevel = (levelChange) => {
  let currentLevel = map.getLevel();
  map.setLevel(currentLevel + levelChange);
};

// 키보드 이벤트 리스너
document.addEventListener("keydown", (event) => {
  switch (event.key) {
    case "ArrowUp":
      moveMap(0.001, 0);
      break;
    case "ArrowDown":
      moveMap(-0.001, 0);
      break;
    case "ArrowLeft":
      moveMap(0, -0.001);
      break;
    case "ArrowRight":
      moveMap(0, 0.001);
      break;
    case "+":
    case "=":
      changeMapLevel(-1);
      break;
    case "-":
      changeMapLevel(1);
      break;
    case "Enter":
      clearTable();
      map.setCenter(new kakao.maps.LatLng(37.507194, 127.022783));
      map.setLevel(6);
      break;
    default:
      return;
  }
});
