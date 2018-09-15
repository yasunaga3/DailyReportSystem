<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%----------------- ページネーション用パラメータの取得 -------------------%>
<% int itemCount = Integer.parseInt(request.getParameter("item_count"));                // 情報リストの総件数
	   int items_per_page = Integer.parseInt((String)application.getAttribute("items")); // 1ページ当たりの表示件数の取得
	   int endItem = ((itemCount - 1) / items_per_page) + 1;                                            // ループ処理の終了条件となる数値
%>

<%----------------------------- ページネーション ---------------------------%>
<div id="pagination">
    （全 itemCount 件）<br />
    <c:forEach var="i" begin="1" end="<%=endItem  %>" step="1">
        <c:choose>
        	<%---------- 現行ページの場合はリンクは不要 ---------%>
            <c:when test="${i == page}">
                <c:out value="${i}" />&nbsp;
            </c:when>
            <%-------- 現行ページ以外の場合はリンクを貼る --------%>
            <c:otherwise>
                <a href="<c:url value='${link}?page=${i}' />"><c:out value="${i}" /></a>&nbsp;
            </c:otherwise>
        </c:choose>
    </c:forEach>
</div>
