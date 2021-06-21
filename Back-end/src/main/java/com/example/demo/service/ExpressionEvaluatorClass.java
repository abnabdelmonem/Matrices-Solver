package com.example.demo.service;



public class ExpressionEvaluatorClass implements IExpressionEvaluator {
    NodeStack stack=new NodeStack(); int detectChar,varNum; boolean detectVar;
    NodeStack stack1=new NodeStack(); NodeStack stack2=new NodeStack();
    /**
     * This method checks for invalid parenthesis setup or invalid operators setup
     * @param expression The infix expression to be checked
     * @return 0 if the expression is invalid and 1 otherwise
     */
    public int checkInfix(String expression) {
        detectChar=0; varNum=0; detectVar=false;
        StringBuilder indicator=new StringBuilder (expression);
        //--------------------------------------------------------------
        while(!stack.isEmpty()) {
            stack.pop();
        }
        if(expression.equals("")) {
            return 0;
        }
        for(int i=0; i < expression.length(); i++) {
            if(expression.charAt(i)=='/') {
                return 0;
            }
            if(detectChar==0 && ( Character.isDigit(expression.charAt(i)) || Character.isAlphabetic(expression.charAt(i)) ) ) {
                detectChar=1;
            }
            if(expression.charAt(i)==' ' || expression.charAt(i)=='\t' || expression.charAt(i)=='\n') {continue;}
            else if(detectChar==0 && (expression.charAt(i)=='*' || expression.charAt(i)=='/' || expression.charAt(i)==')' || expression.charAt(i)=='=') ) { return 0;}
            else if(!Character.isDigit(expression.charAt(i)) && !Character.isAlphabetic(expression.charAt(i)) && expression.charAt(i)!='*' && expression.charAt(i)!='/' && expression.charAt(i)!='+' && expression.charAt(i)!='-' && expression.charAt(i)!='(' && expression.charAt(i)!=')' && expression.charAt(i)!='.' && expression.charAt(i)!='=') {return 0;}
            else if(Character.isAlphabetic(expression.charAt(i))) {
                if(indicator.charAt(i)==expression.charAt(i)) {
                    varNum++;
                }
                for(int t=i; t<expression.length(); t++) {
                    if(indicator.charAt(t) == expression.charAt(i)) {
                        indicator.setCharAt(t, '0');
                    }
                }
                detectVar=true;
                for(int j=i+1; j < expression.length(); j++) {
                    if(expression.charAt(j)==' ' || expression.charAt(j)=='\t' || expression.charAt(j)=='\n') {continue;}
                    else if(Character.isAlphabetic(expression.charAt(j))) {
                        return 0;
                    }else {break;}
                }
            }
            else if(expression.charAt(i)=='(') {
                stack.push('(');
                for(int j=i+1; j < expression.length(); j++) {
                    if(expression.charAt(j)==' ' || expression.charAt(j)=='\t' || expression.charAt(j)=='\n') {continue;}
                    else if(expression.charAt(j)=='/' || expression.charAt(j)=='*') {
                        return 0;
                    }else {break;}
                }
            }else if(expression.charAt(i)==')') {
                if((i+1) < expression.length()) {
                    for(int j=i+1; j < expression.length(); j++) {
                        if(expression.charAt(j)==' ' || expression.charAt(j)=='\t' || expression.charAt(j)=='\n') {continue;}
                        else if(expression.charAt(j)=='(') {
                            return 0;
                        }else {break;}
                    }
                }
                if(!stack.isEmpty() && (char)stack.pop()!='(') {
                    return 0;
                }
            }
            else if(expression.charAt(i)=='*' || expression.charAt(i)=='/' || expression.charAt(i)=='+' || expression.charAt(i)=='-') {
                if((i+1) < expression.length()) {
                    int found=0;
                    for(int j=i+1; j < expression.length(); j++) {
                        if(expression.charAt(j)==' ' || expression.charAt(j)=='\t' || expression.charAt(j)=='\n') {continue;}
                        else if(expression.charAt(j)=='*' || expression.charAt(j)=='/' || expression.charAt(j)==')') {
                            return 0;
                        }else {
                            found=1;
                            break;
                        }
                    }
                    if(found==0) {
                        return 0;
                    }
                }else {
                    return 0;
                }
            }
        }if(!stack.isEmpty()) {
            return 0;
        }
        for(int i=0; i<stack.size(); i++) {
            stack.pop();
        }//reaching this point means that the test succeeded
        if(detectChar==0) {
            return 0;
        }

        return 1;
    }

}
