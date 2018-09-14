<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%----  itemCount：日報リストの総件数 ------------------------------%>
<%----- items_per_page：1ページ当たりの表示件数の取得 ------------%>
<%----- endItem：ループ処理の終了条件となる数値 -------------------%>
<% int itemCount = Integer.parseInt(request.getParameter("item_count"));
		int items_per_page = Integer.parseInt((String)application.getAttribute("items"));
		int endItem = ((itemCount - 1) / items_per_page) + 1;
%>

<%----------------------------- ページネーション ---------------------------%>
<div id="pagination">
    （全 itemCount 件）<br />
    <c:forEach var="i" begin="1" end="<%=endItem  %>" step="1">
        <c:choose>
            <c:when test="${i == page}">
                <c:out value="${i}" />&nbsp;
            </c:when>
            <c:otherwise>
                <a href="<c:url value='${link}?page=${i}' />"><c:out value="${i}" /></a>&nbsp;
            </c:otherwise>
        </c:choose>
    </c:forEach>
</div>
