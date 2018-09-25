<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/WEB-INF/views/layout/app.jsp">

	<c:param name="content">
		<c:choose>
			<%----------- 日報情報がある場合は編集フォームを表示する ----------%>
			<c:when test="${report != null}">
				<h2>日報　編集ページ</h2>
				<form method="POST" action="<c:url value='/reports?action=update' />">
                    <c:import url="_form.jsp" />
                </form>
			</c:when>

			<%------------------- 日報情報がない場合 -----------------%>
			<c:otherwise>
                <h2>お探しのデータは見つかりませんでした。</h2>
            </c:otherwise>
		</c:choose>

		<%------------------- ReportsIndexServletへリンク -----------------%>
		<p><a href="<c:url value='/reports?action=index' />">一覧に戻る</a></p>
	</c:param>

</c:import>