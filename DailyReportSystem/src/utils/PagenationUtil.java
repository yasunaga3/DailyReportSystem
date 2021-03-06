package utils;

import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import models.Employee;
import models.Report;

public class PagenationUtil {

	/**
	 * @param request：HttpServletRequest
	 * @param login_employee：ログイン中の従業員情報
	 * @param current_page：現行ページ
	 * @param items_per_page：1ページ当たりの情報表示件数
	 *
	 * 1ページ当たりの表示件数分の日報リストを取得する
	 * 日報リスト全体の総件数を取得する
	 *
	 * 1ページ当たりの表示件数分の日報リストをリクエスト情報にセットする
	 * 日報リスト全体の総件数をリクエスト情報にセットする
	 * 現行ページをリクエスト情報にセットする
	 */
	public static void setMyReportsPage(HttpServletRequest request, Employee login_employee, int current_page, int items_per_page) {
		EntityManager em = DBUtil.createEntityManager();
		// 日報リストの取得(1ページ当たりの表示件数のみ)
        List<Report> reports = em.createNamedQuery("getMyAllReports", Report.class)
									                 .setParameter("employee", login_employee)
									                 .setFirstResult(items_per_page * (current_page - 1))
									                 .setMaxResults(items_per_page)
									                 .getResultList();
        // 日報リストの件数
		long reports_count = (long)em.createNamedQuery("getMyReportsCount", Long.class)
										                    .setParameter("employee", login_employee)
										                    .getSingleResult();
		em.close();
		// リクエスト情報の設定
		request.setAttribute("reports", reports);                       // 日報リスト(1ページ当たりの表示件数のみ)
		request.setAttribute("reports_count", reports_count); // 日報リストの総件数
		request.setAttribute("page", current_page);                 // 現行ページ
	}

	/**
	 * @param request：HttpServletRequest
	 * @param login_employee：ログイン中の従業員情報
	 * @param current_page：現行ページ
	 * @param items_per_page：1ページ当たりの情報表示件数
	 *
	 * 1ページ当たりの表示件数分の日報リストを取得する
	 * 日報リスト全体の総件数を取得する
	 *
	 * 1ページ当たりの表示件数分の日報リストをリクエスト情報にセットする
	 * 日報リスト全体の総件数をリクエスト情報にセットする
	 * 現行ページをリクエスト情報にセットする
	 */
	public static void setAllReportsPage(HttpServletRequest request, int current_page, int items_per_page) {
		EntityManager em = DBUtil.createEntityManager();
		// 日報リストの取得(1ページ当たりの表示件数のみ)
        List<Report> reports = em.createNamedQuery("getAllReports", Report.class)
									                 .setFirstResult(items_per_page * (current_page - 1))
									                 .setMaxResults(items_per_page)
									                 .getResultList();
        // 日報リストの件数
        long reports_count = (long)em.createNamedQuery("getReportsCount", Long.class)
                											.getSingleResult();
		em.close();
		// リクエスト情報の設定
		request.setAttribute("reports", reports);                       // 日報リスト(1ページ当たりの表示件数のみ)
		request.setAttribute("reports_count", reports_count); // 日報リストの総件数
		request.setAttribute("page", current_page);                 // 現行ページ
	}


	/**
	 * @param request：HttpServletRequest
	 * @param login_employee：ログイン中の従業員情報
	 * @param current_page：現行ページ
	 * @param items_per_page：1ページ当たりの情報表示件数
	 *
	 * 1ページ当たりの表示件数分の従業員リストを取得する
	 * 従業員リスト全体の総件数を取得する
	 *
	 * 1ページ当たりの表示件数分の従業員リストをリクエスト情報にセットする
	 * 従業員リスト全体の総件数をリクエスト情報にセットする
	 * 現行ページをリクエスト情報にセットする
	 */
	public static void setEmployeesIndex(HttpServletRequest request, Employee login_employee, int current_page, int items_per_page) {
        EntityManager em = DBUtil.createEntityManager();
		// 従業員リストの取得(1ページ当たりの表示件数のみ)
        List<Employee> employees = em.createNamedQuery("getAllEmployees", Employee.class)
												                .setFirstResult(items_per_page * (current_page - 1))
												                .setMaxResults(items_per_page)
												                .getResultList();
        // 従業員リストの件数
		long employees_count = (long)em.createNamedQuery("getEmployeesCount", Long.class)
												                  .getSingleResult();
		em.close();
		// リクエスト情報の設定
        request.setAttribute("employees", employees);                       // 従業員リスト(1ページ当たりの表示件数のみ)
        request.setAttribute("employees_count", employees_count); // 従業員リストの総件数
        request.setAttribute("page", current_page);                             // 現行ページ

//        System.out.println("employees.size() = " + employees.size());
//        System.out.println("employees_count = " + employees_count);
	}

}
