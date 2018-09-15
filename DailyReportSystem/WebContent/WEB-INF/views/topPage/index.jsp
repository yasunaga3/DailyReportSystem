<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:import url="../layout/app.jsp">

    <c:param name="content">
    	<%------------------ フラッシュの表示 -----------------%>
        <c:if test="${flush != null}">
     		<div id="flush_success"><c:out value="${flush}" /></div>
        </c:if>

		<%------------- 自分の日報一覧テーブルの表示 -----------%>
        <h2>日報管理システムへようこそ</h2>
        <h3>【自分の日報　一覧】</h3>
        <table id="report_list">
        	<tbody>
        		<tr><!------- テーブルタイトル行 -------->
                    <th class="report_name">氏名</th>
                    <th class="report_date">日付</th>
                    <th class="report_title">タイトル</th>
                    <th class="report_action">操作</th>
                </tr><!-------------------------------->
                <c:forEach var="report" items="${reports}" varStatus="status">
                    <tr class="row${status.count % 2}">
                        <td class="report_name"><c:out value="${report.employee.name}" /></td>
                        <td class="report_date"><fmt:formatDate value='${report.report_date}' pattern='yyyy-MM-dd' /></td>
                        <td class="report_title">${report.title}</td>
                        <td class="report_action"><a href="<c:url value='/reports/show?id=${report.id}' />">詳細を見る</a></td>
                    </tr>
                </c:forEach>
        	</tbody>
        </table>

        <%-------------------------- ページネーション --------------------------%>
        <%---- 属性   item_count=日報リストの総件数,  link=リンク先URL -----%>
		<jsp:include page="../layout/pagenation.jsp">
		    <jsp:param name="item_count" value="${reports_count}" />
		    <jsp:param name="link" value="/login" />
		</jsp:include>

        <%------------- 新規日報登録へのリンク -----------%>
        <p><a href="<c:url value='/reports/new' />">新規日報の登録</a></p>
    </c:param>

</c:import>