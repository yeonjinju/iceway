<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>로그인</title>
    <style>
      body {background-color: #fafafa; font-family: Arial, sans-serif;}
      .container {width: 350px; margin: 80px auto; background-color: #fff; border: 1px solid #e6e6e6; text-align: center; box-shadow: 0 0 5px rgba(0, 0, 0, 0.1); border-radius: 5px;}
      .logoIcon {width: 200px; margin-top: 20px;}
      input[type="text"], input[type="password"] {display: block; margin: 10px auto; width: 80%; padding: 14px; background-color: #fafafa; border: 1px solid #dbdbdb; border-radius: 5px; font-size: 14px;}
      button {margin: 10px auto; padding: 10px; width: 80%; background-color: #0095f6; border: none; color: #fff; font-weight: 600; border-radius: 5px; cursor: pointer;}
      button:disabled {background-color: #b2dffc;}
    </style>
  </head>
  <body>
    <div class="container">
      <a href="#">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 59.39 14.76" class="logoIcon">
          <defs>
            <linearGradient id="a" x1="19.21" x2="68.61" y1="17.39" y2="17.39" gradientUnits="userSpaceOnUse">
              <stop offset="0" stop-color="#fff" />
              <stop offset="1" />
            </linearGradient>
          </defs>
          <path d="m7.87 19.6 2 2a1.11 1.11 0 0 0 1.55 0l5.89-5.9a2.88 2.88 0 0 0 .58-.8 4.63 4.63 0 0 0-.59-4.74 4.59 4.59 0 0 0-4.89-.5 2.12 2.12 0 0 0-.59.44L5.89 16a1.13 1.13 0 0 0 0 1.58Z" style="fill: #584843" transform="translate(-3 -9.13)"/>
          <path d="M.58 13.57a1.37 1.37 0 0 0 1.18 1.19.81.81 0 0 0 .69-.2l3-3-.84-.84-.85-.85-3 3a.78.78 0 0 0-.18.7ZM14.75 13.57a1.37 1.37 0 0 1-1.18 1.19.81.81 0 0 1-.69-.2l-3-3 .84-.84.83-.83 3 3a.76.76 0 0 1 .2.68Z" class="b"/>
          <path d="m6.31 7.45 5.37-5.36M8.05 9.18l5.36-5.36" class="c" />
          <path d="m13.46 19.6-2 2a1.11 1.11 0 0 1-1.55 0L4 15.69a2.88 2.88 0 0 1-.58-.8A4.63 4.63 0 0 1 4 10.15a4.59 4.59 0 0 1 4.89-.5 2.12 2.12 0 0 1 .59.44L15.44 16a1.13 1.13 0 0 1 0 1.58Z" style="fill: #ff9c92" transform="translate(-3 -9.13)"/>
          <path d="M9.62 7.84 4.26 2.47M7.89 9.57 2.52 4.21" class="c" />
          <path d="M18.88 3.63h1.94l-1.29 7.3h-1.94ZM25.9 4.93c-.12-.24-.4-.36-.85-.36a1.32 1.32 0 0 0-.62.14 1.22 1.22 0 0 0-.46.49 3.67 3.67 0 0 0-.34.87c-.1.35-.19.79-.29 1.31a11 11 0 0 0-.16 1.34 2.17 2.17 0 0 0 .09.79.6.6 0 0 0 .33.38 1.35 1.35 0 0 0 .53.1 2 2 0 0 0 .49-.07.92.92 0 0 0 .43-.27 1.74 1.74 0 0 0 .35-.56 4.06 4.06 0 0 0 .27-.95h1.94a5.16 5.16 0 0 1-.32 1.12 2.69 2.69 0 0 1-.61.93 2.86 2.86 0 0 1-1.05.63 4.84 4.84 0 0 1-1.59.22 4.27 4.27 0 0 1-1.7-.27 1.66 1.66 0 0 1-.86-.76 2.53 2.53 0 0 1-.23-1.2 10.06 10.06 0 0 1 .17-1.53 11.84 11.84 0 0 1 .37-1.53 3.32 3.32 0 0 1 .66-1.2 2.84 2.84 0 0 1 1.13-.79 5 5 0 0 1 1.79-.28 3.91 3.91 0 0 1 1.59.26 1.75 1.75 0 0 1 .79.65 1.66 1.66 0 0 1 .22.85 5.7 5.7 0 0 1-.08.85H26a1.92 1.92 0 0 0-.1-1.16ZM34.69 4.87h-3.44l-.31 1.72h3.24L34 7.76h-3.27l-.35 2H34l-.21 1.21h-5.56l1.28-7.3h5.39ZM37.3 3.63l.12 5.4 2.09-5.4h1.86l.19 5.4 2-5.4h1.86l-3 7.3h-2.15l-.15-5.3-2 5.3h-2.21l-.47-7.3ZM50.3 3.63l1.27 7.3h-2l-.17-1.55h-2.6l-.71 1.55h-2l3.84-7.3Zm-1.06 4.55-.35-3.31-1.51 3.29ZM55.11 6.45 57 3.63h2.1l-3.28 4.42-.5 2.88h-2l.51-2.88-1.72-4.42h2.19Z" class="e"/>
          <text style="font-size: 10.38px; fill: #a06060; font-family: HelveticaNeue-CondensedBold, Helvetica Neue; font-weight: 700;" transform="matrix(1.3 0 -.17 .98 16.82 10.92)">ICE
            <tspan x="13.25" y="0" style="letter-spacing: -0.02em">W</tspan>
            <tspan x="20.96" y="0" style="letter-spacing: -0.05em">A</tspan>
            <tspan x="26.16" y="0">Y</tspan>
          </text>
        </svg>
      </a>
      <form action="/users/login" method="post">
        <input type="text" name="userName" placeholder="사용자명" required />
        <input type="password" name="password" placeholder="비밀번호" required/>
        <button type="submit">로그인</button>
      </form>
    </div>
  </body>
</html>
