package com.fincassa.jtest.controller.actions;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by vyn on 14.11.2016.
 */
public interface IAction {
    void doAction(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;
}
