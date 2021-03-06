<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ja">
<head>
	<meta charset="UTF-8">
	<title>日報管理システム</title>
	<link rel="stylesheet" href="<c:url value='/css/reset.css' />">
	<link rel="stylesheet" href="<c:url value='/css/style.css' />">
</head>

<body>
	<div id="wrapper">
		<!-------------------------------------------- header -------------------------------------------->
		<div id="header">
			<div id="header_menu">
				<h1><a href="<c:url value='/login?page=1' />">日報管理システム</a></h1>&nbsp;&nbsp;&nbsp;
				<c:if test="${sessionScope.login_employee != null}">
					<c:if test="${sessionScope.login_employee.admin_flag == 1}">
						<a href="<c:url value='/employees?action=index' />">従業員管理</a>&nbsp;
					</c:if>
					<a href="<c:url value='/reports?action=index' />">日報管理</a>&nbsp;
				</c:if>
			</div><!-- "header_menu" -->
			<%---------- ログインユーザー用の表示 --------%>
			<c:if test="${sessionScope.login_employee != null}">
                 <div id="employee_name">
                     <c:out value="${sessionScope.login_employee.name}" />&nbsp;さん&nbsp;&nbsp;&nbsp;
                     <a href="<c:url value='/login?action=logout' />">ログアウト</a>
                 </div><!-- "employee_name" -->
             </c:if>
		</div><!-- "header" -->
		<!-------------------------------------------------------------------------------------------------->

		<div id="content">${param.content}</div><!-- コンテンツ -->
		<div id="footer">by Tako Kirameki.</div><!-- フッター -->
	</div><!-- "wrapper" -->
</body>

</html>