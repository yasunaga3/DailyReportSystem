package controller;

import java.io.IOException;
import java.sql.Date;
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
import models.Report;
import models.validators.ReportValidator;
import utils.DBUtil;
import utils.PagenationUtil;

/**
 * Servlet implementation class ReportsServlet
 */
@WebServlet("/reports")
public class ReportsServlet extends HttpServlet {
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
			PagenationUtil.setAllReportsPage(request, current_page, items_per_page); // ページネーション
			url = "/WEB-INF/views/reports/index.jsp";
		}

		// 日報ページの｢一覧に戻る｣が押された場合
		if("index".equals(action)) {
			PagenationUtil.setAllReportsPage(request, current_page, items_per_page);
			url = "/WEB-INF/views/reports/index.jsp";
		// 日報の｢詳細を見る｣が押された場合
		} else if ("show".equals(action)) {
			url = showReport(request, session);
		// ｢新規日報の登録｣が押された場合
		} else if ("new".equals(action)) {
			url = newReport(request, session);
		// 日報新規登録ページの｢投稿｣ボタンが押された場合
		} else if ("create".equals(action)) {
			url = createNewReport(request, session);
		// 日報詳細ページの｢この日報を編集する｣が押された場合
		} else if ("edit".equals(action)) {
			url = editReport(request, session);
		// 日報編集ページの｢投稿｣ボタンが押された場合
		} else if ("update".equals(action)) {
			url = updateReport(request, session);
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

	// 日報の｢詳細を見る｣が押された場合の処理
	protected String showReport(HttpServletRequest request, HttpSession session) {
		// 日報情報の取得
		Integer report_id = Integer.parseInt(request.getParameter("id"));
		EntityManager em = DBUtil.createEntityManager();
        Report r = em.find(Report.class, report_id);
        em.close();
        // リクエスト情報の設定
        request.setAttribute("report", r);
        request.setAttribute("_token", session.getId());
		return "/WEB-INF/views/reports/show.jsp";
	}
	
	// ｢新規日報の登録｣が押された場合の処理
	protected String newReport(HttpServletRequest request, HttpSession session) {
		String _token = session.getId();
	    Report r = new Report();
	    r.setReport_date(new Date(System.currentTimeMillis()));
	    // リクエスト情報の設定
		request.setAttribute("_token", _token);
		request.setAttribute("report", r);
		return "/WEB-INF/views/reports/new.jsp";		
	}
	
	// 日報新規登録ページの｢投稿｣ボタンが押された場合の処理
	protected String createNewReport(HttpServletRequest request, HttpSession session) {
		String url = null;
		// Stringで受け取った日付を Date 型へ変換する処理
		// 日付欄をわざと未入力にした場合は当日の日付を入れる
		Date report_date = new Date(System.currentTimeMillis());
		String rd_str = request.getParameter("report_date");
		if(rd_str != null && !rd_str.equals("")) {
		    report_date = Date.valueOf(request.getParameter("report_date"));
		}

		// sessionとrequestスコープ内のsessionIDを取得する
		session = request.getSession();
		String _token = (String) request.getParameter("_token");

		// sessionスコープ内のsessionIDとrequestスコープ内のsessionIDが一致すれば
		// 日報の登録処理を行う
		if (_token != null && _token.equals(session.getId())) {
			EntityManager em = DBUtil.createEntityManager();
			// 日報情報の取得
			Report r = new Report();
			r.setEmployee((Employee) session.getAttribute("login_employee"));
			r.setReport_date(report_date);
            r.setTitle(request.getParameter("title"));
            r.setContent(request.getParameter("content"));
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            r.setCreated_at(currentTime);
            r.setUpdated_at(currentTime);

            // バリデーション
            List<String> errors = ReportValidator.validate(r);
            if(errors.size() > 0) {
                em.close();
                // 	リクエストスコープへ情報を格納する
                request.setAttribute("_token", session.getId());
                request.setAttribute("report", r);
                request.setAttribute("errors", errors);
                url = "/WEB-INF/views/reports/new.jsp";
            } else {
            	// バリデーションOKならばDBに追加する
                em.getTransaction().begin();
                em.persist(r);
                em.getTransaction().commit();
                em.close();
                request.setAttribute("flush", "登録が完了しました。");
                url = "reports?action=index";
            }
		}
		return url;		
	}
	
	// 日報詳細ページページの｢この日報を編集する｣が押された場合の処理
	protected String editReport(HttpServletRequest request, HttpSession session) {
		// 日報情報の取得
		Integer report_id = Integer.parseInt(request.getParameter("id"));
		EntityManager em = DBUtil.createEntityManager();
        Report r = em.find(Report.class, report_id);
        em.close();
        // login_employee.getId()とr.getEmployee().getId()が一致したら
        // request情報とsession情報を設定
        Employee login_employee = (Employee)session.getAttribute("login_employee");
        if(login_employee.getId() == r.getEmployee().getId()) {
            request.setAttribute("report", r);
            request.setAttribute("_token", session.getId());
            session.setAttribute("report_id", r.getId());
        }
		return "/WEB-INF/views/reports/edit.jsp";		
	}
	
	// 日報編集ページの｢投稿｣ボタンが押された場合の処理
	protected String updateReport(HttpServletRequest request, HttpSession session) {
		String url =null;
        String _token = (String)request.getParameter("_token");
        int report_id = (int) session.getAttribute("report_id");
        if(_token != null && _token.equals(request.getSession().getId())) {
            EntityManager em = DBUtil.createEntityManager();
            Report r = em.find(Report.class, report_id);
            r.setReport_date(Date.valueOf(request.getParameter("report_date")));
            r.setTitle(request.getParameter("title"));
            r.setContent(request.getParameter("content"));
            r.setUpdated_at(new Timestamp(System.currentTimeMillis()));
            List<String> errors = ReportValidator.validate(r);
            if(errors.size() > 0) {
                em.close();
                request.setAttribute("_token", request.getSession().getId());
                request.setAttribute("report", r);
                request.setAttribute("errors", errors);
                url ="/WEB-INF/views/reports/edit.jsp";
            } else {
                em.getTransaction().begin();
                em.getTransaction().commit();
                em.close();
                request.setAttribute("flush", "更新が完了しました。");
                request.getSession().removeAttribute("report_id");
                url = "reports?action=index";
            }
        }
		return url;		
	}
	
}
