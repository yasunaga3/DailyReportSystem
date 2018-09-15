package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import models.Employee;
import utils.PagenationUtil;

/**
 * Servlet implementation class EmployeesServlet
 */
@WebServlet("/employees")
public class EmployeesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 変数の初期化
		String url = null;	 // 遷移先URL
		int current_page = 1; // 現行ページ用変数の初期化
		// パラメータの取得
		String str_current_page = request.getParameter("page"); // 現行ページの取得
		String action = request.getParameter("action"); // actionパラメータの取得
		int items_per_page = Integer.parseInt((String) this.getServletContext().getAttribute("items")); // 1ページ当たりの表示数
		HttpSession session = request.getSession();
		Employee e = (Employee) session.getAttribute("login_employee"); // ログイン中従業員の取得

		// ページ遷移を行う場合
		if (str_current_page != null && !str_current_page.equals("")) {
			current_page = Integer.parseInt(str_current_page); // 現行ページの取得
			// ページネーションを設定しページ遷移を行う
			PagenationUtil.setEmployeesIndex(request, e, current_page, items_per_page); // ページネーション
			url = "/WEB-INF/views/employees/index.jsp";
		}

		// 従業員indexを表示する(初回従業員リストを要求された)場合
		if ("index".equals(action)) {
			PagenationUtil.setEmployeesIndex(request, e, current_page, items_per_page); // ページネーション
			url = "/WEB-INF/views/employees/index.jsp";
		}
		// 	遷移先URLの設定
		RequestDispatcher rd = request.getRequestDispatcher(url);
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
