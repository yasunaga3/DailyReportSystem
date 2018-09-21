<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%---------- 削除確認用スクリプト ----------%>
<script>
	function confirmDestroy(){
		if(confirm("本当に削除してよろしいですか？")){ document.forms[1].submit(); }
	}
</script>

<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
    	<%------------------------- 従業員情報編集・削除用フォーム -----------------------%>
    	<c:choose>
    		<%--------- 従業員情報ありの場合 ---------%>
    		<c:when test="${employee != null}">
    			<h2>id : ${employee.id} の従業員情報　編集ページ</h2>
    			<p>（パスワードは変更する場合のみ入力してください）</p>
    			<%--------- 従業員情報編集用フォーム ---------%>
    			<form method="post" action="<c:url value='/employees?action=update' />">
    				<c:import url="_form.jsp" />
    			</form>
    			<%--------- 従業員情報削除用フォーム ---------%>
    			<p><a href="#" onclick="confirmDestroy();">この従業員情報を削除する</a></p>
    			<form method="post" action="<c:url value='/employees?action=destroy' />">
					<input type="hidden" name="_token" value="${_token}" />
    			</form>
    		</c:when>
    		<%--------- 従業員情報なしの場合 ---------%>
    		<c:otherwise>
    			<h2>お探しのデータは見つかりませんでした。</h2>
    		</c:otherwise>
    	</c:choose>
    	<%--------------------- 従業員情報編集・削除用フォームEND --------------------%>

    	<%---------- 一覧画面へのリンク ----------%>
    	<p><a href="<c:url value='/employees?action=index' />">一覧に戻る</a></p>
    </c:param>
</c:import>