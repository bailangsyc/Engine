/*
 *  Copyright (C) 2014 The AppCan Open Source Project.
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.

 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.

 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.zywx.wbpalmstar.engine.universalex;

import java.lang.reflect.Constructor;

public class ThirdPluginObject {

    public static final String js_object_begin = "window.";
    public static final String js_arg_low = "jo(arguments)";
    public static final String js_staves = ".";
    public static final String js_function_begin = ":function(){return ";
    public static final String js_symbol = "=";
    public static final String js_l_brackets = "(";
    public static final String js_function_end = ");},";
    public static final String js_property_end = ",";
    public static final String js_l_braces = "{";
    public static final String js_object_end = "};";
    public static final String js_method_transaction = "transaction:function(){var b=jo(arguments);" +
            "uexDispatcher.dispatch('uexDataBaseMgr','beginTransaction',b);arguments[2]();" +
            "uexDispatcher.dispatch('uexDataBaseMgr','endTransaction',b);},";

    public static final String js_dispatch_header = "uexDispatcher.dispatch(\'";
    public static final String js_dispatch_mid = "\',\'";
    public static final String js_dispatch_end = "\',";

    public String uexName;// jsName（插件的名字）：例如 uexDataBaseMgr
    public StringBuffer uexScript;
    public String jclass; //(入口类的包名): 例如 org.zywx.wbpalmstar.plugin.uexdatabasemgr.EUExDataBaseMgr
    // 入口类中的两个的构造：初始化插件的时候反射获取了入口类中含有两个参数(context,EBrowserView）的构造
    public Constructor<?> jobject;
    public boolean isGlobal = false;
    public EUExBase pluginObj = null;

    public ThirdPluginObject(Constructor<?> javaObj) {
        jobject = javaObj;
        uexScript = new StringBuffer();
    }

    public void oneObjectBegin(String jsName) {
        String begin = js_object_begin + jsName + js_symbol + js_l_braces;
        uexScript.append(begin);
        uexName = jsName;
    }

    public void addMethod(String method) {
        if ("transaction".equals(method)) {
            uexScript.append(js_method_transaction);
            return;
        }

        uexScript.append(method);
        uexScript.append(js_function_begin);

        uexScript.append(js_dispatch_header);
        uexScript.append(uexName);
        uexScript.append(js_dispatch_mid);
        uexScript.append(method);
        uexScript.append(js_dispatch_end);
        uexScript.append(js_arg_low);
        uexScript.append(js_function_end);
    }

    public void addProperty(String property) {
        uexScript.append(property);
        uexScript.append(js_property_end);
    }

    public void oneObjectOver(StringBuffer scrit) {
        uexScript.append(js_object_end);
        scrit.append(uexScript);
        uexScript = null;
    }

}
