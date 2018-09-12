package models.validators;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import models.Employee;
import utils.DBUtil;

public class EmployeeValidator {

    public static List<String> validate(Employee e, Boolean code_duplicate_check_flag, Boolean password_check_flag) {
    	// エラーリスト
        List<String> errors = new ArrayList<String>();
        // 社員コード欄のバリデーション
        String code_error = _validateCode(e.getCode(), code_duplicate_check_flag);
        if(!code_error.equals("")) { errors.add(code_error); }
        // 氏名欄のバリデーション
        String name_error = _validateName(e.getName());
        if(!name_error.equals("")) { errors.add(name_error); }
        // パスワード欄のバリデーション
        String password_error = _validatePassword(e.getPassword(), password_check_flag);
        if(!password_error.equals("")) { errors.add(password_error); }
        // エラーリストの返却
		return errors;
    }

    // 社員コード欄のバリデーション
    private static String _validateCode(String code, Boolean code_duplicate_check_flag) {
    	// 社員コード欄の空欄チェック
        if(code == null || code.equals("")) { return "社員番号を入力してください。"; }
        // 社員コードの重複チェック
        if(code_duplicate_check_flag) {
            EntityManager em = DBUtil.createEntityManager();
            long employees_count = (long)em.createNamedQuery("checkRegisteredCode", Long.class)
                                           							  .setParameter("code", code)
                                           							  .getSingleResult();
            em.close();
            if(employees_count > 0) {
                return "入力された社員番号の情報は既に存在しています。";
            }
        }
        // 空欄ではなく、かつ重複もない場合、エラーリストに追加しない
        return "";
    }

    // 氏名欄のバリデーション
    private static String _validateName(String name) {
    	// 氏名欄の空欄チェック
        if(name == null || name.equals("")) { return "氏名を入力してください。"; }
        // 空欄ではなく、かつ重複もない場合、エラーリストに追加しない
        return "";
    }

    // パスワード欄のバリデーション
    private static String _validatePassword(String password, Boolean password_check_flag) {
    	// パスワード欄の空欄チェック
        if(password_check_flag && (password == null || password.equals(""))) { return "パスワードを入力してください。"; }
        // 空欄ではなく、かつ重複もない場合、エラーリストに追加しない
        return "";
    }
}
