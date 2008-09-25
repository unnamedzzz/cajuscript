/*
 * Operation.java
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
import org.cajuscript.math.Operable;
import org.cajuscript.CajuScriptException;

/**
 * Script element of type operation.
 * @author eduveks
 */
public class Operation extends Base {
    /**
     * Operators.
     */
    public static enum Operator {
        ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION, MODULES,
        AND, OR, EQUAL, NOT_EQUAL, LESS, GREATER, LESS_EQUAL, GREATER_EQUAL
    }
    private static final Boolean BOOLEAN_TRUE = new Boolean(true);
    private static final Boolean BOOLEAN_FALSE = new Boolean(false);
    private Element firstCommand = null;
    private Element secondCommand = null;
    private Operator operator = null;
    private String contextsKey;
    private String valueKey;

    /**
     * Create new Operation.
     * @param line Line detail
     */
    public Operation(LineDetail line) {
        super(line);
        contextsKey = org.cajuscript.CajuScript.CAJU_VARS + "_contexts_" + Integer.toString(this.hashCode());
        valueKey = org.cajuscript.CajuScript.CAJU_VARS + "_value_" + Integer.toString(this.hashCode());
    }

    /**
     * Set commands of this operation
     * @param firstCommand First command
     * @param operator Operator
     * @param secondCommand Second command
     * @throws org.cajuscript.CajuScriptException Operator is invalid!
     */
    public void setCommands(Element firstCommand, Operator operator, Element secondCommand) throws CajuScriptException {
        this.firstCommand = firstCommand;
        this.operator = operator;
        this.secondCommand = secondCommand;
    }

    /**
     * Executed this element and all childs elements.
     * @param caju CajuScript
     * @param context Context
     * @param syntax Syntax
     * @return Value returned by execution
     * @throws org.cajuscript.CajuScriptException Errors ocurred on execution
     */
    @Override
    public Value execute(CajuScript caju, Context context, Syntax syntax) throws CajuScriptException {
        caju.setRunningLine(getLineDetail());
        Value contextsValue = context.getVar(contextsKey);
        if (contextsValue == null) {
            contextsValue = caju.toValue(new java.util.ArrayList<Context>());
            context.setVar(contextsKey, contextsValue);
        }
        java.util.ArrayList<Context> contexts = (java.util.ArrayList<Context>)contextsValue.getValue();
        Value v = context.getVar(valueKey);
        if (v == null || !contexts.contains(context)) {
            contexts.add(context);
            v = new Value(caju, context, syntax);
            context.setVar(valueKey, v);
        }
        Value v1 = firstCommand.execute(caju, context, syntax);
        Value v2 = secondCommand.execute(caju, context, syntax);
        switch (operator) {
            case ADDITION:
                if (v1.getType() == Value.Type.NUMBER && v2.getType() == Value.Type.NUMBER) {
                    if (v1.getTypeNumber() == Value.TypeNumber.INTEGER && v2.getTypeNumber() == Value.TypeNumber.INTEGER) {
                        v.setValue(new Integer(v1.getNumberIntegerValue() + v2.getNumberIntegerValue()));
                        return v;
                    } else if ((v1.getTypeNumber() == Value.TypeNumber.INTEGER || v1.getTypeNumber() == Value.TypeNumber.FLOAT) && (v2.getTypeNumber() == Value.TypeNumber.INTEGER || v2.getTypeNumber() == Value.TypeNumber.FLOAT)) {
                        v.setValue(new Float(v1.getNumberFloatValue() + v2.getNumberFloatValue()));
                        return v;
                    } else if ((v1.getTypeNumber() == Value.TypeNumber.INTEGER || v1.getTypeNumber() == Value.TypeNumber.LONG) && (v2.getTypeNumber() == Value.TypeNumber.INTEGER || v2.getTypeNumber() == Value.TypeNumber.LONG)) {
                        v.setValue(new Long(v1.getNumberLongValue() + v2.getNumberLongValue()));
                        return v;
                    } else {
                        v.setValue(new Double(v1.getNumberDoubleValue() + v2.getNumberDoubleValue()));
                        return v;
                    }
                } else if (v1.getType() == Value.Type.OBJECT && v2.getType() == Value.Type.OBJECT && v1.getValue() instanceof Operable && v2.getValue() instanceof Operable) {
                    v.setValue(Operable.class.cast(v1.getValue()).plus(v2.getValue()));
                    return v;
                } else if ((v1.getType() == Value.Type.STRING || v1.getType() == Value.Type.NUMBER || v1.getType() == Value.Type.OBJECT) && (v2.getType() == Value.Type.STRING || v2.getType() == Value.Type.NUMBER || v2.getType() == Value.Type.OBJECT)) {
                    v.setValue(v1.getStringValue() + v2.getStringValue());
                    return v;
                }
                return null;
            case SUBTRACTION:
                if (v1.getType() == Value.Type.NUMBER && v2.getType() == Value.Type.NUMBER) {
                    if (v1.getTypeNumber() == Value.TypeNumber.INTEGER && v2.getTypeNumber() == Value.TypeNumber.INTEGER) {
                        v.setValue(new Integer(v1.getNumberIntegerValue() - v2.getNumberIntegerValue()));
                        return v;
                    } else if ((v1.getTypeNumber() == Value.TypeNumber.INTEGER || v1.getTypeNumber() == Value.TypeNumber.FLOAT) && (v2.getTypeNumber() == Value.TypeNumber.INTEGER || v2.getTypeNumber() == Value.TypeNumber.FLOAT)) {
                        v.setValue(new Float(v1.getNumberFloatValue() - v2.getNumberFloatValue()));
                        return v;
                    } else if ((v1.getTypeNumber() == Value.TypeNumber.INTEGER || v1.getTypeNumber() == Value.TypeNumber.LONG) && (v2.getTypeNumber() == Value.TypeNumber.INTEGER || v2.getTypeNumber() == Value.TypeNumber.LONG)) {
                        v.setValue(new Long(v1.getNumberLongValue() - v2.getNumberLongValue()));
                        return v;
                    } else {
                        v.setValue(new Double(v1.getNumberDoubleValue() - v2.getNumberDoubleValue()));
                        return v;
                    }
                } else if (v1.getType() == Value.Type.OBJECT && v2.getType() == Value.Type.OBJECT && v1.getValue() instanceof Operable && v2.getValue() instanceof Operable) {
                    v.setValue(Operable.class.cast(v1.getValue()).subtract(v2.getValue()));
                    return v;
                }
                return null;
            case MULTIPLICATION:
                if (v1.getType() == Value.Type.NUMBER && v2.getType() == Value.Type.NUMBER) {
                    if (v1.getTypeNumber() == Value.TypeNumber.INTEGER && v2.getTypeNumber() == Value.TypeNumber.INTEGER) {
                        v.setValue(new Integer(v1.getNumberIntegerValue() * v2.getNumberIntegerValue()));
                        return v;
                    } else if ((v1.getTypeNumber() == Value.TypeNumber.INTEGER || v1.getTypeNumber() == Value.TypeNumber.FLOAT) && (v2.getTypeNumber() == Value.TypeNumber.INTEGER || v2.getTypeNumber() == Value.TypeNumber.FLOAT)) {
                        v.setValue(new Float(v1.getNumberFloatValue() * v2.getNumberFloatValue()));
                        return v;
                    } else if ((v1.getTypeNumber() == Value.TypeNumber.INTEGER || v1.getTypeNumber() == Value.TypeNumber.LONG) && (v2.getTypeNumber() == Value.TypeNumber.INTEGER || v2.getTypeNumber() == Value.TypeNumber.LONG)) {
                        v.setValue(new Long(v1.getNumberLongValue() * v2.getNumberLongValue()));
                        return v;
                    } else {
                        v.setValue(new Double(v1.getNumberDoubleValue() * v2.getNumberDoubleValue()));
                        return v;
                    }
                } else if (v1.getType() == Value.Type.OBJECT && v2.getType() == Value.Type.OBJECT && v1.getValue() instanceof Operable && v2.getValue() instanceof Operable) {
                    v.setValue(Operable.class.cast(v1.getValue()).multiply(v2.getValue()));
                    return v;
                }
                return null;
            case DIVISION:
                if (v1.getType() == Value.Type.NUMBER && v2.getType() == Value.Type.NUMBER) {
                    if (v1.getTypeNumber() == Value.TypeNumber.INTEGER && v2.getTypeNumber() == Value.TypeNumber.INTEGER) {
                        v.setValue(new Integer(v1.getNumberIntegerValue() / v2.getNumberIntegerValue()));
                        return v;
                    } else if ((v1.getTypeNumber() == Value.TypeNumber.INTEGER || v1.getTypeNumber() == Value.TypeNumber.FLOAT) && (v2.getTypeNumber() == Value.TypeNumber.INTEGER || v2.getTypeNumber() == Value.TypeNumber.FLOAT)) {
                        v.setValue(new Float(v1.getNumberFloatValue() / v2.getNumberFloatValue()));
                        return v;
                    } else if ((v1.getTypeNumber() == Value.TypeNumber.INTEGER || v1.getTypeNumber() == Value.TypeNumber.LONG) && (v2.getTypeNumber() == Value.TypeNumber.INTEGER || v2.getTypeNumber() == Value.TypeNumber.LONG)) {
                        v.setValue(new Long(v1.getNumberLongValue() / v2.getNumberLongValue()));
                        return v;
                    } else {
                        v.setValue(new Double(v1.getNumberDoubleValue() / v2.getNumberDoubleValue()));
                        return v;
                    }
                } else if (v1.getType() == Value.Type.OBJECT && v2.getType() == Value.Type.OBJECT && v1.getValue() instanceof Operable && v2.getValue() instanceof Operable) {
                    v.setValue(Operable.class.cast(v1.getValue()).divide(v2.getValue()));
                    return v;
                }
                return null;
            case MODULES:
                if (v1.getType() == Value.Type.NUMBER && v2.getType() == Value.Type.NUMBER) {
                    if (v1.getTypeNumber() == Value.TypeNumber.INTEGER && v2.getTypeNumber() == Value.TypeNumber.INTEGER) {
                        v.setValue(new Integer(v1.getNumberIntegerValue() % v2.getNumberIntegerValue()));
                        return v;
                    } else if ((v1.getTypeNumber() == Value.TypeNumber.INTEGER || v1.getTypeNumber() == Value.TypeNumber.FLOAT) && (v2.getTypeNumber() == Value.TypeNumber.INTEGER || v2.getTypeNumber() == Value.TypeNumber.FLOAT)) {
                        v.setValue(new Float(v1.getNumberFloatValue() % v2.getNumberFloatValue()));
                        return v;
                    } else if ((v1.getTypeNumber() == Value.TypeNumber.INTEGER || v1.getTypeNumber() == Value.TypeNumber.LONG) && (v2.getTypeNumber() == Value.TypeNumber.INTEGER || v2.getTypeNumber() == Value.TypeNumber.LONG)) {
                        v.setValue(new Long(v1.getNumberLongValue() % v2.getNumberLongValue()));
                        return v;
                    } else {
                        v.setValue(new Double(v1.getNumberDoubleValue() % v2.getNumberDoubleValue()));
                        return v;
                    }
                } else if (v1.getType() == Value.Type.OBJECT && v2.getType() == Value.Type.OBJECT && v1.getValue() instanceof Operable && v2.getValue() instanceof Operable) {
                    v.setValue(Operable.class.cast(v1.getValue()).module(v2.getValue()));
                    return v;
                }
                return null;
            case AND:
                if (((Boolean) v1.getValue()).booleanValue() == true && ((Boolean) v2.getValue()).booleanValue() == true) {
                    v.setValue(BOOLEAN_TRUE);
                    return v;
                } else {
                    v.setValue(BOOLEAN_FALSE);
                    return v;
                }
            case OR:
                if (((Boolean) v1.getValue()).booleanValue() == true || ((Boolean) v2.getValue()).booleanValue() == true) {
                    v.setValue(BOOLEAN_TRUE);
                    return v;
                } else {
                    v.setValue(BOOLEAN_FALSE);
                    return v;
                }
            case EQUAL:
                if (v1.getType() == Value.Type.NUMBER && v2.getType() == Value.Type.NUMBER) {
                    if (v1.getNumberValue() == v2.getNumberValue()) {
                        v.setValue(BOOLEAN_TRUE);
                        return v;
                    } else {
                        v.setValue(BOOLEAN_FALSE);
                        return v;
                    }
                } else if ((v1.getType() == Value.Type.STRING || v1.getType() == Value.Type.NUMBER) && (v2.getType() == Value.Type.STRING || v2.getType() == Value.Type.NUMBER)) {
                    if (v1.getStringValue().equals(v2.getStringValue())) {
                        v.setValue(BOOLEAN_TRUE);
                        return v;
                    } else {
                        v.setValue(BOOLEAN_FALSE);
                        return v;
                    }
                }
                if (v1.getValue() == null || v2.getValue() == null) {
                    v.setValue(new Boolean(v1.getValue() == v2.getValue()));
                } else {
                    v.setValue(new Boolean(v1.getValue().equals(v2.getValue())));
                }
                return v;
            case NOT_EQUAL:
                if (v1.getType() == Value.Type.NUMBER && v2.getType() == Value.Type.NUMBER) {
                    if (v1.getNumberValue() != v2.getNumberValue()) {
                        v.setValue(BOOLEAN_TRUE);
                        return v;
                    } else {
                        v.setValue(BOOLEAN_FALSE);
                        return v;
                    }
                } else if ((v1.getType() == Value.Type.STRING || v1.getType() == Value.Type.NUMBER) && (v2.getType() == Value.Type.STRING || v2.getType() == Value.Type.NUMBER)) {
                    if (!v1.getStringValue().equals(v2.getStringValue())) {
                        v.setValue(BOOLEAN_TRUE);
                        return v;
                    } else {
                        v.setValue(BOOLEAN_FALSE);
                        return v;
                    }
                }
                if (v1.getValue() == null || v2.getValue() == null) {
                    v.setValue(new Boolean(v1.getValue() != v2.getValue()));
                } else {
                    v.setValue(new Boolean(!v1.getValue().equals(v2.getValue())));
                }
                return v;
            case LESS:
                if (v1.getType() == Value.Type.NUMBER && v2.getType() == Value.Type.NUMBER) {
                    if (v1.getNumberValue() < v2.getNumberValue()) {
                        v.setValue(BOOLEAN_TRUE);
                        return v;
                    } else {
                        v.setValue(BOOLEAN_FALSE);
                        return v;
                    }
                } else if ((v1.getType() == Value.Type.STRING || v1.getType() == Value.Type.NUMBER) && (v2.getType() == Value.Type.STRING || v2.getType() == Value.Type.NUMBER)) {
                    if (v1.getStringValue().compareTo(v2.getStringValue()) < 0) {
                        v.setValue(BOOLEAN_TRUE);
                        return v;
                    } else {
                        v.setValue(BOOLEAN_FALSE);
                        return v;
                    }
                }
                return null;
            case GREATER:
                if (v1.getType() == Value.Type.NUMBER && v2.getType() == Value.Type.NUMBER) {
                    if (v1.getNumberValue() > v2.getNumberValue()) {
                        v.setValue(BOOLEAN_TRUE);
                        return v;
                    } else {
                        v.setValue(BOOLEAN_FALSE);
                        return v;
                    }
                } else if ((v1.getType() == Value.Type.STRING || v1.getType() == Value.Type.NUMBER) && (v2.getType() == Value.Type.STRING || v2.getType() == Value.Type.NUMBER)) {
                    if (v1.getStringValue().compareTo(v2.getStringValue()) > 0) {
                        v.setValue(BOOLEAN_TRUE);
                        return v;
                    } else {
                        v.setValue(BOOLEAN_FALSE);
                        return v;
                    }
                }
                return null;
            case LESS_EQUAL:
                if (v1.getType() == Value.Type.NUMBER && v2.getType() == Value.Type.NUMBER) {
                    if (v1.getNumberValue() <= v2.getNumberValue()) {
                        v.setValue(BOOLEAN_TRUE);
                        return v;
                    } else {
                        v.setValue(BOOLEAN_FALSE);
                        return v;
                    }
                } else if ((v1.getType() == Value.Type.STRING || v1.getType() == Value.Type.NUMBER) && (v2.getType() == Value.Type.STRING || v2.getType() == Value.Type.NUMBER)) {
                    int c = v1.getStringValue().compareTo(v2.getStringValue());
                    if (c < 0 || c == 0) {
                        v.setValue(BOOLEAN_TRUE);
                        return v;
                    } else {
                        v.setValue(BOOLEAN_FALSE);
                        return v;
                    }
                }
                return null;
            case GREATER_EQUAL:
                if (v1.getType() == Value.Type.NUMBER && v2.getType() == Value.Type.NUMBER) {
                    if (v1.getNumberValue() >= v2.getNumberValue()) {
                        v.setValue(BOOLEAN_TRUE);
                        return v;
                    } else {
                        v.setValue(BOOLEAN_FALSE);
                        return v;
                    }
                } else if ((v1.getType() == Value.Type.STRING || v1.getType() == Value.Type.NUMBER) && (v2.getType() == Value.Type.STRING || v2.getType() == Value.Type.NUMBER)) {
                    int c = v1.getStringValue().compareTo(v2.getStringValue());
                    if (c > 0 || c == 0) {
                        v.setValue(BOOLEAN_TRUE);
                        return v;
                    } else {
                        v.setValue(BOOLEAN_FALSE);
                        return v;
                    }
                }
                return null;
            default:
                break;
        }
        return null;
    }
}
