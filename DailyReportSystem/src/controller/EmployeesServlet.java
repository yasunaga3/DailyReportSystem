package controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import models.Employee;
import models.validators.EmployeeValidator;
import utils.DBUtil;
import utils.EncryptUtil;
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
		// 従業員一覧画面の｢詳細を表示｣が押された場合
		} else if ("show".equals(action)) {
			url = showEmployeeDetails(request);
		// 従業員情報詳細ページの｢この従業員情報を編集する｣が押された場合
		} else if ("edit".equals(action)) {
			url = editEmployeeInfo(request, session);
		// 従業員情報編集ページの｢投稿｣ボタンが押された場合
		} else if ("update".equals(action)) {
			url = updateEmployeeInfo(request, session);
		// 従業員情報編集ページの｢この従業員情報を削除する｣が押された場合
		} else if ("destroy".equals(action)){
			url  = destroyEmployee(request, session);
		// 従業員一覧画面の｢新規従業員の登録｣が押された場合
		} else if ("new".equals(action)){
			url = newEmployee(request, session);
		// 従業員新規登録ページ画面の｢投稿｣ボタンが押された場合
		} else if ("create".equals(action)){
			url = createNewEmployee(request, session);
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

	// 従業員一覧画面の｢詳細を表示｣が押された場合の処理
	protected String showEmployeeDetails(HttpServletRequest request) {
		int id = Integer.parseInt(request.getParameter("id"));
        EntityManager em = DBUtil.createEntityManager();
        Employee e = em.find(Employee.class, id);
        em.close();
        // リクエスト情報の設定
        request.setAttribute("employee", e);
		return "/WEB-INF/views/employees/show.jsp";
	}

	// 従業員情報詳細ページの｢この従業員情報を編集する｣が押された場合の処理
	protected String editEmployeeInfo(HttpServletRequest request, HttpSession session) {
        // 従業員情報の取得
		int id = Integer.parseInt(request.getParameter("id"));
		EntityManager em = DBUtil.createEntityManager();
        Employee e = em.find(Employee.class, id);
        em.close();
        // リクエスト情報の設定
        request.setAttribute("employee", e);
        request.setAttribute("_token", session.getId());
        session.setAttribute("employee_id", e.getId());
		return "/WEB-INF/views/employees/edit.jsp";
	}

	// 従業員情報編集ページの｢投稿｣ボタンが押された場合の処理
	protected String updateEmployeeInfo(HttpServletRequest request, HttpSession session) {
		String url = null;
		// session_id("_token")と編集対象となる従業員IDの取得
		String _token = request.getParameter("_token");	// リクエストスコープ内のsession_id
		Integer emp_id = (Integer)(session.getAttribute("employee_id"));
	    // 従業員情報の取得
		String emp_code = request.getParameter("code");
		String emp_name = request.getParameter("name");
		String emp_pass = request.getParameter("password");
		int admin_flg = Integer.parseInt(request.getParameter("admin_flag"));
	    // session_idが一致ならば従業員情報更新
	    if(_token != null && _token.equals(session.getId())) {
	        EntityManager em = DBUtil.createEntityManager();
	        Employee e = em.find(Employee.class, emp_id);
	        // 従業員コード重複チェック
	        Boolean code_duplicate_check = true;
	        if (e.getCode().equals(emp_code)) {
	        	code_duplicate_check = false;
			} else {
				e.setCode(emp_code);
			}
	        // パスワード変更チェック
	        Boolean password_check_flag = true;
	        if (emp_pass == null || emp_pass.equals("")) {
	        	password_check_flag = false;
			} else {
				String salt = (String)this.getServletContext().getAttribute("salt"); // 暗号化方式の取得
				String encryptedPass = EncryptUtil.getPasswordEncrypt(emp_pass, salt); // 暗号化済パスワード
				e.setPassword(encryptedPass);
			}
	        // 従業員情報の設定
	        e.setName(emp_name);
	        e.setAdmin_flag(admin_flg);
	        e.setUpdated_at(new Timestamp(System.currentTimeMillis()));
	        e.setDelete_flag(0);
	        // バリデーション
	        List<String> errors = EmployeeValidator.validate(e, code_duplicate_check, password_check_flag);
	        // バリデーションの結果、エラーがあれば edit.jsp へ差し戻し、エラーがなければ更新する
	        if (errors.size() > 0) {
	            em.close();
	            // フラッシュ(失敗)用情報の設定
	            request.setAttribute("_token", request.getSession().getId());
	            request.setAttribute("employee", e);
	            request.setAttribute("errors", errors);
	            // edit.jspへ差し戻し
	            url = "/WEB-INF/views/employees/edit.jsp";
	        } else {
				// 従業員情報の更新
	            em.getTransaction().begin();
	            em.getTransaction().commit();
	            em.close();
	            request.setAttribute("flush", "更新が完了しました。");
	            session.removeAttribute("employee_id");
	            url = "employees?action=index";
	        }
	    }
		return url;
	}

	// 従業員情報編集ページの｢この従業員情報を削除する｣が押された場合の処理
	protected String destroyEmployee(HttpServletRequest request, HttpSession session) {
		String url = null;
		// session_id("_token")と編集対象となる従業員IDの取得
		String _token = request.getParameter("_token");	// リクエストスコープ内のsession_id
		Integer emp_id = (Integer)(session.getAttribute("employee_id"));
		if (_token != null && _token.equals(session.getId())) {
			EntityManager em = DBUtil.createEntityManager();
			Employee e = em.find(Employee.class, emp_id);
		    e.setDelete_flag(1); // 削除フラグを立てる
		    e.setUpdated_at(new Timestamp(System.currentTimeMillis()));
		    // 更新処理
		    em.getTransaction().begin();
		    em.getTransaction().commit();
		    em.close();
		    request.setAttribute("flush", "削除が完了しました。");
		    session.removeAttribute("employee_id");
            url = "employees?action=index";
		}
		return url;
	}

	// 従業員一覧画面の｢新規従業員の登録｣が押された場合の処理
	protected String newEmployee(HttpServletRequest request, HttpSession session) {
		request.setAttribute("_token", session.getId());
		return "/WEB-INF/views/employees/new.jsp";
	}

	// 従業員新規登録ページ画面の｢投稿｣ボタンが押された場合の処理
	protected String createNewEmployee(HttpServletRequest request, HttpSession session) {
		String url = null;
		String _token = (String) request.getParameter("_token");
		// 新規従業員の作成
		if (_token != null && _token.equals(request.getSession().getId())) {
			// リクエストパラメータの取得
			String plainPassword = request.getParameter("password"); // パスワード(平文)の取得
			String salt = (String) this.getServletContext().getAttribute("salt"); // サルト文字列の取得
			String encryptedPass = EncryptUtil.getPasswordEncrypt(plainPassword, salt); // 暗号化パスワードの取得
			int admin_flag = Integer.parseInt(request.getParameter("admin_flag")); // 管理者フラグの取得
			Timestamp currentTime = new Timestamp(System.currentTimeMillis());
			// 従業員情報の取得
			Employee e = new Employee();
			e.setCode(request.getParameter("code"));
			e.setName(request.getParameter("name"));
			e.setPassword(encryptedPass);
			e.setAdmin_flag(admin_flag);
			e.setCreated_at(currentTime);
			e.setUpdated_at(currentTime);
			e.setDelete_flag(0);
			// バリデーション
			List<String> errors = EmployeeValidator.validate(e, true, true);
			EntityManager em = DBUtil.createEntityManager();
			if (errors.size() > 0) {
				em.close();
				// 	リクエストスコープへ情報を格納する
				request.setAttribute("_token", session.getId()); // session_id
				request.setAttribute("employee", e); // 従業員情報
				request.setAttribute("errors", errors); // エラー情報
				url = "/WEB-INF/views/employees/new.jsp";
			} else {
				// バリデーションOKならばDBに追加する
				em.getTransaction().begin();
				em.persist(e);
				em.getTransaction().commit();
				em.close();
				request.setAttribute("flush", "登録が完了しました。");
				url = "employees?action=index";
			}
		}
		return url;
	}

}
