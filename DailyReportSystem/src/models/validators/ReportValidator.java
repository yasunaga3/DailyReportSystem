package models.validators;

import java.util.ArrayList;
import java.util.List;

import models.Report;

public class ReportValidator {

    public static List<String> validate(Report r) {
    	// エラーリスト
        List<String> errors = new ArrayList<String>();
        // タイトルチェック
        String title_error = _validateTitle(r.getTitle());
        if(!title_error.equals("")) { errors.add(title_error); }
        // 内容チェック
        String content_error = _validateContent(r.getContent());
        if(!content_error.equals("")) { errors.add(content_error); }
        // エラーリストの返却
        return errors;
    }

    // タイトルチェック
    private static String _validateTitle(String title) {
        if(title == null || title.equals("")) { return "タイトルを入力してください。"; }
        return "";
    }

    // 内容チェック
    private static String _validateContent(String content) {
        if(content == null || content.equals("")) { return "内容を入力してください。"; }
        return "";
    }
}
