<ul>
  <li>
    <a href="/history">
      <svg
        xmlns="http://www.w3.org/2000/svg"
        width="30"
        height="30"
        fill="currentColor"
        class="bi bi-cart-fill"
        viewBox="0 0 16 16"
      >
        <path
          d="M0 1.5A.5.5 0 0 1 .5 1H2a.5.5 0 0 1 .485.379L2.89 3H14.5a.5.5 0 0 1 .491.592l-1.5 8A.5.5 0 0 1 13 12H4a.5.5 0 0 1-.491-.408L2.01 3.607 1.61 2H.5a.5.5 0 0 1-.5-.5zM5 12a2 2 0 1 0 0 4 2 2 0 0 0 0-4zm7 0a2 2 0 1 0 0 4 2 2 0 0 0 0-4zm-7 1a1 1 0 1 1 0 2 1 1 0 0 1 0-2zm7 0a1 1 0 1 1 0 2 1 1 0 0 1 0-2z"
        />
      </svg>
    </a>
  </li>
  <li>
    <a href="/status">
      <svg
        xmlns="http://www.w3.org/2000/svg"
        width="30"
        height="30"
        fill="currentColor"
        class="bi bi-geo-alt-fill"
        viewBox="0 0 16 16"
      >
        <path
          d="M8 16s6-5.686 6-10A6 6 0 0 0 2 6c0 4.314 6 10 6 10zm0-7a3 3 0 1 1 0-6 3 3 0 0 1 0 6z"
        />
      </svg>
    </a>
  </li>
</ul>
<button id="startButton">Start</button>
<button id="stopButton">Stop</button>
<button id="resetButton">Reset</button>
<script>
  document.getElementById("startButton").addEventListener("click", function () {
    fetch("/startScheduler", {
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
    fetch("/stopScheduler", {
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
    fetch("/resetScheduler", {
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
