<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>   
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>로그인 폼</title>
  <style>
    /* 전체 페이지 배경 */
    body {
      background-color: #fafafa;
      font-family: Arial, sans-serif;
    }

    /* 컨테이너 */
    .container {
      width: 350px;
      margin: 80px auto;
      background-color: #fff;
      border: 1px solid #e6e6e6;
      text-align: center;
      box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
      border-radius: 5px;
    }

    /* 로고 */
    .logo {
      margin: 22px auto 12px;
      font-size: 40px;
      font-weight: bold;
      color: #333;
    }

    /* 입력 폼 스타일 */
    input[type="text"],
    input[type="password"] {
      display: block;
      margin: 10px auto;
      width: 80%;
      padding: 14px;
      background-color: #fafafa;
      border: 1px solid #dbdbdb;
      border-radius: 5px;
      font-size: 14px;
    }

    /* 버튼 스타일 */
    button {
      margin: 10px auto;
      padding: 10px;
      width: 80%;
      background-color: #0095f6;
      border: none;
      color: #fff;
      font-weight: 600;
      border-radius: 5px;
      cursor: pointer;
    }

    button:disabled {
      background-color: #b2dffc;
    }

  </style>
</head>
<body>
  <div class="container">
    <div class="logo">IceWay</div>
    <form action="/users/login" method="post">
      <input type="text" name="userName" placeholder="사용자명" required />
      <input type="password" name="password" placeholder="비밀번호" required />
      <button type="submit">로그인</button>
    </form>
  </div>
</body>
</html>
