package com.fincassa.jtest.controller;

import com.fincassa.jtest.controller.actions.GetActionsFactory;
import com.fincassa.jtest.controller.actions.IAction;
import com.fincassa.jtest.data.DBManager;
import com.fincassa.jtest.utils.CookiesManager;
import com.fincassa.jtest.utils.IOManager;
import com.fincassa.jtest.utils.JsonManager;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.*;
import java.io.*;
import java.util.HashMap;


/**
 * Created by vyn on 12.11.2016.
 */
@MultipartConfig
public class UploadController extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        try {
            IOManager.init();
            CookiesManager.init("clientid");
        } catch (IOException e) {
            throw new ServletException(e.getLocalizedMessage());
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (CookiesManager.getClientId(req) == null) CookiesManager.setClientId(resp);

        super.service(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name= req.getParameter("action");

        if (name == null) {throw new ServletException("Unacceptalbe action!!!");}

        IAction action= GetActionsFactory.getActionByName(name);

        action.doAction(req, resp);

        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Part file= req.getPart("file");

        if (file == null) {throw new ServletException("User must select some files!!!");};

        String uploadedGuidFilepath= IOManager.saveFileToStorageDirectory(file);

        int fileid= DBManager.registerFile(CookiesManager.getClientId(req),
                uploadedGuidFilepath,
                extractFilename(file));

        PrintWriter outWriter= resp.getWriter();

        String json= JsonManager.encodeToJson(new HashMap<String,String>(1){{
            put("fileid",String.valueOf(fileid));
        }});

        outWriter.write(json);
        outWriter.flush();
        outWriter.close();

        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private String extractFilename(Part pFilepart) {
        String content= pFilepart.getHeader("Content-Disposition");

        return content.split("=")[2].replaceAll("\"","");
    }
}
