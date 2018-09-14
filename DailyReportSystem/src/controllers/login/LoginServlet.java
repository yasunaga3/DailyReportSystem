package controllers.login;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import models.Employee;
import utils.DBUtil;
import utils.EncryptUtil;
import utils.PagenationUtil;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ページ情報の取得
		int items_per_page = Integer.parseInt((String) this.getServletContext().getAttribute("items")); // 1ページ当たりの表示数
		int current_page = Integer.parseInt(request.getParameter("page")); // 現行ページ

		HttpSession session = request.getSession();
		Employee e = (Employee) session.getAttribute("login_employee");
		PagenationUtil.setMyReportsPage(request, e, current_page, items_per_page); // ページネーション
		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/topPage/index.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ページ情報の取得
		int items_per_page = Integer.parseInt((String) this.getServletContext().getAttribute("items")); // 1ページ当たりの情報表示件数
		int current_page = 1; // 現行ページ
		// 従業員コードとパスワードの取得
		String code = request.getParameter("code");
		String plain_pass = request.getParameter("password");
		// ログインチェックと移譲先URL
		Employee login_employee = loginCheck(code, plain_pass);
		String url = null;
		// URLとリクエスト情報の設定
		if (login_employee == null) {
//			request.setAttribute("_token", request.getSession().getId());
			request.setAttribute("hasError", true);
			url = "login.jsp";
		} else {
			PagenationUtil.setMyReportsPage(request, login_employee, current_page, items_per_page); // ページネーション
			HttpSession session = request.getSession();
			session.setAttribute("login_employee", login_employee);
			request.setAttribute("flush", "ログインしました。");
			url = "/WEB-INF/views/topPage/index.jsp";
		}
		RequestDispatcher rd = request.getRequestDispatcher(url);
		rd.forward(request, response);
	}

	// ログインチェック
	protected Employee loginCheck(String code, String plain_pass) {
		EntityManager em = DBUtil.createEntityManager();
		Employee e = null;
		// 暗号化パスワードの取得
		if (code != null && !code.equals("") && plain_pass != null && !plain_pass.equals("")) {
			String salt = (String) this.getServletContext().getAttribute("salt"); // サルト文字列の取得
			String password = EncryptUtil.getPasswordEncrypt(plain_pass, salt); // パスワードの暗号化
			// 該当する従業員の検索
			try {
				e = em.createNamedQuery("checkLoginCodeAndPassword", Employee.class)
						.setParameter("code", code)
						.setParameter("pass", password)
						.getSingleResult();
			} catch (NoResultException ex) {
				ex.printStackTrace();
			}
			em.close();
		}
		return e;
	}

}
