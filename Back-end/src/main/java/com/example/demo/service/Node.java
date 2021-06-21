package com.example.demo.service;

public class Node {
    private Object element; private Node prev;
    /**
     * Node constructor
     * @param e The node element
     * @param n	The previous Node
     */
    public Node(Object e,Node n) {
        element=e; prev=n;
    }
    /**
     * This method return the node element
     * @return Node element
     */
    public Object getElement() {
        return element;
    }
    /**
     * This method returns the previous node
     * @return The previous node
     */
    public Node getPrev() {
        return prev;
    }
    /**
     * This method sets the element of the node
     * @param newElement the new element of the node
     */
    public void setElement(Object newElement) {
        element=newElement;
    }
    /**
     * This method sets the previous node
     * @param newPrev the new previous node
     */
    public void setPrev(Node newPrev) {
        prev=newPrev;
    }

}


