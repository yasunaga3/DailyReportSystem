<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="../layout/app.jsp">
	<c:param name="content">
		<%---------------------------- フラッシュ ----------------------------%>
		<c:if test="${flush != null}">
            <div id="flush_success"><c:out value="${flush}" /></div>
        </c:if>
        <%----------------------- 従業員一覧テーブル -----------------------%>
		<h2>従業員　一覧</h2>
        <table id="employee_list">
        	<tbody>
        		<tr><%------------------ 見出し行 -------------------%>
        			<th>社員番号</th><th>氏名</th><th>操作</th>
        		</tr>
        		<%------------------ 一覧表 -------------------%>
				<c:forEach var="employee" items="${employees}" varStatus="status">
					<tr class="row${status.count % 2}">
						<td><c:out value="${employee.code}" /></td>
				            <td><c:out value="${employee.name}" /></td>
				            <td>
				            	<c:choose>
				            		<c:when test="${employee.delete_flag == 1}">
				            			（削除済み）
				            		</c:when>
					            	<c:otherwise>
					            		<a href="<c:url value='/employees?action=show&id=${employee.id}' />">詳細を表示</a>
					            	</c:otherwise>
				            	</c:choose>
				            </td>
					</tr>
				</c:forEach>
        	</tbody>
        </table>

	  <%-------------------------- ページネーション -------------------------%>
	  <%---- 属性   item_count=日報リストの総件数,  link=リンク先URL ----%>
		<jsp:include page="../layout/pagenation.jsp">
		  <jsp:param name="item_count" value="${employees_count}" />
		  <jsp:param name="link" value="/employees" />
		</jsp:include>

		<!-------------------------- リンク先の指定 ------------------------->
		<p><a href="<c:url value='/employees?action=new' />">新規従業員の登録</a></p>
	</c:param>
</c:import>