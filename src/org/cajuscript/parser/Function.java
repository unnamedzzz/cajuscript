/*
 * Function.java
 * 
 * This file is part of CajuScript.
 * 
 * CajuScript is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3, or (at your option) 
 * any later version.
 * 
 * CajuScript is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with CajuScript.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.cajuscript.parser;

import org.cajuscript.CajuScript;
import org.cajuscript.Context;
import org.cajuscript.Value;
import org.cajuscript.Syntax;
import org.cajuscript.CajuScriptException;
import org.cajuscript.SyntaxPosition;
import org.cajuscript.compiler.Executable;

/**
 * Script element of type function.
 * @author eduveks
 */
public class Function extends Base {
    private String name = "";
    private String[] paramKey = new String[0];
    
    /**
     * Create new Function.
     * @param line Line detail
     */
    public Function(LineDetail line) {
        super(line);
    }

    /**
     * Create new Function.
     * @param executable Executable
     */
    public Function(Executable executable, String[] parameters) {
        super(executable);
        this.paramKey = parameters;
    }
    
    /**
     * Set the function definition, name and parameters.
     * @param funcDef Function definition
     */
    public void setDefinition(String funcDef, Syntax syntax) {
        funcDef = funcDef.trim();
        SyntaxPosition syntaxPositionStart = syntax.matcherPosition(funcDef, syntax.getFunctionCallParametersBegin());
        if (syntaxPositionStart.getStart() > -1) {
            SyntaxPosition syntaxPositionEnd = syntax.matcherPosition(funcDef, syntax.getFunctionCallParametersEnd());
            name = funcDef.substring(0, syntaxPositionStart.getStart()).trim();
            String param = funcDef.substring(syntaxPositionStart.getEnd(), syntaxPositionEnd.getStart()).trim();
            paramKey = syntax.getFunctionCallParametersSeparator().split(param);
            for (int x = 0; x < paramKey.length; x++) {
                paramKey[x] = paramKey[x].trim();
            }
        } else {
            int p = funcDef.indexOf(' ');
            if (p > -1) {
                name = funcDef.substring(0, p).trim();
                String param = funcDef.substring(p + 1).trim();
                paramKey = syntax.getFunctionCallParametersSeparator().split(param);
                for (int x = 0; x < paramKey.length; x++) {
                    paramKey[x] = paramKey[x].trim();
                }
            } else {
                name = funcDef.trim();
            }
        }
    }
    
    /**
     * Function name.
     * @return Name
     */
    public String getName() {
        return name;
    }

    /**
     * Function name.
     * @param name Name
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Function parameters.
     * @return Parameters
     */
    public String[] getParameters() {
        return paramKey;
    }
    
    /**
     * Function parameters.
     * @param paramKey Parameters
     */
    public void setParameters(String[] paramKey) {
        this.paramKey = paramKey;
    }

    /**
     * Run function.
     * @param caju CajuScript instance
     * @param paramValue Values of parameters
     * @return Value returned by execution
     * @throws org.cajuscript.CajuScriptException Errors ocurred on execution
     */
    public Value invoke(CajuScript caju, Context context, Syntax syntax, String... paramValue) throws CajuScriptException {
        if (executable == null) {
            caju.setRunningLine(getLineDetail());
        }
        for (int i = 0; i < paramValue.length; i++) {
            context.setVar(paramKey[i], caju.getVar(paramValue[i]));
        }
        if (executable == null) {
            for (Element element : elements) {
                Value v = element.execute(caju, context, syntax);
                if (v != null && canElementReturn(element)) {
                    return v;
                }
            }
        } else {
            Value v = executable.execute(caju, context, syntax);
            if (v != null) {
                return v;
            }
        }
        return new Value(caju, context, syntax);
    }
    
    /**
     * This method is never used for functions, method "invoke" can be used to
     * execute a function.
     * @param caju CajuScript instance
     * @param context Context
     * @param syntax Syntax
     * @return Null value
     * @throws org.cajuscript.CajuScriptException Errors ocurred on execution
     */
    @Override
    public Value execute(CajuScript caju, Context context, Syntax syntax) throws CajuScriptException {
        caju.setRunningLine(getLineDetail());
        return null;
    }
}
