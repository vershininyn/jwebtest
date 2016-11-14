package com.fincassa.jtest.controller.actions.base;

import com.fincassa.jtest.controller.actions.IAction;
import com.fincassa.jtest.utils.CookiesManager;
import com.fincassa.jtest.data.DBManager;
import com.fincassa.jtest.utils.JsonManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Created by vyn on 14.11.2016.
 */
public class GetFilesListAction implements IAction {
    @Override
    public void doAction(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String clientid= CookiesManager.getClientId(req);

        Map<String,String> filelist= DBManager.getFilesByClientId(clientid);

        String json= JsonManager.encodeToJson(filelist);

        PrintWriter writer= resp.getWriter();

        writer.write(json);
        writer.flush();
        writer.close();
    }
}
