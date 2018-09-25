<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:import url="/WEB-INF/views/layout/app.jsp">

	<c:param name="content">
		<%------------------------- フラッシュ ------------------------%>
        <c:if test="${flush != null}">
            <div id="flush_success"><c:out value="${flush}" /></div>
        </c:if>

        <%------------------------- 日報一覧表 ------------------------%>
        <h2>日報　一覧</h2>
        <table id="report_list">
			<tbody>
				<tr>
                    <th class="report_name">氏名</th>
                    <th class="report_date">日付</th>
                    <th class="report_title">タイトル</th>
                    <th class="report_action">操作</th>
                </tr>
                <c:forEach var="report" items="${reports}" varStatus="status">
                    <tr class="row${status.count % 2}">
                        <td class="report_name"><c:out value="${report.employee.name}" /></td>
                        <td class="report_date"><fmt:formatDate value='${report.report_date}' pattern='yyyy-MM-dd' /></td>
                        <td class="report_title">${report.title}</td>
                        <td class="report_action"><a href="<c:url value='/reports?action=show&id=${report.id}' />">詳細を見る</a></td>
                    </tr>
                </c:forEach>
			</tbody>
        </table>


        <%-------------------------- ページネーション --------------------------%>
        <%---- 属性   item_count=日報リストの総件数,  link=リンク先URL -----%>
		<jsp:include page="../layout/pagenation.jsp">
		    <jsp:param name="item_count" value="${reports_count}" />
		    <jsp:param name="link" value="/reports" />
		</jsp:include>


        <%------------- 新規日報登録へのリンク -----------%>
        <p><a href="<c:url value='/reports?action=new' />">新規日報の登録</a></p>
	</c:param>

</c:import>