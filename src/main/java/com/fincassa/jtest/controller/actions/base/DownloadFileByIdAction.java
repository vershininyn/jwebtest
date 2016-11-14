package com.fincassa.jtest.controller.actions.base;

import com.fincassa.jtest.controller.actions.IAction;
import com.fincassa.jtest.data.DBManager;
import com.fincassa.jtest.utils.IOManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by vyn on 14.11.2016.
 */
public class DownloadFileByIdAction implements IAction {
    @Override
    public void doAction(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id= req.getParameter("id");

        if ((id == null) || (id.isEmpty())) {throw new ServletException("Unacceptable id field!!!");}

        List<String> fileinfo= DBManager.getFileinfoById(Integer.parseInt(id));

        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/octet-stream");
        resp.setHeader("Content-disposition", "attachment; filename="+fileinfo.get(1));

        long size = IOManager.writeFileFromStorageToStream(fileinfo.get(0), resp.getOutputStream());
        resp.setContentLength((int) size);
    }
}
