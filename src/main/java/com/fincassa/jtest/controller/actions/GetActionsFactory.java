package com.fincassa.jtest.controller.actions;

import com.fincassa.jtest.controller.actions.base.DownloadFileByIdAction;
import com.fincassa.jtest.controller.actions.base.GetFilesListAction;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vyn on 14.11.2016.
 */
public class GetActionsFactory {
    private static final Map<String,IAction> _actionMap= new HashMap<String,IAction>(5);

    static {
        _actionMap.put("downloadfile",new DownloadFileByIdAction());
        _actionMap.put("getfileslist",new GetFilesListAction());
    }

    public static IAction getActionByName(String pName) {
        return _actionMap.get(pName);
    }
}
