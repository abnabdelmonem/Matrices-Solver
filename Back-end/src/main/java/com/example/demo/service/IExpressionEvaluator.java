package com.example.demo.service;

public interface IExpressionEvaluator {
    /**
     * This method checks for invalid parenthesis setup or invalid operators setup
     * @param expression The infix expression to be checked
     * @return 0 if the expression is invalid and 1 otherwise
     */
    public int checkInfix(String expression);
}
